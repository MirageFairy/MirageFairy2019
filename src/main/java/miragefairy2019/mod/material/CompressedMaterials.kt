package miragefairy2019.mod.material

import miragefairy2019.lib.EnumFireSpreadSpeed
import miragefairy2019.lib.EnumFlammability
import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.resourcemaker.DataIngredient
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation

object CompressedMaterials {
    lateinit var blockMaterials1: () -> BlockMaterials<CompressedMaterialCard>
    lateinit var itemBlockMaterials1: () -> ItemBlockMaterials<CompressedMaterialCard>
    lateinit var blockMaterials2: () -> BlockMaterials<CompressedMaterialCard>
    lateinit var itemBlockMaterials2: () -> ItemBlockMaterials<CompressedMaterialCard>
    val compressedMaterialsModule = module {

        // ブロックごと
        run {

            // ブロック状素材1
            blockMaterials1 = block({ BlockMaterials(BlockVariantList(CompressedMaterialCard.values().filter { it.group == 1 }.toList())) }, "materials1") {
                setCreativeTab { Main.creativeTab }
                makeBlockStates(resourceName.path) {
                    DataModelBlockDefinition(
                        variants = (0..15).associate { meta ->
                            val modelName = CompressedMaterialCard.values().filter { it.group == 1 }.getOrNull(meta)?.let { "miragefairy2019:${it.resourceName}" } ?: "minecraft:stone"
                            "variant=$meta" to DataSingleVariantList(DataVariant(model = modelName))
                        }
                    )
                }
            }
            itemBlockMaterials1 = item({ ItemBlockMaterials(blockMaterials1()) }, "materials1") {
                CompressedMaterialCard.values().filter { it.group == 1 }.forEach {
                    setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.resourceName))
                    addOreName(it.oreName, it.metadata)
                }
            }

            // ブロック状素材2
            blockMaterials2 = block({ BlockMaterials(BlockVariantList(CompressedMaterialCard.values().filter { it.group == 2 }.toList())) }, "materials2") {
                setCreativeTab { Main.creativeTab }
                makeBlockStates(resourceName.path) {
                    DataModelBlockDefinition(
                        variants = (0..15).associate { meta ->
                            val modelName = CompressedMaterialCard.values().filter { it.group == 2 }.getOrNull(meta)?.let { "miragefairy2019:${it.resourceName}" } ?: "minecraft:stone"
                            "variant=$meta" to DataSingleVariantList(DataVariant(model = modelName))
                        }
                    )
                }
            }
            itemBlockMaterials2 = item({ ItemBlockMaterials(blockMaterials2()) }, "materials2") {
                CompressedMaterialCard.values().filter { it.group == 2 }.forEach {
                    setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.resourceName))
                    addOreName(it.oreName, it.metadata)
                }
            }

        }

        // カードごと
        CompressedMaterialCard.values().forEach { compressedMaterial ->

            // 翻訳生成
            onMakeLang { enJa("tile.${compressedMaterial.unlocalizedName}.name", compressedMaterial.englishName, compressedMaterial.japaneseName) }

            // ブロックモデル生成
            makeBlockModel(compressedMaterial.resourceName) {
                DataModel(
                    parent = "block/cube_all",
                    textures = mapOf(
                        "all" to "miragefairy2019:blocks/${compressedMaterial.resourceName}"
                    )
                )
            }

            // アイテムモデル生成
            makeItemModel(compressedMaterial.resourceName) { block }

        }

        // 個別
        run {
            fun decompress(ingot: String, block: String, ingredientBlock: DataIngredient, resultIngot: DataResult) {
                makeRecipe("materials/compress/${block}_to_${ingot}") {
                    DataShapedRecipe(
                        pattern = listOf(
                            "#"
                        ),
                        key = mapOf(
                            "#" to ingredientBlock
                        ),
                        result = resultIngot
                    )
                }
            }

            fun compress(block: String, ingredientIngot: DataIngredient, resultBlock: DataResult) {
                makeRecipe("materials/compress/$block") {
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
                }
            }

            fun compress8(block: String, ingredientIngot: DataIngredient, resultBlock: DataResult) {
                makeRecipe("materials/compress/$block") {
                    DataShapelessRecipe(
                        ingredients = (1..8).map { ingredientIngot },
                        result = resultBlock
                    )
                }
            }

            operator fun String.not() = DataOreIngredient(ore = this)
            operator fun String.invoke(data: Int) = DataSimpleIngredient(item = this, data = data)
            fun r(item: String, data: Int, count: Int) = DataResult(item = item, data = data, count = count)
            val m = "miragefairy2019:materials"
            val fm = "miragefairy2019:fairy_materials"
            val m1 = "miragefairy2019:materials1"
            decompress("apatite_gem", "apatite_block", !"blockApatite", r(m, 0, 9))
            decompress("fluorite_gem", "fluorite_block", !"blockFluorite", r(m, 1, 9))
            decompress("sulfur_gem", "sulfur_block", !"blockSulfur", r(m, 2, 9))
            decompress("cinnabar_gem", "cinnabar_block", !"blockCinnabar", r(m, 6, 9))
            decompress("moonstone_gem", "moonstone_block", !"blockMoonstone", r(m, 7, 9))
            decompress("magnetite_gem", "magnetite_block", !"blockMagnetite", r(m, 8, 9))
            decompress("pyrope_gem", "pyrope_block", !"blockPyrope", r(m, 10, 9))
            decompress("smithsonite_gem", "smithsonite_block", !"blockSmithsonite", r(m, 11, 9))
            decompress("charcoal", "charcoal_block", !"blockCharcoal", r("minecraft:coal", 1, 9))
            decompress("mirage_flower_leaf", "mirage_flower_leaf_block", !"blockLeafMirageFlower", r(fm, 8, 9))
            decompress("miragium_ingot", "miragium_ingot_block", !"blockMiragium", r(m, 5, 9))
            decompress("miragium_dust", "miragium_dust_block", !"blockDustMiragium", r(m, 3, 9))
            decompress("nephrite_gem", "nephrite_block", !"blockNephrite", r(m, 14, 9))
            decompress("topaz_gem", "topaz_block", !"blockTopaz", r(m, 15, 9))
            decompress("tourmaline_gem", "tourmaline_block", !"blockTourmaline", r(m, 16, 9))
            decompress("velopeda_leaf", "velopeda_block", !"blockLeafMirageFairyVelopeda", r(fm, 26, 9))
            compress("apatite_block", !"gemApatite", r(m1, 0, 1))
            compress("fluorite_block", !"gemFluorite", r(m1, 1, 1))
            compress("sulfur_block", !"gemSulfur", r(m1, 2, 1))
            compress("cinnabar_block", !"gemCinnabar", r(m1, 3, 1))
            compress("moonstone_block", !"gemMoonstone", r(m1, 4, 1))
            compress("magnetite_block", !"gemMagnetite", r(m1, 5, 1))
            compress("pyrope_block", !"gemPyrope", r(m1, 6, 1))
            compress("smithsonite_block", !"gemSmithsonite", r(m1, 7, 1))
            compress("charcoal_block", "minecraft:coal"(1), r(m1, 8, 1))
            compress("mirage_flower_leaf_block", !"leafMirageFlower", r(m1, 9, 1))
            compress("miragium_ingot_block", !"ingotMiragium", r(m1, 10, 1))
            compress("miragium_dust_block", !"dustMiragium", r(m1, 11, 1))
            compress("nephrite_block", !"gemNephrite", r(m1, 12, 1))
            compress("topaz_block", !"gemTopaz", r(m1, 13, 1))
            compress("tourmaline_block", !"gemTourmaline", r(m1, 14, 1))
            compress("velopeda_block", !"leafMirageFairyVelopeda", r(m1, 15, 1))

            // TODO move
            decompress("miragium_tiny_dust", "miragium_dust", !"dustMiragium", r(m, 4, 9))
            decompress("miragium_nugget", "miragium_ingot", !"ingotMiragium", r(m, 13, 9))
            decompress("mirage_fairy_solid_fuel_half_chunk", "mirage_fairy_solid_fuel_ingot", !"ingotMirageFairySolidFuel", r(fm, 30, 8))
            compress("miragium_dust", !"dustTinyMiragium", r(m, 3, 1))
            compress("miragium_ingot", !"nuggetMiragium", r(m, 5, 1))
            compress8("mirage_fairy_solid_fuel_ingot", !"halfChunkMirageFairySolidFuel", r(fm, 29, 1))

            // TODO move
            makeRecipe("reinforced_stone") {
                DataShapedRecipe(
                    pattern = listOf(
                        "sms",
                        "mIm",
                        "sms"
                    ),
                    key = mapOf(
                        "I" to DataOreIngredient(ore = "ingotMiragium"),
                        "m" to DataOreIngredient(ore = "gemMagnetite"),
                        "s" to DataOreIngredient(ore = "stone")
                    ),
                    result = DataResult(item = "miragefairy2019:materials2", data = CompressedMaterialCard.REINFORCED_STONE.metadata, count = 4)
                )
            }
            makeRecipe("reinforced_plastic") {
                DataShapedRecipe(
                    pattern = listOf(
                        "AB",
                        "BA"
                    ),
                    key = mapOf(
                        "A" to DataOreIngredient(ore = "mirageFairyReinforcedStone"),
                        "B" to DataOreIngredient(ore = "gemMirageFairyPlastic")
                    ),
                    result = DataResult(item = "miragefairy2019:materials2", data = CompressedMaterialCard.REINFORCED_PLASTIC.metadata, count = 2)
                )
            }
            makeRecipe("drywall") {
                DataShapedRecipe(
                    pattern = listOf(
                        "sbs",
                        "bmb",
                        "sbs"
                    ),
                    key = mapOf(
                        "m" to DataOreIngredient(ore = "dustMiragium"),
                        "b" to DataSimpleIngredient(item = "minecraft:dye", data = 15),
                        "s" to DataOreIngredient(ore = "stone")
                    ),
                    result = DataResult(item = "miragefairy2019:materials2", data = CompressedMaterialCard.DRYWALL.metadata, count = 4)
                )
            }

        }

    }
}

class HardnessClass(val blockHardness: Float, val harvestTool: String, val harvestLevel: Int) {
    companion object {
        val SOFT = HardnessClass(3.0f, "pickaxe", 0) // 硬度3程度、カルサイト級
        val HARD = HardnessClass(5.0f, "pickaxe", 0) // 硬度5程度、石級
        val VERY_HARD = HardnessClass(5.0f, "pickaxe", 1) // 硬度7程度、クリスタル級
        val SUPER_HARD = HardnessClass(5.0f, "pickaxe", 2) // 硬度9程度、ダイヤモンド級
        val OBSIDIAN = HardnessClass(50.0f, "pickaxe", 2) // 黒曜石と同じ
        val REINFORCED = HardnessClass(80.0f, "pickaxe", 2) // IC2の強化石材と同じ
    }
}

enum class CompressedMaterialCard(
    val group: Int,
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    val oreName: String,
    val englishName: String,
    val japaneseName: String,
    hardnessClass: HardnessClass,
    override val burnTime: Int,
    private val soundTypeSupplier: () -> SoundType,
    override val isFallable: Boolean,
    private val materialSupplier: () -> Material,
    override val isBeaconBase: Boolean,
    override val flammability: Int,
    override val fireSpreadSpeed: Int,
    override val enchantPowerBonus: Float
) : IStringSerializable, IBlockVariantMaterials {
    APATITE_BLOCK(1, 0, "apatite_block", "blockApatite", "blockApatite", "Block of Apatite", "燐灰石ブロック", HardnessClass.HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    FLUORITE_BLOCK(1, 1, "fluorite_block", "blockFluorite", "blockFluorite", "Block of Fluorite", "蛍石ブロック", HardnessClass.HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    SULFUR_BLOCK(1, 2, "sulfur_block", "blockSulfur", "blockSulfur", "Block of Sulfur", "硫黄ブロック", HardnessClass.HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    CINNABAR_BLOCK(1, 3, "cinnabar_block", "blockCinnabar", "blockCinnabar", "Block of Cinnabar", "辰砂ブロック", HardnessClass.HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    MOONSTONE_BLOCK(1, 4, "moonstone_block", "blockMoonstone", "blockMoonstone", "Block of Moonstone", "月長石ブロック", HardnessClass.VERY_HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    MAGNETITE_BLOCK(1, 5, "magnetite_block", "blockMagnetite", "blockMagnetite", "Block of Magnetite", "磁鉄鉱ブロック", HardnessClass.HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    PYROPE_BLOCK(1, 6, "pyrope_block", "blockPyrope", "blockPyrope", "Block of Pyrope", "パイロープブロック", HardnessClass.VERY_HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    SMITHSONITE_BLOCK(1, 7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", "Block of Smithsonite", "スミソナイトブロック", HardnessClass.SOFT, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    CHARCOAL_BLOCK(1, 8, "charcoal_block", "blockCharcoal", "blockCharcoal", "Block of Charcoal", "木炭ブロック", HardnessClass(5.0f, "pickaxe", 0), 20 * 10 * 8 * 9, { SoundType.STONE }, false, { Material.ROCK }, false, EnumFlammability.VERY_SLOW.value, EnumFireSpreadSpeed.VERY_SLOW.value, 0.0f),
    MIRAGE_FLOWER_LEAF_BLOCK(1, 9, "mirage_flower_leaf_block", "blockLeafMirageFlower", "blockLeafMirageFlower", "Block of Mirage Flower Leaf", "ミラージュフラワーの葉ブロック", HardnessClass(2.0f, "axe", 0), 0, { SoundType.GLASS }, false, { Material.LEAVES }, false, EnumFlammability.SLIGHTLY_SLOW.value, EnumFireSpreadSpeed.FAST.value, 0.0f),
    MIRAGIUM_INGOT_BLOCK(1, 10, "miragium_ingot_block", "blockMiragium", "blockMiragium", "Block of Miragium", "ミラジウムブロック", HardnessClass(5.0f, "pickaxe", 1), 0, { SoundType.METAL }, false, { Material.IRON }, false, 0, 0, 0.0f),
    MIRAGIUM_DUST_BLOCK(1, 11, "miragium_dust_block", "blockDustMiragium", "blockDustMiragium", "Block of Miragium Dust", "ミラジウムの粉ブロック", HardnessClass(0.5f, "shovel", 0), 0, { SoundType.SNOW }, true, { Material.SAND }, false, 0, 0, 0.0f),
    NEPHRITE_BLOCK(1, 12, "nephrite_block", "blockNephrite", "blockNephrite", "Block of Nephrite", "ネフライトブロック", HardnessClass.HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    TOPAZ_BLOCK(1, 13, "topaz_block", "blockTopaz", "blockTopaz", "Block of Topaz", "トパーズブロック", HardnessClass.SUPER_HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    TOURMALINE_BLOCK(1, 14, "tourmaline_block", "blockTourmaline", "blockTourmaline", "Block of Tourmaline", "トルマリンブロック", HardnessClass.VERY_HARD, 0, { SoundType.STONE }, false, { Material.IRON }, true, 0, 0, 0.0f),
    VELOPEDA_BLOCK(1, 15, "velopeda_block", "blockVelopeda", "blockLeafMirageFairyVelopeda", "Block of Velopeda Leaf", "呪いの布", HardnessClass(0.8f, "shovel", 0), 0, { SoundType.CLOTH }, false, { Material.CLOTH }, false, EnumFlammability.FAST.value, EnumFireSpreadSpeed.MEDIUM.value, 1.0f),

    REINFORCED_STONE(2, 0, "reinforced_stone", "reinforcedStone", "mirageFairyReinforcedStone", "Reinforced Stone", "強化石材", HardnessClass.OBSIDIAN, 0, { SoundType.STONE }, false, { Material.ROCK }, false, 0, 0, 0.0f),
    REINFORCED_PLASTIC(2, 1, "reinforced_plastic", "reinforcedPlastic", "mirageFairyReinforcedPlastic", "Reinforced Plastic", "強化プラスチック", HardnessClass.REINFORCED, 0, { SoundType.STONE }, false, { Material.ROCK }, false, 0, 0, 0.0f),
    DRYWALL(2, 2, "drywall", "drywall", "mirageFairyDrywall", "Drywall", "石膏ボード", HardnessClass.SOFT, 0, { SoundType.WOOD }, false, { Material.ROCK }, false, 0, 0, 0.0f),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName
    override val blockHardness = hardnessClass.blockHardness
    override val harvestTool = hardnessClass.harvestTool
    override val harvestLevel = hardnessClass.harvestLevel
    override val soundType: SoundType get() = soundTypeSupplier()
    override val material: Material get() = materialSupplier()
}
