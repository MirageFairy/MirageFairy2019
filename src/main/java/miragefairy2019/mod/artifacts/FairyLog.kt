package miragefairy2019.mod.artifacts

import miragefairy2019.lib.selectFairyLogDrop
import miragefairy2019.resourcemaker.DataBlockState
import miragefairy2019.resourcemaker.DataBlockStates
import miragefairy2019.libkt.ResourceName
import miragefairy2019.resourcemaker.block
import miragefairy2019.libkt.item
import miragefairy2019.resourcemaker.makeBlockItemModel
import miragefairy2019.resourcemaker.makeBlockModel
import miragefairy2019.resourcemaker.makeBlockStates
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.fairy.FairyTypes
import mirrg.boron.util.UtilsMath
import mirrg.kotlin.gson.jsonElement
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
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.terraingen.DecorateBiomeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object FairyLog {
    lateinit var blockFairyLog: () -> BlockFairyLog
    lateinit var itemBlockFairyLog: () -> ItemBlock
    val module = module {

        blockFairyLog = block({ BlockFairyLog() }, "fairy_log") {
            setUnlocalizedName("fairyLog")
            setCreativeTab { Main.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf("oak", "birch", "spruce", "jungle", "acacia", "dark_oak").flatMap { variant ->
                        listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).map { facing ->
                            "facing=${facing.first},variant=$variant" to DataBlockState("miragefairy2019:${variant}_fairy_log", y = facing.second)
                        }
                    }.toMap()
                )
            }
        }
        run {
            fun makeBlockModel(name: String, variant: String) = makeBlockModel(ResourceName(ModMirageFairy2019.MODID, name)) {
                jsonElement(
                    "parent" to "miragefairy2019:block/fairy_log".jsonElement,
                    "textures" to jsonElement(
                        "end" to "blocks/log_${variant}_top".jsonElement,
                        "side" to "blocks/log_$variant".jsonElement
                    )
                )
            }
            makeBlockModel("acacia_fairy_log", "acacia")
            makeBlockModel("birch_fairy_log", "birch")
            makeBlockModel("dark_oak_fairy_log", "big_oak")
            makeBlockModel("jungle_fairy_log", "jungle")
            makeBlockModel("oak_fairy_log", "oak")
            makeBlockModel("spruce_fairy_log", "spruce")
        }
        itemBlockFairyLog = item({ ItemBlock(blockFairyLog()) }, "fairy_log") {
            setUnlocalizedName("fairyLog")
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation(variant = "facing=north,variant=oak")
        }
        makeBlockItemModel(ResourceName(ModMirageFairy2019.MODID, "fairy_log"))

        // 地形生成
        onHookDecorator {
            MinecraftForge.EVENT_BUS.register(object {
                @SubscribeEvent
                fun handle(event: DecorateBiomeEvent.Post) {
                    val times = UtilsMath.randomInt(event.rand, (event.world.height.toDouble() / 16.0) * 50.0) // 1セクションあたり50回判定を行う // TODO
                    repeat(times) {
                        val facing = EnumFacing.HORIZONTALS[event.rand.nextInt(4)]
                        val posLog = event.chunkPos.getBlock(
                            event.rand.nextInt(16) + 8,
                            event.rand.nextInt(event.world.height),
                            event.rand.nextInt(16) + 8
                        )
                        val posAir = posLog.offset(facing)

                        if (!event.world.getBlockState(posAir).isSideSolid(event.world, posAir, facing.opposite)) {
                            when (event.world.getBlockState(posLog)) {
                                Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK),
                                Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
                                Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH),
                                Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
                                Blocks.LOG2.defaultState.withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA),
                                Blocks.LOG2.defaultState.withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK)
                                -> event.world.setBlockState(posLog, blockFairyLog().getState(facing), 2)
                            }
                        }
                    }
                }
            })
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
            drops.add(selectFairyLogDrop(blockAccess, pos, blockAccess.rand) ?: FairyTypes.instance.air.main.createItemStack())
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
