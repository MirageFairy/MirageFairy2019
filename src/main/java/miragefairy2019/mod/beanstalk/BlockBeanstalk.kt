package miragefairy2019.mod.beanstalk

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
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class BlockBeanstalk : Block(Material.WOOD) {
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
    override fun getStateFromMeta(meta: Int) = getBlockState(EnumFacing.VALUES[meta atLeast 0 atMost 6])
    override fun getMetaFromState(blockState: IBlockState) = getFacing(blockState).index
    override fun createBlockState() = BlockStateContainer(this, FACING)
    override fun withRotation(blockState: IBlockState, rotation: Rotation): IBlockState = getBlockState(rotation.rotate(getFacing(blockState)))
    override fun withMirror(blockState: IBlockState, mirror: Mirror): IBlockState = getBlockState(mirror.mirror(getFacing(blockState)))

    override fun getStateForPlacement(world: World, blockPos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, metadata: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
        return getBlockState(facing.opposite)
    }




    // レンダリング

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun getBlockFaceShape(world: IBlockAccess, blockState: IBlockState, blockPos: BlockPos, facing: EnumFacing) = BlockFaceShape.UNDEFINED


    // 性質

    override fun isLadder(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos, entity: EntityLivingBase) = true

}
