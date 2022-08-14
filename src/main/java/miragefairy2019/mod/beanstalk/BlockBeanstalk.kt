package miragefairy2019.mod.beanstalk

import miragefairy2019.mod.systems.IFacedCursor
import miragefairy2019.mod.systems.IFacedCursorBlock
import miragefairy2019.mod.systems.IFacedCursorHandler
import miragefairy2019.mod.systems.getAdvancedFacing
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface IBeanstalkBlock {
    fun getFacing(blockState: IBlockState, world: IBlockAccess, pos: BlockPos): EnumFacing?
}

abstract class BlockBeanstalk : Block(Material.WOOD), IBeanstalkBlock, IFacedCursorBlock {
    companion object {
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java)
    }


    init {
        soundType = SoundType.PLANT
        setHardness(2.0f)
        setHarvestLevel("axe", 0)
    }


    // Metadata

    init {
        defaultState = blockState.baseState.withProperty(FACING, EnumFacing.DOWN)
    }

    fun getBlockState(facing: EnumFacing): IBlockState = defaultState.withProperty(FACING, facing)
    fun getFacing(blockState: IBlockState): EnumFacing = blockState.getValue(FACING)
    override fun getFacing(blockState: IBlockState, world: IBlockAccess, pos: BlockPos) = getFacing(blockState)
    override fun getStateFromMeta(meta: Int) = getBlockState(EnumFacing.VALUES[meta atLeast 0 atMost 6])
    override fun getMetaFromState(blockState: IBlockState) = getFacing(blockState).index
    override fun createBlockState() = BlockStateContainer(this, FACING)
    override fun withRotation(blockState: IBlockState, rotation: Rotation): IBlockState = getBlockState(rotation.rotate(getFacing(blockState)))
    override fun withMirror(blockState: IBlockState, mirror: Mirror): IBlockState = getBlockState(mirror.mirror(getFacing(blockState)))

    override fun getStateForPlacement(world: World, blockPos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, metadata: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
        return getBlockState(getAdvancedFacing(facing, hitX, hitY, hitZ, 6.0 / 16.0))
    }


    // レンダリング

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun getBlockFaceShape(world: IBlockAccess, blockState: IBlockState, blockPos: BlockPos, facing: EnumFacing) = BlockFaceShape.UNDEFINED
    override fun getFacedCursorHandler(itemStack: ItemStack) = object : IFacedCursorHandler {
        override fun getFacedCursor(item: Item, itemStack: ItemStack, world: World, blockPos: BlockPos, player: EntityPlayer, rayTraceResult: RayTraceResult) = object : IFacedCursor {
            override val borderSize get() = 6.0 / 16.0
        }
    }


    // 性質

    override fun isLadder(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos, entity: EntityLivingBase) = true

}

open class BlockBeanstalkEnd : BlockBeanstalk() {

    // Box

    private val collisionBoundingBoxDown = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 16 / 16.0, 11 / 16.0)
    private val collisionBoundingBoxUp = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 16 / 16.0, 11 / 16.0)
    private val collisionBoundingBoxNorth = AxisAlignedBB(5 / 16.0, 5 / 16.0, 0 / 16.0, 11 / 16.0, 11 / 16.0, 16 / 16.0)
    private val collisionBoundingBoxSouth = AxisAlignedBB(5 / 16.0, 5 / 16.0, 0 / 16.0, 11 / 16.0, 11 / 16.0, 16 / 16.0)
    private val collisionBoundingBoxWest = AxisAlignedBB(0 / 16.0, 5 / 16.0, 5 / 16.0, 16 / 16.0, 11 / 16.0, 11 / 16.0)
    private val collisionBoundingBoxEast = AxisAlignedBB(0 / 16.0, 5 / 16.0, 5 / 16.0, 16 / 16.0, 11 / 16.0, 11 / 16.0)
    override fun getCollisionBoundingBox(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos) = when (getFacing(blockState)) {
        EnumFacing.DOWN -> collisionBoundingBoxDown
        EnumFacing.UP -> collisionBoundingBoxUp
        EnumFacing.NORTH -> collisionBoundingBoxNorth
        EnumFacing.SOUTH -> collisionBoundingBoxSouth
        EnumFacing.WEST -> collisionBoundingBoxWest
        EnumFacing.EAST -> collisionBoundingBoxEast
    }

    override fun getBoundingBox(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos) = getCollisionBoundingBox(blockState, world, blockPos)

}
