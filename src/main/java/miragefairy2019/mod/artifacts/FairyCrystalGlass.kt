package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.BlockScope
import miragefairy2019.lib.modinitializer.ItemScope
import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataSelector
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.with
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.hydrogen.toLowerCaseHead
import mirrg.kotlin.toSnakeCase
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
    lateinit var blockVeryPureFairyCrystalGlass: () -> BlockFairyCrystalGlass
    lateinit var itemBlockVeryPureFairyCrystalGlass: () -> ItemBlock
    lateinit var blockWildFairyCrystalGlass: () -> BlockFairyCrystalGlass
    lateinit var itemBlockWildFairyCrystalGlass: () -> ItemBlock
    lateinit var blockVeryWildFairyCrystalGlass: () -> BlockFairyCrystalGlass
    lateinit var itemBlockVeryWildFairyCrystalGlass: () -> ItemBlock
    val fairyCrystalGlassModule = module {

        fun cube(texture: String, rotation: Int?) = DataElement(
            from = DataPoint(0.0, 0.0, 0.0),
            to = DataPoint(16.0, 16.0, 16.0),
            faces = DataFaces(
                down = DataFace(texture = texture, cullface = "down", rotation = rotation),
                up = DataFace(texture = texture, cullface = "up", rotation = rotation),
                north = DataFace(texture = texture, cullface = "north", rotation = rotation),
                south = DataFace(texture = texture, cullface = "south", rotation = rotation),
                west = DataFace(texture = texture, cullface = "west", rotation = rotation),
                east = DataFace(texture = texture, cullface = "east", rotation = rotation)
            )
        )


        fun makeParticleModel(name: String) = makeBlockModel(name) {
            DataModel(
                parent = "block/block",
                elements = listOf(
                    DataElement(
                        from = DataPoint(8.0, 8.0, 8.0),
                        to = DataPoint(8.0, 8.0, 8.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#particle", cullface = "down"),
                            up = DataFace(texture = "#particle", cullface = "up"),
                            north = DataFace(texture = "#particle", cullface = "north"),
                            south = DataFace(texture = "#particle", cullface = "south"),
                            west = DataFace(texture = "#particle", cullface = "west"),
                            east = DataFace(texture = "#particle", cullface = "east")
                        )
                    )
                ),
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/$name"
                )
            )
        }
        makeParticleModel("fairy_crystal_glass_particle")
        makeParticleModel("pure_fairy_crystal_glass_particle")
        makeParticleModel("very_pure_fairy_crystal_glass_particle")
        makeParticleModel("wild_fairy_crystal_glass_particle")
        makeParticleModel("very_wild_fairy_crystal_glass_particle")


        fun makeBackgroundModel(name: String) = makeBlockModel(name) {
            DataModel(
                parent = "block/block",
                elements = listOf(
                    cube("#background", null)
                ),
                textures = mapOf(
                    "background" to "miragefairy2019:blocks/$name"
                )
            )
        }
        makeBackgroundModel("fairy_crystal_glass_background")
        makeBackgroundModel("pure_fairy_crystal_glass_background")
        makeBackgroundModel("very_pure_fairy_crystal_glass_background")


        fun makeFrameModel(name: String) = makeBlockModel(name) {
            DataModel(
                elements = listOf(
                    DataElement(
                        from = DataPoint(0.0, 0.0, 0.0),
                        to = DataPoint(16.0, 16.0, 16.0),
                        faces = DataFaces(
                            north = DataFace(texture = "#frame", cullface = "north", rotation = null),
                            south = DataFace(texture = "#frame", cullface = "south", rotation = null),
                            west = DataFace(texture = "#frame", cullface = "west", rotation = null),
                            east = DataFace(texture = "#frame", cullface = "east", rotation = null)
                        )
                    )
                ),
                textures = mapOf(
                    "frame" to "miragefairy2019:blocks/$name"
                )
            )
        }
        makeFrameModel("fairy_crystal_glass_frame")
        makeFrameModel("wild_fairy_crystal_glass_frame")


        fun fairyCrystalGlass(
            crystalMetadata: Int,
            prefix: String,
            backgroundPrefix: String,
            framePrefix: String,
            englishPrefix: String,
            japanesePrefix: String
        ): Pair<BlockScope<BlockFairyCrystalGlass>, ItemScope<ItemBlock>> {
            val block = block({ BlockFairyCrystalGlass() }, "${prefix}FairyCrystalGlass".toSnakeCase()) {
                setUnlocalizedName("${prefix}FairyCrystalGlass".toLowerCaseHead())
                setCreativeTab { Main.creativeTab }
                makeBlockStates(resourceName.path) {
                    DataModelBlockDefinition(
                        multipart = listOf(
                            DataSelector(
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${prefix}FairyCrystalGlassParticle".toSnakeCase()}")
                            ),
                            DataSelector(
                                `when` = mapOf("down" to false.jsonElement),
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 180)
                            ),
                            DataSelector(
                                `when` = mapOf("up" to false.jsonElement),
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}")
                            ),
                            DataSelector(
                                `when` = mapOf("north" to false.jsonElement),
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 90)
                            ),
                            DataSelector(
                                `when` = mapOf("south" to false.jsonElement),
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = -90)
                            ),
                            DataSelector(
                                `when` = mapOf("west" to false.jsonElement),
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 90, y = -90)
                            ),
                            DataSelector(
                                `when` = mapOf("east" to false.jsonElement),
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 90, y = 90)
                            ),
                            DataSelector(
                                apply = DataVariant("${ModMirageFairy2019.MODID}:${"${backgroundPrefix}FairyCrystalGlassBackground".toSnakeCase()}")
                            )
                        )
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "${prefix}FairyCrystalGlass".toSnakeCase()) {
                addOreName("blockMirageFairyCrystal$prefix")
                setCustomModelResourceLocation(model = ResourceLocation(ModMirageFairy2019.MODID, "${prefix}FairyCrystalGlass".toSnakeCase()))
                makeItemModel("${prefix}FairyCrystalGlass".toSnakeCase()) {
                    DataModel(
                        parent = "block/block",
                        elements = listOf(
                            cube("#background", null),
                            cube("#frame", 0),
                            cube("#frame", 90),
                            cube("#frame", 180),
                            cube("#frame", 270)
                        ),
                        textures = mapOf(
                            "particle" to "miragefairy2019:blocks/${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}",
                            "background" to "miragefairy2019:blocks/${"${backgroundPrefix}FairyCrystalGlassBackground".toSnakeCase()}",
                            "frame" to "miragefairy2019:blocks/${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}"
                        )
                    )
                }
            }

            onMakeLang { enJa("tile.${"${prefix}FairyCrystalGlass".toLowerCaseHead()}.name", "${englishPrefix}Fairy Crystal Glass", "${japanesePrefix}フェアリークリスタルガラス") }

            // 圧縮レシピ
            makeRecipe("${prefix}FairyCrystalGlass".toSnakeCase()) {
                DataShapelessRecipe(
                    ingredients = (1..8).map { DataOreIngredient(ore = "mirageFairyCrystal$prefix") },
                    result = DataResult(item = "${ModMirageFairy2019.MODID}:${"${prefix}FairyCrystalGlass".toSnakeCase()}")
                )
            }

            // 分解レシピ
            makeRecipe("${"${prefix}FairyCrystal".toSnakeCase()}_from_${"${prefix}FairyCrystalGlass".toSnakeCase()}") {
                DataShapelessRecipe(
                    ingredients = listOf(DataOreIngredient(ore = "blockMirageFairyCrystal$prefix")),
                    result = DataResult(item = "${ModMirageFairy2019.MODID}:fairy_crystal", data = crystalMetadata, count = 8)
                )
            }

            return Pair(block, item)
        }

        fairyCrystalGlass(0, "", "", "", "", "").let {
            blockFairyCrystalGlass = it.first
            itemBlockFairyCrystalGlass = it.second
        }
        fairyCrystalGlass(2, "Pure", "Pure", "", "Pure ", "高純度").let {
            blockPureFairyCrystalGlass = it.first
            itemBlockPureFairyCrystalGlass = it.second
        }
        fairyCrystalGlass(3, "VeryPure", "VeryPure", "", "Very Pure ", "超高純度").let {
            blockVeryPureFairyCrystalGlass = it.first
            itemBlockVeryPureFairyCrystalGlass = it.second
        }
        fairyCrystalGlass(4, "Wild", "Pure", "Wild", "Wild ", "野蛮な").let {
            blockWildFairyCrystalGlass = it.first
            itemBlockWildFairyCrystalGlass = it.second
        }
        fairyCrystalGlass(5, "VeryWild", "VeryPure", "Wild", "Very Wild ", "超野蛮な").let {
            blockVeryWildFairyCrystalGlass = it.first
            itemBlockVeryWildFairyCrystalGlass = it.second
        }

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
