package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object FairyResinTapper {
    lateinit var blockFairyResinTapper: () -> BlockFairyResinTapper
    lateinit var itemBlockFairyResinTapper: () -> ItemBlock
    val module: Module = {
        blockFairyResinTapper = block({ BlockFairyResinTapper() }, "fairy_resin_tapper") {
            setUnlocalizedName("fairyResinTapper")
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).map { facing ->
                        "facing=${facing.first}" to DataBlockState(
                            "miragefairy2019:fairy_resin_tapper",
                            y = facing.second
                        )
                    }
                )
            }
        }
        itemBlockFairyResinTapper = item({ ItemBlock(blockFairyResinTapper()) }, "fairy_resin_tapper") {
            setCustomModelResourceLocation(variant = "facing=north")
        }
        tileEntity("fairy_resin_tapper", TileEntityFairyResinTapper::class.java)
    }
}

class BlockFairyResinTapper : BlockFairyBoxBase() {
    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyResinTapper()
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        val tileEntity = worldIn.getTileEntity(pos) as? TileEntityFairyResinTapper ?: return false
        val executor = tileEntity.executor ?: return false
        return executor.onBlockActivated(playerIn, hand, facing, hitX, hitY, hitZ)
    }
}

abstract class TileEntityFairyBoxBase : TileEntity(), ITickable {
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

        executor?.onUpdateTick()
    }


    abstract val executor: TileEntityExecutor?
}

class TileEntityFairyResinTapper : TileEntityFairyBoxBase() {
    override val executor: TileEntityExecutor?
        get() {
            return null
        }
}
