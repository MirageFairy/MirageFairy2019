package miragefairy2019.mod3.worldgen

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod3.fairy.FairyTypes
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.worldgen.api.ApiMirageFlower
import net.minecraft.block.Block
import net.minecraft.block.BlockNewLog
import net.minecraft.block.BlockOldLog
import net.minecraft.block.BlockPlanks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.Mirror
import net.minecraft.util.NonNullList
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object FairyLog {
    lateinit var blockFairyLog: () -> BlockFairyLog
    lateinit var itemBlockFairyLog: () -> ItemBlock
    val module: Module = {
        blockFairyLog = block({ BlockFairyLog() }, "fairy_log") {
            setUnlocalizedName("fairyLog")
            setCreativeTab { ApiMain.creativeTab }
        }
        itemBlockFairyLog = item({ ItemBlock(blockFairyLog()) }, "fairy_log") {
            setUnlocalizedName("fairyLog")
            setCreativeTab { ApiMain.creativeTab }
            setCustomModelResourceLocation(variant = "facing=north,variant=oak")
        }
    }
}

class BlockFairyLog : Block(Material.WOOD) {
    companion object {
        val VARIANT: PropertyEnum<BlockPlanks.EnumType> = PropertyEnum.create("variant", BlockPlanks.EnumType::class.java)
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)
    }

    init {

        // meta
        defaultState = blockState.baseState.withProperty(VARIANT, BlockPlanks.EnumType.OAK).withProperty(FACING, EnumFacing.NORTH)

        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(2.0f)
        setHarvestLevel("axe", 0)

    }

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

    override fun createBlockState() = BlockStateContainer(this, VARIANT, FACING)

    fun getState(facing: EnumFacing): IBlockState = defaultState.withProperty(FACING, facing)

    override fun getActualState(blockState: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = null
        ?: getActualState0(blockState, world, pos.up())
        ?: getActualState0(blockState, world, pos.down())
        ?: getActualState0(blockState, world, pos.west())
        ?: getActualState0(blockState, world, pos.east())
        ?: getActualState0(blockState, world, pos.north())
        ?: getActualState0(blockState, world, pos.south())
        ?: blockState

    private fun getActualState0(blockState: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState? = when (world.getBlockState(pos)) {
        Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK) -> blockState.withProperty(VARIANT, BlockPlanks.EnumType.OAK)
        Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE) -> blockState.withProperty(VARIANT, BlockPlanks.EnumType.SPRUCE)
        Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH) -> blockState.withProperty(VARIANT, BlockPlanks.EnumType.BIRCH)
        Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE) -> blockState.withProperty(VARIANT, BlockPlanks.EnumType.JUNGLE)
        Blocks.LOG2.defaultState.withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA) -> blockState.withProperty(VARIANT, BlockPlanks.EnumType.ACACIA)
        Blocks.LOG2.defaultState.withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK) -> blockState.withProperty(VARIANT, BlockPlanks.EnumType.DARK_OAK)
        else -> null
    }

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)))

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.horizontalFacing.opposite), 2)
    }


    // 動作

    // ドロップ

    /**
     * クリエイティブピックでの取得アイテム。
     */
    override fun getItem(world: World, pos: BlockPos, state: IBlockState) = ItemStack(FairyLog.itemBlockFairyLog())

    override fun getDrops(drops: NonNullList<ItemStack>, blockAccess: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) {
        if (blockAccess !is World) return
        repeat(3 + fortune) {
            drops.add(ApiMirageFlower.fairyLogDropRegistry.drop(blockAccess, pos, blockAccess.rand) ?: FairyTypes.instance.air.main.createItemStack())
        }
    }

    /**
     * シルクタッチ無効。
     */
    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = false

    //

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED

}
