package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeRecipe
import mirrg.kotlin.hydrogen.toUpperCamelCase

val fairyMetamorphosisRecipeModule = module {
    fun make(resultName: String, fairyMotif: String, result: DataResult) {
        makeRecipe("fairy_metamorphosis/${resultName}_from_${fairyMotif}") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairyStick"),
                    DataOreIngredient(ore = "container250MiragiumWater"),
                    DataOreIngredient(ore = "mirageFairy2019Fairy${fairyMotif.toUpperCamelCase()}Rank1"),
                    DataSimpleIngredient(item = "minecraft:dirt", data = 0)
                ),
                result = result
            )
        }
    }
    make("chicken", "chicken", DataResult(item = "minecraft:chicken"))
    make("beef", "cow", DataResult(item = "minecraft:beef"))
    make("bone_meal", "skeleton", DataResult(item = "minecraft:dye", data = 15))
    make("rotten_flesh", "zombie", DataResult(item = "minecraft:rotten_flesh"))
    make("slime_ball", "slime", DataResult(item = "minecraft:slime_ball"))
    make("magma_cream", "magma_cube", DataResult(item = "minecraft:magma_cream"))
    make("blaze_powder", "blaze", DataResult(item = "minecraft:blaze_powder"))
}
