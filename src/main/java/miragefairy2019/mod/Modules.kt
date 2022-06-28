package miragefairy2019.mod

import miragefairy2019.jei.jeiModule
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.DataOrIngredient
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.artifactsModule
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.mod.fairy.Fairy
import miragefairy2019.mod.fairybox.FairyBox
import miragefairy2019.mod.fairyrelation.fairyRelationModule
import miragefairy2019.mod.fairyweapon.fairyWeaponModule
import miragefairy2019.mod.material.materialModule
import miragefairy2019.mod.pedestal.pedestalModule
import miragefairy2019.mod.placeditem.PlacedItem
import miragefairy2019.mod.playeraura.playerAuraModule
import miragefairy2019.mod.recipes.recipesModule
import miragefairy2019.mod.skill.skillModule
import miragefairy2019.mod.systems.systemsModule

val modules = module {

    Main.mainModule(this)
    jeiModule(this)

    artifactsModule(this)
    pedestalModule(this)
    materialModule(this)
    systemsModule(this)
    recipesModule(this)

    playerAuraModule(this)
    PlacedItem.placedItemMdule(this)
    skillModule(this)
    Fairy.fairyModule(this)
    fairyWeaponModule(this)
    fairyRelationModule(this)
    FairyBox.fairyBoxModule(this)

    // TODO move

    // ミラジウムの板
    makeRecipe("miragium_plate") {
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
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 20
            )
        )
    }

    // 歪曲杖からミラジウムの板
    makeRecipe("miragium_plate_from_distortion_fairy_wand") {
        DataShapedRecipe(
            pattern = listOf(
                "d",
                "I",
                "I"
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "d" to WandType.DISTORTION.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 20
            )
        )
    }

    // リラジウム
    makeRecipe("lilagium_ingot") {
        DataShapedRecipe(
            pattern = listOf(
                "mLm",
                "LIL",
                "mLm"
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "L" to DataSimpleIngredient(
                    item = "minecraft:double_plant",
                    data = 1
                ),
                "m" to WandType.MELTING.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 19
            )
        )
    }

    // 融合のワンドからリラジウム
    makeRecipe("lilagium_ingot_from_fusion_fairy_wand") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "ingotMiragium"),
                DataSimpleIngredient(
                    item = "minecraft:double_plant",
                    data = 1
                ),
                DataSimpleIngredient(
                    item = "minecraft:double_plant",
                    data = 1
                ),
                WandType.FUSION.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 19
            )
        )
    }

    // 妖精の革
    makeRecipe("mirage_fairy_leather") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "leather"),
                DataOreIngredient(ore = "container1000MirageFlowerOil")
            ),
            result = DataResult(
                item = "miragefairy2019:fairy_materials",
                data = 14
            )
        )
    }

    // 歪曲のワンドからミラジウムの棒
    makeRecipe("miragium_rod_from_distortion_fairy_wand") {
        DataShapedRecipe(
            pattern = listOf(
                "d ",
                " I"
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "d" to WandType.DISTORTION.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 12
            )
        )
    }

    // 妖精蒐集箱
    makeRecipe("fairy_collection_box") {
        DataShapedRecipe(
            pattern = listOf(
                "sls",
                "PLD",
                "sCs"
            ),
            key = mapOf(
                "L" to DataOreIngredient(ore = "logWood"),
                "P" to DataOreIngredient(ore = "paneGlass"),
                "D" to DataOreIngredient(ore = "doorWood"),
                "l" to DataOreIngredient(ore = "torch"),
                "C" to DataSimpleIngredient(
                    item = "minecraft:carpet",
                    data = 14
                ),
                "s" to DataOreIngredient(ore = "mirageFairy2019SphereSpace")
            ),
            result = DataResult(
                item = "miragefairy2019:fairy_collection_box"
            )
        )
    }

    // ブレイズロッドからブレイズパウダー　高効率
    makeRecipe("blaze_powder_from_blaze_rod") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataSimpleIngredient(
                    item = "minecraft:blaze_rod"
                )
            ),
            result = DataResult(
                item = "minecraft:blaze_powder",
                count = 3
            )
        )
    }

    // 妖精の原木
    makeRecipe("fairy_wood_log") {
        DataShapedRecipe(
            pattern = listOf(
                "ooo",
                "oLo",
                "ooo"
            ),
            key = mapOf(
                "L" to DataOreIngredient(ore = "logWood"),
                "o" to DataOreIngredient(ore = "container1000MirageFlowerOil")
            ),
            result = DataResult(
                item = "miragefairy2019:fairy_wood_log"
            )
        )
    }

    // 硫黄の粉→ファイヤーチャージ
    makeRecipe("fire_charge_from_sulfur_dust") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "gunpowder"),
                DataOreIngredient(ore = "dustSulfur"),
                DataOrIngredient(
                    DataSimpleIngredient(
                        item = "minecraft:coal",
                        data = 0
                    ),
                    DataSimpleIngredient(
                        item = "minecraft:coal",
                        data = 1
                    ),
                    DataOreIngredient(ore = "dustCoal"),
                    DataOreIngredient(ore = "dustCharcoal")
                ),
                DataOreIngredient(ore = "dustMiragium")
            ),
            result = DataResult(
                item = "minecraft:fire_charge",
                count = 3
            )
        )
    }

    // エリトラ
    makeRecipe("elytra_from_mirage_fairy_leather") {
        DataShapedRecipe(
            pattern = listOf(
                "SLS",
                "LLL",
                "LcL"
            ),
            key = mapOf(
                "c" to WandType.CRAFTING.ingredientData,
                "S" to DataOreIngredient(ore = "mirageFairy2019SphereLevitate"),
                "L" to DataOreIngredient(ore = "mirageFairyLeather")
            ),
            result = DataResult(
                item = "minecraft:elytra"
            )
        )
    }

}
