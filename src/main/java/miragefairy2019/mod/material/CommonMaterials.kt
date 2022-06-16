package miragefairy2019.mod.material

import miragefairy2019.common.toOreName
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.DataIngredient
import miragefairy2019.libkt.DataItemModel
import miragefairy2019.libkt.DataOrIngredient
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.MakeItemVariantModelScope
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.generated
import miragefairy2019.libkt.handheld
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeBlockItemModel
import miragefairy2019.libkt.makeBlockModel
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.makeItemVariantModel
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setCustomModelResourceLocations
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.WandType
import mirrg.kotlin.gson.jsonElement
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

object CommonMaterials {
    lateinit var itemMaterials: () -> ItemSimpleMaterials
    val module = module {

        // アイテム状素材
        itemMaterials = item({ ItemSimpleMaterials() }, "materials") {
            setUnlocalizedName("materials")
            setCreativeTab { Main.creativeTab }

            fun r(
                metadata: Int,
                registryName: String,
                unlocalizedName: String,
                oreName: String,
                modelSupplier: MakeItemVariantModelScope<ItemSimpleMaterials, ItemVariantSimpleMaterials>.() -> DataItemModel
            ) = itemVariant(registryName, { ItemVariantSimpleMaterials(it, unlocalizedName) }, metadata) {
                addOreName(oreName)
                makeItemVariantModel { modelSupplier() }
            }

            fun ItemVariantInitializer<ItemSimpleMaterials, ItemVariantSimpleMaterials>.fuel(burnTime: Int) = also { itemInitializer.modInitializer.onRegisterItem { itemVariant.burnTime = burnTime } }

            r(0, "apatite_gem", "gemApatite", "gemApatite", { generated })
            r(1, "fluorite_gem", "gemFluorite", "gemFluorite", { generated })
            r(2, "sulfur_gem", "gemSulfur", "gemSulfur", { generated })
            r(3, "miragium_dust", "dustMiragium", "dustMiragium", { generated })
            r(4, "miragium_tiny_dust", "dustTinyMiragium", "dustTinyMiragium", { generated })
            r(5, "miragium_ingot", "ingotMiragium", "ingotMiragium", { generated })
            r(6, "cinnabar_gem", "gemCinnabar", "gemCinnabar", { generated })
            r(7, "moonstone_gem", "gemMoonstone", "gemMoonstone", { generated })
            r(8, "magnetite_gem", "gemMagnetite", "gemMagnetite", { generated })
            r(9, "saltpeter_gem", "gemSaltpeter", "gemSaltpeter", { generated })
            r(10, "pyrope_gem", "gemPyrope", "gemPyrope", { generated })
            r(11, "smithsonite_gem", "gemSmithsonite", "gemSmithsonite", { generated })
            r(12, "miragium_rod", "rodMiragium", "rodMiragium", { handheld })
            r(13, "miragium_nugget", "nuggetMiragium", "nuggetMiragium", { generated })
            r(14, "nephrite_gem", "gemNephrite", "gemNephrite", { generated })
            r(15, "topaz_gem", "gemTopaz", "gemTopaz", { generated })
            r(16, "tourmaline_gem", "gemTourmaline", "gemTourmaline", { generated })
            r(17, "heliolite_gem", "gemHeliolite", "gemHeliolite", { generated })
            r(18, "labradorite_gem", "gemLabradorite", "gemLabradorite", { generated })
            r(19, "lilagium_ingot", "ingotLilagium", "ingotLilagium", { generated })
            r(20, "miragium_plate", "plateMiragium", "plateMiragium", { generated })
            r(21, "coal_dust", "dustCoal", "dustCoal", { generated }).fuel(1600)
            r(22, "charcoal_dust", "dustCharcoal", "dustCharcoal", { generated }).fuel(1600)
            r(23, "apatite_dust", "dustApatite", "dustApatite", { generated })
            r(24, "fluorite_dust", "dustFluorite", "dustFluorite", { generated })
            r(25, "sulfur_dust", "dustSulfur", "dustSulfur", { generated })
            r(26, "cinnabar_dust", "dustCinnabar", "dustCinnabar", { generated })
            r(27, "moonstone_dust", "dustMoonstone", "dustMoonstone", { generated })
            r(28, "magnetite_dust", "dustMagnetite", "dustMagnetite", { generated })
            r(29, "pyrite_gem", "gemPyrite", "gemPyrite", { generated })

            onRegisterItem {
                if (Main.side.isClient) item.setCustomModelResourceLocations()
            }
        }

        // langの生成
        onMakeLang {
            enJa("item.gemApatite.name", "Apatite", "燐灰石")
            enJa("item.gemFluorite.name", "Fluorite", "蛍石")
            enJa("item.gemSulfur.name", "Sulfur", "硫黄")
            enJa("item.dustMiragium.name", "Miragium Dust", "ミラジウムの粉")
            enJa("item.dustTinyMiragium.name", "Tiny Pile of Miragium Dust", "ミラジウムの微粉")
            enJa("item.ingotMiragium.name", "Miragium Ingot", "ミラジウムインゴット")
            enJa("item.gemCinnabar.name", "Cinnabar", "辰砂")
            enJa("item.gemMoonstone.name", "Moonstone", "月長石")
            enJa("item.gemMagnetite.name", "Magnetite", "磁鉄鉱")
            enJa("item.gemSaltpeter.name", "Saltpeter", "硝石")
            enJa("item.gemPyrope.name", "Pyrope", "パイロープ")
            enJa("item.gemSmithsonite.name", "Smithsonite", "スミソナイト")
            enJa("item.rodMiragium.name", "Miragium Rod", "ミラジウムの棒")
            enJa("item.nuggetMiragium.name", "Miragium Nugget", "ミラジウムの塊")
            enJa("item.gemNephrite.name", "Nephrite", "ネフライト")
            enJa("item.gemTopaz.name", "Topaz", "トパーズ")
            enJa("item.gemTourmaline.name", "Tourmaline", "トルマリン")
            enJa("item.gemHeliolite.name", "Heliolite", "ヘリオライト")
            enJa("item.gemLabradorite.name", "Labradorite", "ラブラドライト")
            enJa("item.ingotLilagium.name", "Lilagium Ingot", "リラジウムインゴット")
            enJa("item.plateMiragium.name", "Miragium Plate", "ミラジウムの板")
            enJa("item.dustCoal.name", "Coal Dust", "石炭の粉")
            enJa("item.dustCharcoal.name", "Charcoal Dust", "木炭の粉")
            enJa("item.dustApatite.name", "Apatite Dust", "燐灰石の粉")
            enJa("item.dustFluorite.name", "Fluorite Dust", "蛍石の粉")
            enJa("item.dustSulfur.name", "Sulfur Dust", "硫黄の粉")
            enJa("item.dustCinnabar.name", "Cinnabar Dust", "辰砂の粉")
            enJa("item.dustMoonstone.name", "Moonstone Dust", "月長石の粉")
            enJa("item.dustMagnetite.name", "Magnetite Dust", "磁鉄鉱の粉")
            enJa("item.gemPyrite.name", "Pyrite", "パイライト")
        }

        // レシピの生成
        run {

            // Tier 2 金鉱石 -> 金
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "gold_ore_smelt_tier_2"),
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.MELTING.ingredientData,
                        WandType.BREAKING.ingredientData,
                        DataSimpleIngredient(item = "minecraft:gold_ore")
                    ),
                    result = DataResult(
                        item = "minecraft:gold_nugget",
                        count = 17
                    )
                )
            )

            // Tier 4 金鉱石 -> 金
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "gold_ore_smelt_tier_4"),
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.MELTING.ingredientData,
                        WandType.FUSION.ingredientData,
                        DataOreIngredient(ore = "dustMiragium"),
                        DataSimpleIngredient(item = "minecraft:gold_ore")
                    ),
                    result = DataResult(
                        item = "minecraft:gold_ingot",
                        count = 3
                    )
                )
            )

            // Tier 2 鉄鉱石 -> 鉄
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "iron_ore_smelt_tier_2"),
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.MELTING.ingredientData,
                        WandType.BREAKING.ingredientData,
                        DataSimpleIngredient(item = "minecraft:iron_ore")
                    ),
                    result = DataResult(
                        item = "minecraft:iron_nugget",
                        count = 17
                    )
                )
            )

            // Tier 4 鉄鉱石 -> 鉄
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "iron_ore_smelt_tier_4"),
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.MELTING.ingredientData,
                        WandType.FUSION.ingredientData,
                        DataOreIngredient(ore = "dustMiragium"),
                        DataSimpleIngredient(item = "minecraft:iron_ore")
                    ),
                    result = DataResult(
                        item = "minecraft:iron_ingot",
                        count = 3
                    )
                )
            )

            // Tier 2 磁鉄鉱の粉 -> 鉄
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "magnetite_smelt_tier_2"),
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.MELTING.ingredientData,
                        DataOrIngredient(
                            DataOreIngredient(ore = "dustCoal"),
                            DataOreIngredient(ore = "dustCharcoal")
                        ),
                        DataOreIngredient(ore = "dustMagnetite")
                    ),
                    result = DataResult(
                        item = "minecraft:iron_nugget",
                        count = 3
                    )
                )
            )

            // Tier 4 磁鉄鉱の粉 -> 鉄
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "magnetite_smelt_tier_4"),
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.MELTING.ingredientData,
                        WandType.DISTORTION.ingredientData,
                        DataOreIngredient(ore = "dustMagnetite"),
                        DataOreIngredient(ore = "dustMagnetite"),
                        DataOreIngredient(ore = "dustMagnetite"),
                        DataOreIngredient(ore = "dustMagnetite"),
                        DataOreIngredient(ore = "dustMagnetite"),
                        DataOreIngredient(ore = "dustMagnetite"),
                        DataOreIngredient(ore = "dustMagnetite")
                    ),
                    result = DataResult(
                        item = "minecraft:iron_ingot",
                        count = 3
                    )
                )
            )

        }

        // レシピの登録
        onAddRecipe a@{
            GameRegistry.addSmelting("gemPyrite".toOreName().copyItemStack() ?: return@a, "nuggetIron".toOreName().copyItemStack(3) ?: return@a, 0.7f)
        }

    }
}


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
