package miragefairy2019.mod.material

import miragefairy2019.common.toOreName
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocations
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.resourcemaker.DataItemModel
import miragefairy2019.resourcemaker.DataOrIngredient
import miragefairy2019.resourcemaker.DataOreIngredient
import miragefairy2019.resourcemaker.DataResult
import miragefairy2019.resourcemaker.DataShapelessRecipe
import miragefairy2019.resourcemaker.DataSimpleIngredient
import miragefairy2019.resourcemaker.MakeItemModelScope
import miragefairy2019.resourcemaker.generated
import miragefairy2019.resourcemaker.handheld
import miragefairy2019.resourcemaker.makeItemModel
import miragefairy2019.resourcemaker.makeRecipe
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
                modelSupplier: MakeItemModelScope.() -> DataItemModel
            ) = itemVariant(registryName, { ItemVariantSimpleMaterials(it, unlocalizedName) }, metadata) {
                addOreName(oreName)
                makeItemModel(registryName) { modelSupplier() }
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
            makeRecipe("gold_ore_smelt_tier_2") {
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
            }

            // Tier 4 金鉱石 -> 金
            makeRecipe("gold_ore_smelt_tier_4") {
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
            }

            // Tier 2 鉄鉱石 -> 鉄
            makeRecipe("iron_ore_smelt_tier_2") {
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
            }

            // Tier 4 鉄鉱石 -> 鉄
            makeRecipe("iron_ore_smelt_tier_4") {
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
            }

            // Tier 2 磁鉄鉱の粉 -> 鉄
            makeRecipe("magnetite_smelt_tier_2") {
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
            }

            // Tier 4 磁鉄鉱の粉 -> 鉄
            makeRecipe("magnetite_smelt_tier_4") {
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
            }

        }

        // レシピの登録
        onAddRecipe a@{
            GameRegistry.addSmelting("gemPyrite".toOreName().copyItemStack() ?: return@a, "nuggetIron".toOreName().copyItemStack(3) ?: return@a, 0.7f)
        }

    }
}
