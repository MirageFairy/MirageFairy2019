package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataPart
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.with
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLiving
import net.minecraft.item.ItemBlock
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object FairyCrystalGlass {
    lateinit var blockFairyCrystalGlass: () -> BlockFairyCrystalGlass
    lateinit var itemBlockFairyCrystalGlass: () -> ItemBlock
    lateinit var blockPureFairyCrystalGlass: () -> BlockFairyCrystalGlass
    lateinit var itemBlockPureFairyCrystalGlass: () -> ItemBlock
    val module: Module = {
        // 通常
        blockFairyCrystalGlass = block({ BlockFairyCrystalGlass() }, "fairy_crystal_glass") {
            setUnlocalizedName("fairyCrystalGlass")
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    multipart = listOf(
                        DataPart(
                            `when` = mapOf("down" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 180)
                        ),
                        DataPart(
                            `when` = mapOf("up" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame")
                        ),
                        DataPart(
                            `when` = mapOf("north" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 90)
                        ),
                        DataPart(
                            `when` = mapOf("south" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = -90)
                        ),
                        DataPart(
                            `when` = mapOf("west" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 90, y = -90)
                        ),
                        DataPart(
                            `when` = mapOf("east" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 90, y = 90)
                        ),
                        DataPart(
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass")
                        )
                    )
                )
            }
        }
        itemBlockFairyCrystalGlass = item({ ItemBlock(blockFairyCrystalGlass()) }, "fairy_crystal_glass") {
            addOreName("blockMirageFairyCrystal")
            setCustomModelResourceLocation(model = ResourceLocation(ModMirageFairy2019.MODID, "fairy_crystal_glass"))
        }
        onMakeLang { enJa("tile.fairyCrystalGlass.name", "Fairy Crystal Glass", "フェアリークリスタルガラス") }
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "fairy_crystal_glass"),
            DataShapelessRecipe(
                ingredients = (1..8).map { DataOreIngredient(ore = "mirageFairyCrystal") },
                result = DataResult(
                    item = "${ModMirageFairy2019.MODID}:fairy_crystal_glass"
                )
            )
        )
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "fairy_crystal_from_fairy_crystal_glass"),
            DataShapelessRecipe(
                ingredients = listOf(DataOreIngredient(ore = "blockMirageFairyCrystal")),
                result = DataResult(
                    item = "${ModMirageFairy2019.MODID}:fairy_crystal",
                    data = 0,
                    count = 8
                )
            )
        )

        // 高純度
        blockPureFairyCrystalGlass = block({ BlockFairyCrystalGlass() }, "pure_fairy_crystal_glass") {
            setUnlocalizedName("pureFairyCrystalGlass")
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    multipart = listOf(
                        DataPart(
                            `when` = mapOf("down" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 180)
                        ),
                        DataPart(
                            `when` = mapOf("up" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame")
                        ),
                        DataPart(
                            `when` = mapOf("north" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 90)
                        ),
                        DataPart(
                            `when` = mapOf("south" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = -90)
                        ),
                        DataPart(
                            `when` = mapOf("west" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 90, y = -90)
                        ),
                        DataPart(
                            `when` = mapOf("east" to false),
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:fairy_crystal_glass_frame", x = 90, y = 90)
                        ),
                        DataPart(
                            apply = DataBlockState("${ModMirageFairy2019.MODID}:pure_fairy_crystal_glass")
                        )
                    )
                )
            }
        }
        itemBlockPureFairyCrystalGlass = item({ ItemBlock(blockPureFairyCrystalGlass()) }, "pure_fairy_crystal_glass") {
            addOreName("blockMirageFairyCrystalPure")
            setCustomModelResourceLocation(model = ResourceLocation(ModMirageFairy2019.MODID, "pure_fairy_crystal_glass"))
        }
        onMakeLang { enJa("tile.pureFairyCrystalGlass.name", "Pure Fairy Crystal Glass", "高純度フェアリークリスタルガラス") }
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "pure_fairy_crystal_glass"),
            DataShapelessRecipe(
                ingredients = (1..8).map { DataOreIngredient(ore = "mirageFairyCrystalPure") },
                result = DataResult(
                    item = "${ModMirageFairy2019.MODID}:pure_fairy_crystal_glass"
                )
            )
        )
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "pure_fairy_crystal_from_pure_fairy_crystal_glass"),
            DataShapelessRecipe(
                ingredients = listOf(DataOreIngredient(ore = "blockMirageFairyCrystalPure")),
                result = DataResult(
                    item = "${ModMirageFairy2019.MODID}:fairy_crystal",
                    data = 2,
                    count = 8
                )
            )
        )
    }
}

class BlockFairyCrystalGlass : Block(Material.GLASS) {
    companion object {
        val DOWN: PropertyBool = PropertyBool.create("down")
        val UP: PropertyBool = PropertyBool.create("up")
        val NORTH: PropertyBool = PropertyBool.create("north")
        val SOUTH: PropertyBool = PropertyBool.create("south")
        val WEST: PropertyBool = PropertyBool.create("west")
        val EAST: PropertyBool = PropertyBool.create("east")
    }

    // BlockState

    init {
        defaultState = blockState.baseState.with(DOWN, false).with(UP, false).with(NORTH, false).with(SOUTH, false).with(WEST, false).with(EAST, false)
    }

    override fun createBlockState() = BlockStateContainer(this, DOWN, UP, NORTH, SOUTH, WEST, EAST)
    override fun getMetaFromState(blockState: IBlockState) = 0
    override fun getStateFromMeta(metadata: Int): IBlockState = defaultState
    override fun getActualState(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos) = blockState
        .let { if (world.getBlockState(blockPos.offset(EnumFacing.DOWN)) == blockState) it.with(DOWN, true) else it }
        .let { if (world.getBlockState(blockPos.offset(EnumFacing.UP)) == blockState) it.with(UP, true) else it }
        .let { if (world.getBlockState(blockPos.offset(EnumFacing.NORTH)) == blockState) it.with(NORTH, true) else it }
        .let { if (world.getBlockState(blockPos.offset(EnumFacing.SOUTH)) == blockState) it.with(SOUTH, true) else it }
        .let { if (world.getBlockState(blockPos.offset(EnumFacing.WEST)) == blockState) it.with(WEST, true) else it }
        .let { if (world.getBlockState(blockPos.offset(EnumFacing.EAST)) == blockState) it.with(EAST, true) else it }


    // 挙動

    init {
        setHardness(1.0f)
        setHarvestLevel("pickaxe", 0)
    }

    override fun isOpaqueCube(blockState: IBlockState) = false
    override fun isFullCube(blockState: IBlockState) = false
    override fun isSideSolid(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos, facing: EnumFacing) = true
    override fun canCreatureSpawn(state: IBlockState, world: IBlockAccess, pos: BlockPos, type: EntityLiving.SpawnPlacementType) = false


    // レンダリング

    init {
        soundType = SoundType.GLASS
    }

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos, facing: EnumFacing): Boolean {
        if (blockState.block === world.getBlockState(blockPos.offset(facing)).block) return false // ブロックが等しい場合は表示しない
        return super.shouldSideBeRendered(blockState, world, blockPos, facing)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT
}
