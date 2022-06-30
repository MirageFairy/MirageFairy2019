package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.DataIngredient
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeRecipe
import mirrg.kotlin.hydrogen.toUpperCamelCase

val fairyTamingRecipeModule = module {
    fun make(
        name: String,
        fairies: List<String>,
        additionalInputs: List<DataIngredient>,
        result: DataResult
    ) = makeRecipe("fairy_taming/${name}_generation") {
        DataShapelessRecipe(
            ingredients = listOf(
                listOf(DataOreIngredient(ore = "mirageFairyStick")),
                fairies.map { fairy -> DataOreIngredient(ore = "mirageFairy2019Fairy${fairy.toUpperCamelCase()}Rank1") },
                additionalInputs
            ).flatten(),
            result = result
        )
    }
    make("egg", listOf("chicken"), listOf(), DataResult(item = "minecraft:egg"))
    make("milk_bucket", listOf("cow"), listOf(DataSimpleIngredient(item = "minecraft:bucket")), DataResult(item = "minecraft:milk_bucket"))
    make("stone", listOf("water", "lava"), listOf(), DataResult(item = "minecraft:stone", data = 0))
    make("string", listOf("spider"), listOf(), DataResult(item = "minecraft:string"))
    make("water_bucket", listOf("water"), listOf(DataSimpleIngredient(item = "minecraft:bucket")), DataResult(item = "minecraft:water_bucket"))
}
