package miragefairy2019.mod.fairybox

import mirrg.kotlin.castOrNull
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random
import kotlin.math.floor
import kotlin.math.log

class BlockFairyBoxBase(private val tileEntityProvider: () -> TileEntityFairyBoxBase) : BlockContainer(Material.WOOD) {
    init {

        // meta
        defaultState = blockState.baseState.withProperty(FACING, EnumFacing.NORTH)

        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(2.0f)
        setHarvestLevel("axe", 0)

    }


    // Variant

    override fun getMetaFromState(state: IBlockState) = when (state.getValue(FACING)) {
        EnumFacing.NORTH -> 0
        EnumFacing.SOUTH -> 1
        EnumFacing.WEST -> 2
        EnumFacing.EAST -> 3
        else -> 0
    }

    override fun getStateFromMeta(meta: Int): IBlockState = when (meta) {
        0 -> defaultState.withProperty(FACING, EnumFacing.NORTH)
        1 -> defaultState.withProperty(FACING, EnumFacing.SOUTH)
        2 -> defaultState.withProperty(FACING, EnumFacing.WEST)
        3 -> defaultState.withProperty(FACING, EnumFacing.EAST)
        else -> defaultState
    }

    override fun createBlockState() = BlockStateContainer(this, FACING)

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))

    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)))

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.horizontalFacing.opposite), 2)
    }

    fun getFacing(blockState: IBlockState): EnumFacing = blockState.getValue(FACING)


    // Tile Entity
    override fun createNewTileEntity(worldIn: World, meta: Int) = tileEntityProvider()


    // Graphics
    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL


    // Action
    fun getTileEntity(world: World, blockPos: BlockPos) = world.getTileEntity(blockPos)?.castOrNull<TileEntityFairyBoxBase>()
    override fun onBlockActivated(world: World, blockPos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = getTileEntity(world, blockPos) ?: return super.onBlockActivated(world, blockPos, state, playerIn, hand, facing, hitX, hitY, hitZ)
        return tileEntity.getExecutor().onBlockActivated(playerIn, hand, facing, hitX, hitY, hitZ)
    }


    companion object {
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)
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

        getExecutor().onUpdateTick()
    }

    open fun getExecutor() = object : IFairyBoxExecutor {}
}

interface IFairyBoxExecutor {
    fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = false
    fun onUpdateTick() = Unit
}

/** @return 0以上の値 */
fun randomSkipTicks(random: Random, rate: Double) = floor(log(random.nextDouble(), 1 - rate)).toInt()
