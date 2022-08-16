package miragefairy2019.mod.beanstalk

import miragefairy2019.lib.get
import miragefairy2019.lib.indices
import miragefairy2019.libkt.equalsItemDamageTag
import miragefairy2019.mod.fairybox.randomSkipTicks
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.IItemHandlerModifiable

abstract class BlockBeanstalkFlower<T : TileEntityBeanstalkFlower> : BlockBeanstalkEnd(), ITileEntityProvider {
    init {
        hasTileEntity = true
    }

    abstract fun validateTileEntity(tileEntity: TileEntity): T?
    fun getTileEntity(world: World, blockPos: BlockPos) = world.getTileEntity(blockPos)?.let { validateTileEntity(it) }

    abstract fun createNewTileEntity(): T
    override fun createNewTileEntity(world: World, meta: Int) = createNewTileEntity()

    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        super.breakBlock(world, blockPos, blockState)
        world.removeTileEntity(blockPos)
    }

    override fun eventReceived(blockState: IBlockState, world: World, blockPos: BlockPos, id: Int, param: Int): Boolean {
        super.eventReceived(blockState, world, blockPos, id, param)
        val tileEntity = world.getTileEntity(blockPos)
        return tileEntity?.receiveClientEvent(id, param) ?: false
    }

    override fun onBlockActivated(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = getTileEntity(world, blockPos) ?: return false
        tileEntity.doAction()
        return true
    }

}

abstract class TileEntityBeanstalkFlower : TileEntity(), ITickable {

    fun getEncounterBlockPos(): FacedBlockPos? {
        val blockState = world.getBlockState(pos)
        val block = blockState.block as? IBeanstalkBlock ?: return null
        val facing = block.getFacing(blockState, world, pos) ?: return null
        return FacedBlockPos(pos.offset(facing.opposite), facing)
    }


    // Tick

    private var tick = -1

    override fun update() {
        if (world.isRemote) return // サーバーワールドのみ

        // 平均して1分に1回行動する
        val interval = 20 * 60
        if (tick < 0) tick = randomSkipTicks(world.rand, 1 / interval.toDouble())
        if (tick != 0) {
            tick--
            return
        } else {
            tick = randomSkipTicks(world.rand, 1 / interval.toDouble())
        }

        doAction()
    }

    abstract fun doAction()

}

// TODO -> lib
class FacedBlockPos(val blockPos: BlockPos, val facing: EnumFacing)

fun getRoot(world: World, blockPos: BlockPos): FacedBlockPos? {
    fun getFacing(blockPos: BlockPos): EnumFacing? {
        if (!world.isBlockLoaded(blockPos)) return null // チャンクロード範囲外の場合は失敗
        val blockState = world.getBlockState(blockPos)
        val block = blockState.block as? IBeanstalkBlock ?: return null // 豆の木でない場合は終了
        return block.getFacing(blockState, world, blockPos)
    }

    var currentBlockPos = blockPos.toImmutable()
    val acceptedBlockPoses = mutableSetOf<BlockPos>()

    val firstFacing = getFacing(currentBlockPos) ?: return null // 豆の木でない場合は終了
    currentBlockPos = currentBlockPos.offset(firstFacing)
    var lastFacing = firstFacing

    while (true) {
        val facing = getFacing(currentBlockPos) ?: return FacedBlockPos(currentBlockPos, lastFacing.opposite) // 豆の木でない場合は終了
        currentBlockPos = currentBlockPos.offset(facing)
        lastFacing = facing
        if (currentBlockPos in acceptedBlockPoses) return null // ループを検出
        acceptedBlockPoses += currentBlockPos
    }
}

fun move(power: Int, srcItemHandler: IItemHandlerModifiable, destItemHandler: IItemHandlerModifiable): List<ItemStack> {
    val movedItemStacks = mutableListOf<ItemStack>()
    var remainingPower = power

    run finish@{
        srcItemHandler.indices.forEach nextSrcSlot@{ srcIndex ->

            if (remainingPower <= 0) return@finish // もうパワーがない
            // まだパワーが残っている

            val virtualSrcItemStack = srcItemHandler.extractItem(srcIndex, 64, true).copy() // srcから搬出可能なアイテム
            val originalSrcItemStack = virtualSrcItemStack.copy()

            if (virtualSrcItemStack.isEmpty) return@nextSrcSlot // 空のsrcスロットは無視
            // srcスロットに何かが入っている

            // 成立

            var movedCount = 0
            try {
                val notEmptyDestIndices = mutableListOf<Int>()
                val emptyDestIndices = mutableListOf<Int>()
                destItemHandler.indices.forEach { destIndex ->
                    val destItemStack = destItemHandler[destIndex]
                    if (destItemStack.isEmpty) {
                        emptyDestIndices += destIndex
                    } else if (destItemStack equalsItemDamageTag virtualSrcItemStack) {
                        notEmptyDestIndices += destIndex
                    }
                }
                (notEmptyDestIndices + emptyDestIndices).forEach nextDestSlot@{ destIndex ->

                    if (virtualSrcItemStack.isEmpty) return@nextSrcSlot // すべて移動し終えた
                    // まだ移動するものが残っている

                    if (!destItemHandler.isItemValid(destIndex, virtualSrcItemStack)) return@nextDestSlot // 宛先がこのアイテムを受け付けない
                    // 宛先がこのアイテムを受け付ける

                    // 移動を試す
                    val remainingSrcItemStack = destItemHandler.insertItem(destIndex, virtualSrcItemStack.copy(), false)
                    if (remainingSrcItemStack.count != virtualSrcItemStack.count) { // 移動が発生した場合
                        movedCount += virtualSrcItemStack.count - remainingSrcItemStack.count
                        virtualSrcItemStack.count = remainingSrcItemStack.count
                    }

                }
            } finally {
                if (movedCount > 0) { // 移動が発生した
                    originalSrcItemStack.count = movedCount
                    movedItemStacks += originalSrcItemStack // 移動したアイテム標本に追加
                    remainingPower-- // パワー消費
                    val removedSrcItemStack = srcItemHandler.extractItem(srcIndex, movedCount, false) // 抽出イベント発火
                    assert(removedSrcItemStack.count == movedCount) { "異常な移動処理が発生しました: ${removedSrcItemStack.count}, $movedCount" }
                }
            }

        }
    }

    return movedItemStacks
}
