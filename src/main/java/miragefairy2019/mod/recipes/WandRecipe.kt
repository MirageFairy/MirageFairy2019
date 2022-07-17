package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.DataIngredient
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData

val wandRecipeModule = module {

    // 破砕のワンドによる粉砕
    fun makeDustRecipe(registryName: String, ingredient: DataIngredient, metadata: Int) {
        makeRecipe(registryName) {
            DataShapelessRecipe(
                ingredients = listOf(
                    WandType.BREAKING.ingredientData,
                    ingredient
                ),
                result = DataResult(item = "miragefairy2019:materials", data = metadata)
            )
        }
    }
    makeDustRecipe("apatite_dust", DataOreIngredient(ore = "gemApatite"), 23)
    makeDustRecipe("fluorite_dust", DataOreIngredient(ore = "gemFluorite"), 24)
    makeDustRecipe("sulfur_dust", DataOreIngredient(ore = "gemSulfur"), 25)
    makeDustRecipe("cinnabar_dust", DataOreIngredient(ore = "gemCinnabar"), 26)
    makeDustRecipe("moonstone_dust", DataOreIngredient(ore = "gemMoonstone"), 27)
    makeDustRecipe("magnetite_dust", DataOreIngredient(ore = "gemMagnetite"), 28)
    makeDustRecipe("coal_dust", DataSimpleIngredient(item = "minecraft:coal", data = 0), 21)
    makeDustRecipe("charcoal_dust", DataSimpleIngredient(item = "minecraft:coal", data = 1), 22)

    // 氷8＋氷結のワンド→氷塊
    makeRecipe("packed_ice") {
        DataShapedRecipe(
            pattern = listOf(
                "###",
                "#f#",
                "###"
            ),
            key = mapOf(
                "#" to DataOreIngredient(ore = "ice"),
                "f" to WandType.FREEZING.ingredientData
            ),
            result = DataResult(item = "minecraft:packed_ice")
        )
    }

    // 塗れたスポンジ＋紅蓮のワンド→スポンジ
    makeRecipe("sponge_from_wet_sponge") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataSimpleIngredient(item = "minecraft:sponge", data = 1),
                WandType.MELTING.ingredientData
            ),
            result = DataResult(item = "minecraft:sponge", data = 0)
        )
    }

    // 砂岩＋破砕のワンド→砂
    makeRecipe("sand_from_sandstone") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "sandstone"),
                WandType.BREAKING.ingredientData
            ),
            result = DataResult(item = "minecraft:sand", data = 0)
        )
    }

    // ブレイズロッドからブレイズパウダー　高効率
    makeRecipe("blaze_powder_from_blaze_rod") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataSimpleIngredient(item = "minecraft:blaze_rod")
            ),
            result = DataResult(item = "minecraft:blaze_powder", count = 3)
        )
    }

    // 骨＞破砕のワンド→骨粉
    makeRecipe("bone_meal_from_bone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "bone")
            ),
            result = DataResult(item = "minecraft:dye", data = 15, count = 4)
        )
    }

    // 粘土ブロック→粘土
    makeRecipe("clay_ball_from_clay") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataSimpleIngredient(item = "minecraft:clay")
            ),
            result = DataResult(item = "minecraft:clay_ball", count = 4)
        )
    }

    // 石→丸石
    makeRecipe("cobblestone_from_stone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "stone")
            ),
            result = DataResult(item = "minecraft:cobblestone")
        )
    }

    // グロウストーン→グロウストーンダスト
    makeRecipe("glowstone_dust_from_glowstone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "glowstone")
            ),
            result = DataResult(item = "minecraft:glowstone_dust", count = 4)
        )
    }

    // 丸石→砂利
    makeRecipe("gravel_from_cobblestone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "cobblestone")
            ),
            result = DataResult(item = "minecraft:gravel")
        )
    }

    // 水→氷
    makeRecipe("ice_from_water") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.FREEZING.ingredientData,
                DataOreIngredient(ore = "container1000Water")
            ),
            result = DataResult(item = "minecraft:ice")
        )
    }

    // 黒曜石→溶岩
    makeRecipe("lava_from_obsidian") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.MELTING.ingredientData,
                DataOreIngredient(ore = "obsidian"),
                DataSimpleIngredient(item = "minecraft:bucket")
            ),
            result = DataResult(item = "minecraft:lava_bucket")
        )
    }

    // ネザーウォートブロック→ネザーウォート
    makeRecipe("nether_wart_from_nether_wart_block") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataSimpleIngredient(item = "minecraft:nether_wart_block")
            ),
            result = DataResult(item = "minecraft:nether_wart", count = 9)
        )
    }

    // 溶岩→黒曜石
    makeRecipe("obsidian_from_lava") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.HYDRATING.ingredientData,
                DataOreIngredient(ore = "container1000Lava")
            ),
            result = DataResult(item = "minecraft:obsidian")
        )
    }

    // ネザークォーツブロック→ネザークォーツ
    makeRecipe("quartz_from_quartz_block") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "blockQuartz")
            ),
            result = DataResult(item = "minecraft:quartz", count = 4)
        )
    }

    // 氷→雪
    makeRecipe("snow_from_ice") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "ice")
            ),
            result = DataResult(item = "minecraft:snow")
        )
    }

    // 雪ブロック→雪玉
    makeRecipe("snowball_from_snow") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataSimpleIngredient(item = "minecraft:snow")
            ),
            result = DataResult(item = "minecraft:snowball", count = 4)
        )
    }

    // 羊毛→糸
    makeRecipe("string_from_wool") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataOreIngredient(ore = "wool")
            ),
            result = DataResult(item = "minecraft:string", count = 4)
        )
    }

    // 氷→水
    makeRecipe("water_from_ice") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.MELTING.ingredientData,
                DataOreIngredient(ore = "ice"),
                DataSimpleIngredient(item = "minecraft:bucket")
            ),
            result = DataResult(item = "minecraft:water_bucket")
        )
    }

    // 加水のワンド→水
    makeRecipe("water_pot_from_fairy_wand") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.HYDRATING.ingredientData,
                DataOreIngredient(ore = "mirageFairyPot")
            ),
            result = DataResult(item = "miragefairy2019:filled_bucket", data = 3)
        )
    }
    makeRecipe("water_bucket_from_fairy_wand") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.HYDRATING.ingredientData,
                DataSimpleIngredient(item = "minecraft:bucket")
            ),
            result = DataResult(item = "minecraft:water_bucket")
        )
    }

}
