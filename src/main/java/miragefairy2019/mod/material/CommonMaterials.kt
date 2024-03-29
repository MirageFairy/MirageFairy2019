package miragefairy2019.mod.material

import miragefairy2019.common.toOreName
import miragefairy2019.lib.modinitializer.ItemVariantScope
import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.itemVariant
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOrIngredient
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.setCustomModelResourceLocations
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData
import net.minecraftforge.fml.common.registry.GameRegistry

enum class CommonMaterialCard(
    val metadata: Int,
    val registryName: String,
    val unlocalizedName: String,
    val englishName: String,
    val japaneseName: String,
    val japanesePoem: String,
    val oreName: String,
    val tier: Int,
    val isHandheld: Boolean,
    val initializer: ItemVariantScope<ItemSimpleMaterials, ItemVariantSimpleMaterials>.() -> Unit
) {
    APATITE_GEM(0, "apatite_gem", "gemApatite", "Apatite", "燐灰石", "", "gemApatite", 0, false, { }),
    FLUORITE_GEM(1, "fluorite_gem", "gemFluorite", "Fluorite", "蛍石", "", "gemFluorite", 0, false, { }),
    SULFUR_GEM(2, "sulfur_gem", "gemSulfur", "Sulfur", "硫黄", "", "gemSulfur", 0, false, { }),
    MIRAGIUM_DUST(3, "miragium_dust", "dustMiragium", "Miragium Dust", "ミラジウムの粉", "", "dustMiragium", 0, false, { }),
    MIRAGIUM_TINY_DUST(4, "miragium_tiny_dust", "dustTinyMiragium", "Tiny Pile of Miragium Dust", "ミラジウムの微粉", "", "dustTinyMiragium", 0, false, { }),
    MIRAGIUM_INGOT(5, "miragium_ingot", "ingotMiragium", "Miragium Ingot", "ミラジウムインゴット", "", "ingotMiragium", 2, false, { }),
    CINNABAR_GEM(6, "cinnabar_gem", "gemCinnabar", "Cinnabar", "辰砂", "", "gemCinnabar", 0, false, { }),
    MOONSTONE_GEM(7, "moonstone_gem", "gemMoonstone", "Moonstone", "月長石", "", "gemMoonstone", 0, false, { }),
    MAGNETITE_GEM(8, "magnetite_gem", "gemMagnetite", "Magnetite", "磁鉄鉱", "", "gemMagnetite", 0, false, { }),
    SALTPETER_GEM(9, "saltpeter_gem", "gemSaltpeter", "Saltpeter", "硝石", "", "gemSaltpeter", 0, false, {
        modScope.makeRecipe("saltpeter") {
            DataShapedRecipe(
                pattern = listOf(
                    "m",
                    "D",
                    "B"
                ),
                key = mapOf(
                    "m" to WandType.MELTING.ingredientData,
                    "D" to DataSimpleIngredient(item = "minecraft:dirt", data = 0),
                    "B" to DataOreIngredient(ore = "container1000Water")
                ),
                result = DataResult(item = "miragefairy2019:materials", data = 9)
            )
        }
    }),
    PYROPE_GEM(10, "pyrope_gem", "gemPyrope", "Pyrope", "パイロープ", "", "gemPyrope", 0, false, { }),
    SMITHSONITE_GEM(11, "smithsonite_gem", "gemSmithsonite", "Smithsonite", "スミソナイト", "", "gemSmithsonite", 0, false, { }),
    MIRAGIUM_ROD(12, "miragium_rod", "rodMiragium", "Miragium Rod", "ミラジウムの棒", "", "rodMiragium", 2, true, {
        modScope.makeRecipe("miragium_rod_by_hand") { // TODO rename
            DataShapedRecipe(
                pattern = listOf(
                    "  I",
                    " I ",
                    "I  "
                ),
                key = mapOf(
                    "I" to DataOreIngredient(ore = "ingotMiragium")
                ),
                result = DataResult(item = "miragefairy2019:materials", data = 12)
            )
        }
    }),
    MIRAGIUM_NUGGET(13, "miragium_nugget", "nuggetMiragium", "Miragium Nugget", "ミラジウムの塊", "", "nuggetMiragium", 2, false, {
        modScope.makeRecipe("miragium_nugget_by_fairy") { // 妖精→
            DataShapelessRecipe(
                ingredients = listOf(
                    WandType.CRAFTING.ingredientData,
                    DataOreIngredient(ore = "dustMiragium"),
                    DataOreIngredient(ore = "mirageFairyCrystal"),
                    DataOreIngredient(ore = "mirageFairy2019FairyAbilityFlame")
                ),
                result = DataResult(item = "miragefairy2019:materials", data = 13, count = 2)
            )
        }
        modScope.makeRecipe("miragium_nugget_by_fairy_wand") {
            DataShapelessRecipe(
                ingredients = listOf(
                    WandType.MELTING.ingredientData,
                    DataOreIngredient(ore = "dustMiragium")
                ),
                result = DataResult(item = "miragefairy2019:materials", data = 13, count = 3)
            )
        }
    }),
    NEPHRITE_GEM(14, "nephrite_gem", "gemNephrite", "Nephrite", "ネフライト", "", "gemNephrite", 0, false, { }),
    TOPAZ_GEM(15, "topaz_gem", "gemTopaz", "Topaz", "トパーズ", "", "gemTopaz", 0, false, { }),
    TOURMALINE_GEM(16, "tourmaline_gem", "gemTourmaline", "Tourmaline", "トルマリン", "", "gemTourmaline", 0, false, { }),
    HELIOLITE_GEM(17, "heliolite_gem", "gemHeliolite", "Heliolite", "ヘリオライト", "", "gemHeliolite", 0, false, { }),
    LABRADORITE_GEM(18, "labradorite_gem", "gemLabradorite", "Labradorite", "ラブラドライト", "", "gemLabradorite", 0, false, { }),
    LILAGIUM_INGOT(19, "lilagium_ingot", "ingotLilagium", "Lilagium Ingot", "リラジウムインゴット", "草花よ、セルロースの神々よ、エーテルの導きにより道を開き給へ", "ingotLilagium", 2, false, {
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "mLm",
                    "LIL",
                    "mLm"
                ),
                key = mapOf(
                    "I" to DataOreIngredient(ore = "ingotMiragium"),
                    "L" to DataSimpleIngredient(item = "minecraft:double_plant", data = 1),
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:materials", data = 19)
            )
        }
    }),
    MIRAGIUM_PLATE(20, "miragium_plate", "plateMiragium", "Miragium Plate", "ミラジウムの板", "", "plateMiragium", 2, false, {
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "mm",
                    "II",
                    "II"
                ),
                key = mapOf(
                    "I" to DataOreIngredient(ore = "ingotMiragium"),
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:materials", data = 20)
            )
        }
    }),
    COAL_DUST(21, "coal_dust", "dustCoal", "Coal Dust", "石炭の粉", "", "dustCoal", 0, false, {
        fuel(1600)
    }),
    CHARCOAL_DUST(22, "charcoal_dust", "dustCharcoal", "Charcoal Dust", "木炭の粉", "", "dustCharcoal", 0, false, {
        fuel(1600)
    }),
    APATITE_DUST(23, "apatite_dust", "dustApatite", "Apatite Dust", "燐灰石の粉", "", "dustApatite", 0, false, { }),
    FLUORITE_DUST(24, "fluorite_dust", "dustFluorite", "Fluorite Dust", "蛍石の粉", "", "dustFluorite", 0, false, { }),
    SULFUR_DUST(25, "sulfur_dust", "dustSulfur", "Sulfur Dust", "硫黄の粉", "", "dustSulfur", 0, false, {
        modScope.makeRecipe("fire_charge_from_sulfur_dust") { // →ファイヤーチャージ
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "gunpowder"),
                    DataOreIngredient(ore = "dustSulfur"),
                    DataOrIngredient(
                        DataSimpleIngredient(item = "minecraft:coal", data = 0),
                        DataSimpleIngredient(item = "minecraft:coal", data = 1),
                        DataOreIngredient(ore = "dustCoal"),
                        DataOreIngredient(ore = "dustCharcoal")
                    ),
                    DataOreIngredient(ore = "dustMiragium")
                ),
                result = DataResult(item = "minecraft:fire_charge", count = 3)
            )
        }
        modScope.makeRecipe("gunpowder_from_saltpeter") { // →火薬
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOrIngredient(
                        DataSimpleIngredient(item = "minecraft:coal", data = 1),
                        DataOreIngredient(ore = "dustCharcoal")
                    ),
                    DataOreIngredient(ore = "dustSulfur"),
                    DataOreIngredient(ore = "gemSaltpeter"),
                    DataOreIngredient(ore = "dustMiragium")
                ),
                result = DataResult(item = "minecraft:gunpowder", count = 3)
            )
        }
    }),
    CINNABAR_DUST(26, "cinnabar_dust", "dustCinnabar", "Cinnabar Dust", "辰砂の粉", "", "dustCinnabar", 0, false, { }),
    MOONSTONE_DUST(27, "moonstone_dust", "dustMoonstone", "Moonstone Dust", "月長石の粉", "", "dustMoonstone", 0, false, { }),
    MAGNETITE_DUST(28, "magnetite_dust", "dustMagnetite", "Magnetite Dust", "磁鉄鉱の粉", "", "dustMagnetite", 0, false, { }),
    PYRITE_GEM(29, "pyrite_gem", "gemPyrite", "Pyrite", "パイライト", "", "gemPyrite", 0, false, {
        modScope.onAddRecipe a@{
            GameRegistry.addSmelting("gemPyrite".toOreName().copyItemStack() ?: return@a, "nuggetIron".toOreName().copyItemStack(3) ?: return@a, 0.7f)
        }
    }),
}

private fun ItemVariantScope<ItemSimpleMaterials, ItemVariantSimpleMaterials>.fuel(burnTime: Int): ItemVariantScope<ItemSimpleMaterials, ItemVariantSimpleMaterials> {
    itemScope.modScope.onRegisterItem {
        itemVariant.burnTime = burnTime
    }
    return this
}


lateinit var itemMaterials: () -> ItemSimpleMaterials

val commonMaterialsModule = module {
    itemMaterials = item({ ItemSimpleMaterials() }, "materials") {
        setUnlocalizedName("materials")
        setCreativeTab { Main.creativeTab }

        CommonMaterialCard.values().forEach { card ->
            itemVariant(card.registryName, { ItemVariantSimpleMaterials(card) }, card.metadata) {
                addOreName(card.oreName)
                makeItemModel { if (card.isHandheld) handheld else generated }
                lang("item.${card.unlocalizedName}.name", card.englishName, card.japaneseName)
                lang("item.${card.unlocalizedName}.poem", "", card.japanesePoem)
                card.initializer(this@itemVariant)
            }
        }

        onRegisterItem {
            if (Main.side.isClient) item.setCustomModelResourceLocations()
        }
    }
}
