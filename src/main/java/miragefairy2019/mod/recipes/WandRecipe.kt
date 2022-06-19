package miragefairy2019.mod.recipes

import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.resourcemaker.DataIngredient
import miragefairy2019.resourcemaker.DataOreIngredient
import miragefairy2019.resourcemaker.DataResult
import miragefairy2019.resourcemaker.DataShapedRecipe
import miragefairy2019.resourcemaker.DataShapelessRecipe
import miragefairy2019.resourcemaker.DataSimpleIngredient
import miragefairy2019.resourcemaker.makeRecipe

object WandRecipe {
    val module = module {

        // 破砕のワンドによる粉砕
        fun makeDustRecipe(registryName: String, ingredient: DataIngredient, metadata: Int) {
            makeRecipe(registryName) {
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.BREAKING.ingredientData,
                        ingredient
                    ),
                    result = DataResult(
                        item = "miragefairy2019:materials",
                        data = metadata
                    )
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
                result = DataResult(
                    item = "minecraft:packed_ice"
                )
            )
        }

        // 塗れたスポンジ＋紅蓮のワンド→スポンジ
        makeRecipe("sponge_from_wet_sponge") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataSimpleIngredient(item = "minecraft:sponge", data = 1),
                    WandType.MELTING.ingredientData
                ),
                result = DataResult(
                    item = "minecraft:sponge",
                    data = 0
                )
            )
        }

        // 砂岩＋破砕のワンド→砂
        makeRecipe("sand_from_sandstone") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "sandstone"),
                    WandType.BREAKING.ingredientData
                ),
                result = DataResult(
                    item = "minecraft:sand",
                    data = 0
                )
            )
        }

    }
}
