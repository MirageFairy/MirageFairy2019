package miragefairy2019.mod.beanstalk

import miragefairy2019.mod.fairybox.randomSkipTicks
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BlockBeanstalkFlower<T : TileEntity> : BlockBeanstalkEnd(), ITileEntityProvider {
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
        val tileEntity: TileEntity? = world.getTileEntity(blockPos)
        return tileEntity?.receiveClientEvent(id, param) ?: false
    }
}

abstract class TileEntityBeanstalkFlower : TileEntity(), ITickable {

    fun getEncounterBlockPos(): BlockPos? {
        val blockState = world.getBlockState(pos)
        val block = blockState.block as? IBeanstalkBlock ?: return null
        val facing = block.getFacing(blockState, world, pos) ?: return null
        return pos.offset(facing)
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

        onUpdateTick()
    }

    abstract fun onUpdateTick()

}

fun getRootBlockPos(world: World, blockPos: BlockPos): BlockPos? {
    var currentBlockPos = blockPos.toImmutable()
    val acceptedBlockPoses = mutableSetOf<BlockPos>()

    while (true) {
        if (!world.isBlockLoaded(currentBlockPos)) return null // チャンクロード範囲外の場合は失敗
        val blockState = world.getBlockState(currentBlockPos)
        val block = blockState.block as? IBeanstalkBlock ?: return currentBlockPos // 豆の木でない場合は終了
        val facing = block.getFacing(blockState, world, currentBlockPos) ?: return currentBlockPos // 豆の木でない場合は終了
        currentBlockPos = currentBlockPos.offset(facing)
        if (currentBlockPos in acceptedBlockPoses) return null // ループを検出
        acceptedBlockPoses += currentBlockPos
    }
}
