package miragefairy2019.mod.artifacts

import miragefairy2019.libkt.BlockInitializer
import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.DataItemModel
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataPart
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.ItemInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.makeItemModel
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.with
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.Main
import mirrg.kotlin.gson.jsonElement
import mirrg.kotlin.gson.jsonElementNotNull
import mirrg.kotlin.toLowerCaseHead
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
    val module = module {
        fun surface(texture: String, cullface: String, rotation: Int?) = jsonElementNotNull(
            "texture" to texture.jsonElement,
            "cullface" to cullface.jsonElement,
            rotation?.let { "rotation" to it.jsonElement }
        )

        fun cube(texture: String, rotation: Int?) = jsonElement(
            "from" to jsonElement(0.jsonElement, 0.jsonElement, 0.jsonElement),
            "to" to jsonElement(16.jsonElement, 16.jsonElement, 16.jsonElement),
            "faces" to jsonElement(
                "down" to surface(texture, "down", rotation),
                "up" to surface(texture, "up", rotation),
                "north" to surface(texture, "north", rotation),
                "south" to surface(texture, "south", rotation),
                "west" to surface(texture, "west", rotation),
                "east" to surface(texture, "east", rotation)
            )
        )


        fun makeBackgroundModel(name: String) = onMakeResource {
            dirBase.resolve("assets/${ModMirageFairy2019.MODID}/models/block/$name.json").place(
                jsonElement(
                    "parent" to "block/block".jsonElement,
                    "elements" to jsonElement(
                        cube("#background", null)
                    ),
                    "textures" to jsonElement(
                        "background" to "miragefairy2019:blocks/$name".jsonElement
                    )
                )
            )
        }
        makeBackgroundModel("fairy_crystal_glass_background")
        makeBackgroundModel("pure_fairy_crystal_glass_background")
        makeBackgroundModel("very_pure_fairy_crystal_glass_background")


        fun makeFrameModel(name: String) = onMakeResource {
            dirBase.resolve("assets/${ModMirageFairy2019.MODID}/models/block/$name.json").place(
                jsonElement(
                    "elements" to jsonElement(
                        jsonElement(
                            "from" to jsonElement(0.jsonElement, 0.jsonElement, 0.jsonElement),
                            "to" to jsonElement(16.jsonElement, 16.jsonElement, 16.jsonElement),
                            "faces" to jsonElement(
                                "north" to surface("#frame", "north", null),
                                "south" to surface("#frame", "south", null),
                                "west" to surface("#frame", "west", null),
                                "east" to surface("#frame", "east", null)
                            )
                        )
                    ),
                    "textures" to jsonElement(
                        "particle" to "miragefairy2019:blocks/$name".jsonElement,
                        "frame" to "miragefairy2019:blocks/$name".jsonElement
                    )
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
        ): Pair<BlockInitializer<BlockFairyCrystalGlass>, ItemInitializer<ItemBlock>> {
            val block = block({ BlockFairyCrystalGlass() }, "${prefix}FairyCrystalGlass".toSnakeCase()) {
                setUnlocalizedName("${prefix}FairyCrystalGlass".toLowerCaseHead())
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        multipart = listOf(
                            DataPart(
                                `when` = mapOf("down" to false),
                                apply = DataBlockState("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 180)
                            ),
                            DataPart(
                                `when` = mapOf("up" to false),
                                apply = DataBlockState("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}")
                            ),
                            DataPart(
                                `when` = mapOf("north" to false),
                                apply = DataBlockState("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 90)
                            ),
                            DataPart(
                                `when` = mapOf("south" to false),
                                apply = DataBlockState("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = -90)
                            ),
                            DataPart(
                                `when` = mapOf("west" to false),
                                apply = DataBlockState("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 90, y = -90)
                            ),
                            DataPart(
                                `when` = mapOf("east" to false),
                                apply = DataBlockState("${ModMirageFairy2019.MODID}:${"${framePrefix}FairyCrystalGlassFrame".toSnakeCase()}", x = 90, y = 90)
                            ),
                            DataPart(
                                apply = DataBlockState("${ModMirageFairy2019.MODID}:${"${backgroundPrefix}FairyCrystalGlassBackground".toSnakeCase()}")
                            )
                        )
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "${prefix}FairyCrystalGlass".toSnakeCase()) {
                addOreName("blockMirageFairyCrystal$prefix")
                setCustomModelResourceLocation(model = ResourceLocation(ModMirageFairy2019.MODID, "${prefix}FairyCrystalGlass".toSnakeCase()))
                makeItemModel {
                    DataItemModel(
                        parent = "block/block",
                        elements = jsonElement(
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
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "${prefix}FairyCrystalGlass".toSnakeCase()),
                DataShapelessRecipe(
                    ingredients = (1..8).map { DataOreIngredient(ore = "mirageFairyCrystal$prefix") },
                    result = DataResult(
                        item = "${ModMirageFairy2019.MODID}:${"${prefix}FairyCrystalGlass".toSnakeCase()}"
                    )
                )
            )

            // 分解レシピ
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "${"${prefix}FairyCrystal".toSnakeCase()}_from_${"${prefix}FairyCrystalGlass".toSnakeCase()}"),
                DataShapelessRecipe(
                    ingredients = listOf(DataOreIngredient(ore = "blockMirageFairyCrystal$prefix")),
                    result = DataResult(
                        item = "${ModMirageFairy2019.MODID}:fairy_crystal",
                        data = crystalMetadata,
                        count = 8
                    )
                )
            )

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