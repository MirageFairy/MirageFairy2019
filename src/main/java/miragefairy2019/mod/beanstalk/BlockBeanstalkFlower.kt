package miragefairy2019.mod.beanstalk

import miragefairy2019.lib.FacedBlockPos
import miragefairy2019.lib.TileEntityIgnoreBlockState
import miragefairy2019.mod.fairybox.randomSkipTicks
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BlockBeanstalkFlower<T : TileEntityBeanstalkFlower> : BlockBeanstalkEnd(), ITileEntityProvider {
    init {
        isBlockContainer = true
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

abstract class TileEntityBeanstalkFlower : TileEntityIgnoreBlockState(), ITickable {

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
