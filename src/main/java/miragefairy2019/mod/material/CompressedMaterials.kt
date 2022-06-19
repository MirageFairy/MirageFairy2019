package miragefairy2019.mod.material

import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.resourcemaker.DataIngredient
import miragefairy2019.resourcemaker.DataOreIngredient
import miragefairy2019.resourcemaker.DataResult
import miragefairy2019.resourcemaker.DataShapedRecipe
import miragefairy2019.resourcemaker.DataSimpleIngredient
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockItemModel
import miragefairy2019.libkt.makeBlockModel
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.resourcemaker.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.gson.jsonElement
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation

object CompressedMaterials {
    lateinit var blockMaterials1: () -> BlockMaterials<EnumVariantMaterials1>
    lateinit var itemBlockMaterials1: () -> ItemBlockMaterials<EnumVariantMaterials1>
    val module = module {

        // ブロック状素材
        blockMaterials1 = block({ BlockMaterials(EnumVariantMaterials1.variantList) }, "materials1") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf(
                        "miragefairy2019:apatite_block",
                        "miragefairy2019:fluorite_block",
                        "miragefairy2019:sulfur_block",
                        "miragefairy2019:cinnabar_block",
                        "miragefairy2019:moonstone_block",
                        "miragefairy2019:magnetite_block",
                        "miragefairy2019:pyrope_block",
                        "miragefairy2019:smithsonite_block",
                        "miragefairy2019:charcoal_block",
                        "miragefairy2019:mirage_flower_leaf_block",
                        "miragefairy2019:miragium_ingot_block",
                        "miragefairy2019:miragium_dust_block",
                        "miragefairy2019:nephrite_block",
                        "miragefairy2019:topaz_block",
                        "miragefairy2019:tourmaline_block",
                        "minecraft:stone"
                    ).mapIndexed { i, model -> "variant=$i" to DataBlockState(model = model) }.toMap()
                )
            }
        }
        itemBlockMaterials1 = item({ ItemBlockMaterials(blockMaterials1()) }, "materials1") {
            onRegisterItem {
                blockMaterials1().variantList.blockVariants.forEach {
                    item.setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.resourceName))
                }
            }
            onCreateItemStack {
                blockMaterials1().variantList.blockVariants.forEach {
                    item.addOreName(it.oreName, it.metadata)
                }
            }
        }

        // 翻訳の生成
        onMakeLang {
            enJa("tile.blockApatite.name", "Block of Apatite", "燐灰石ブロック")
            enJa("tile.blockFluorite.name", "Block of Fluorite", "蛍石ブロック")
            enJa("tile.blockSulfur.name", "Block of Sulfur", "硫黄ブロック")
            enJa("tile.blockCinnabar.name", "Block of Cinnabar", "辰砂ブロック")
            enJa("tile.blockMoonstone.name", "Block of Moonstone", "月長石ブロック")
            enJa("tile.blockMagnetite.name", "Block of Magnetite", "磁鉄鉱ブロック")
            enJa("tile.blockPyrope.name", "Block of Pyrope", "パイロープブロック")
            enJa("tile.blockSmithsonite.name", "Block of Smithsonite", "スミソナイトブロック")
            enJa("tile.blockCharcoal.name", "Block of Charcoal", "木炭ブロック")
            enJa("tile.blockLeafMirageFlower.name", "Block of Mirage Flower Leaf", "ミラージュフラワーの葉ブロック")
            enJa("tile.blockMiragium.name", "Block of Miragium", "ミラジウムブロック")
            enJa("tile.blockDustMiragium.name", "Block of Miragium Dust", "ミラジウムの粉ブロック")
            enJa("tile.blockNephrite.name", "Block of Nephrite", "ネフライトブロック")
            enJa("tile.blockTopaz.name", "Block of Topaz", "トパーズブロック")
            enJa("tile.blockTourmaline.name", "Block of Tourmaline", "トルマリンブロック")
        }

        // ブロックモデルの生成
        run {
            fun makeBlockModel(name: String) = makeBlockModel(ResourceName(ModMirageFairy2019.MODID, name)) {
                jsonElement(
                    "parent" to "block/cube_all".jsonElement,
                    "textures" to mirrg.kotlin.gson.jsonElement(
                        "all" to "miragefairy2019:blocks/$name".jsonElement
                    )
                )
            }
            makeBlockModel("apatite_block")
            makeBlockModel("fluorite_block")
            makeBlockModel("sulfur_block")
            makeBlockModel("cinnabar_block")
            makeBlockModel("moonstone_block")
            makeBlockModel("magnetite_block")
            makeBlockModel("pyrope_block")
            makeBlockModel("smithsonite_block")
            makeBlockModel("charcoal_block")
            makeBlockModel("mirage_flower_leaf_block")
            makeBlockModel("miragium_ingot_block")
            makeBlockModel("miragium_dust_block")
            makeBlockModel("nephrite_block")
            makeBlockModel("topaz_block")
            makeBlockModel("tourmaline_block")
        }

        // アイテムモデルの生成
        run {
            fun makeItemModel(name: String) = makeBlockItemModel(ResourceName(ModMirageFairy2019.MODID, name))
            makeItemModel("apatite_block")
            makeItemModel("fluorite_block")
            makeItemModel("sulfur_block")
            makeItemModel("cinnabar_block")
            makeItemModel("moonstone_block")
            makeItemModel("magnetite_block")
            makeItemModel("pyrope_block")
            makeItemModel("smithsonite_block")
            makeItemModel("charcoal_block")
            makeItemModel("mirage_flower_leaf_block")
            makeItemModel("miragium_ingot_block")
            makeItemModel("miragium_dust_block")
            makeItemModel("nephrite_block")
            makeItemModel("topaz_block")
            makeItemModel("tourmaline_block")
        }

        // レシピの生成
        run {
            fun toi(ingot: String, block: String, ingredientBlock: DataIngredient, resultIngot: DataResult) {
                makeRecipe(
                    ResourceName(ModMirageFairy2019.MODID, "materials/compress/${ingot}_from_${block}"), // TODO rename
                    DataShapedRecipe(
                        pattern = listOf(
                            "#"
                        ),
                        key = mapOf(
                            "#" to ingredientBlock
                        ),
                        result = resultIngot
                    )
                )
            }

            fun tob(block: String, ingredientIngot: DataIngredient, resultBlock: DataResult) {
                makeRecipe(
                    ResourceName(ModMirageFairy2019.MODID, "materials/compress/$block"),
                    DataShapedRecipe(
                        pattern = listOf(
                            "###",
                            "###",
                            "###"
                        ),
                        key = mapOf(
                            "#" to ingredientIngot
                        ),
                        result = resultBlock
                    )
                )
            }

            operator fun String.not() = DataOreIngredient(ore = this)
            operator fun String.invoke(data: Int) = DataSimpleIngredient(item = this, data = data)
            fun r(item: String, data: Int, count: Int) = DataResult(item = item, data = data, count = count)
            val m = "miragefairy2019:materials"
            val fm = "miragefairy2019:fairy_materials"
            val m1 = "miragefairy2019:materials1"
            toi("apatite_gem", "apatite_block", !"blockApatite", r(m, 0, 9))
            toi("fluorite_gem", "fluorite_block", !"blockFluorite", r(m, 1, 9))
            toi("sulfur_gem", "sulfur_block", !"blockSulfur", r(m, 2, 9))
            toi("cinnabar_gem", "cinnabar_block", !"blockCinnabar", r(m, 6, 9))
            toi("moonstone_gem", "moonstone_block", !"blockMoonstone", r(m, 7, 9))
            toi("magnetite_gem", "magnetite_block", !"blockMagnetite", r(m, 8, 9))
            toi("pyrope_gem", "pyrope_block", !"blockPyrope", r(m, 10, 9))
            toi("smithsonite_gem", "smithsonite_block", !"blockSmithsonite", r(m, 11, 9))
            toi("charcoal", "charcoal_block", !"blockCharcoal", r("minecraft:coal", 1, 9))
            toi("mirage_flower_leaf", "mirage_flower_leaf_block", !"blockLeafMirageFlower", r(fm, 8, 9))
            toi("miragium_ingot", "miragium_ingot_block", !"blockMiragium", r(m, 5, 9))
            toi("miragium_dust", "miragium_dust_block", !"blockDustMiragium", r(m, 3, 9))
            toi("nephrite_gem", "nephrite_block", !"blockNephrite", r(m, 14, 9))
            toi("topaz_gem", "topaz_block", !"blockTopaz", r(m, 15, 9))
            toi("tourmaline_gem", "tourmaline_block", !"blockTourmaline", r(m, 16, 9))
            tob("apatite_block", !"gemApatite", r(m1, 0, 1))
            tob("fluorite_block", !"gemFluorite", r(m1, 1, 1))
            tob("sulfur_block", !"gemSulfur", r(m1, 2, 1))
            tob("cinnabar_block", !"gemCinnabar", r(m1, 3, 1))
            tob("moonstone_block", !"gemMoonstone", r(m1, 4, 1))
            tob("magnetite_block", !"gemMagnetite", r(m1, 5, 1))
            tob("pyrope_block", !"gemPyrope", r(m1, 6, 1))
            tob("smithsonite_block", !"gemSmithsonite", r(m1, 7, 1))
            tob("charcoal_block", "minecraft:coal"(1), r(m1, 8, 1))
            tob("mirage_flower_leaf_block", !"leafMirageFlower", r(m1, 9, 1))
            tob("miragium_ingot_block", !"ingotMiragium", r(m1, 10, 1))
            tob("miragium_dust_block", !"dustMiragium", r(m1, 11, 1))
            tob("nephrite_block", !"gemNephrite", r(m1, 12, 1))
            tob("topaz_block", !"gemTopaz", r(m1, 13, 1))
            tob("tourmaline_block", !"gemTourmaline", r(m1, 14, 1))
        }

    }
}

class HardnessClass(val blockHardness: Float, val harvestTool: String, val harvestLevel: Int) {
    companion object {
        val SOFT = HardnessClass(3.0f, "pickaxe", 0) // 硬度3程度、カルサイト級
        val HARD = HardnessClass(5.0f, "pickaxe", 0) // 硬度5程度、石級
        val VERY_HARD = HardnessClass(5.0f, "pickaxe", 1) // 硬度7程度、クリスタル級
        val SUPER_HARD = HardnessClass(5.0f, "pickaxe", 2) // 硬度9程度、ダイヤモンド級
    }
}

enum class EnumVariantMaterials1(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    val oreName: String,
    val hardnessClass: HardnessClass,
    override val burnTime: Int,
    override val soundType: SoundType,
    override val isFallable: Boolean,
    override val material: Material,
    override val isBeaconBase: Boolean
) : IStringSerializable, IBlockVariantMaterials {
    APATITE_BLOCK(0, "apatite_block", "blockApatite", "blockApatite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", "blockFluorite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", "blockSulfur", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", "blockCinnabar", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", "blockMoonstone", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", "blockMagnetite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    PYROPE_BLOCK(6, "pyrope_block", "blockPyrope", "blockPyrope", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    SMITHSONITE_BLOCK(7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", HardnessClass.SOFT, 0, SoundType.STONE, false, Material.IRON, true),
    CHARCOAL_BLOCK(8, "charcoal_block", "blockCharcoal", "blockCharcoal", HardnessClass(5.0f, "pickaxe", 0), 20 * 10 * 8 * 9, SoundType.STONE, false, Material.ROCK, false),
    MIRAGE_FLOWER_LEAF_BLOCK(9, "mirage_flower_leaf_block", "blockLeafMirageFlower", "blockLeafMirageFlower", HardnessClass(2.0f, "axe", 0), 0, SoundType.GLASS, false, Material.LEAVES, false),
    MIRAGIUM_INGOT_BLOCK(10, "miragium_ingot_block", "blockMiragium", "blockMiragium", HardnessClass(5.0f, "pickaxe", 1), 0, SoundType.METAL, false, Material.IRON, false),
    MIRAGIUM_DUST_BLOCK(11, "miragium_dust_block", "blockDustMiragium", "blockDustMiragium", HardnessClass(0.5f, "shovel", 0), 0, SoundType.SNOW, true, Material.SAND, false),
    NEPHRITE_BLOCK(12, "nephrite_block", "blockNephrite", "blockNephrite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    TOPAZ_BLOCK(13, "topaz_block", "blockTopaz", "blockTopaz", HardnessClass.SUPER_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    TOURMALINE_BLOCK(14, "tourmaline_block", "blockTourmaline", "blockTourmaline", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName
    override val blockHardness = hardnessClass.blockHardness
    override val harvestTool = hardnessClass.harvestTool
    override val harvestLevel = hardnessClass.harvestLevel

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}
