package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.mod.material.FairyMaterialCard
import mirrg.kotlin.hydrogen.toUpperCamelCase

val fluidEmptyingRecipeModule = module {

    fun makeRecipe(fluidName: String, containerName: String, containerCapacity: Int) {
        makeRecipe("fluid_emptying/${fluidName}_${containerName}") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "container${containerCapacity}${fluidName.toUpperCamelCase()}"),
                    DataOreIngredient(ore = "mirageFairyAnnihilationPottery")
                ),
                result = DataResult(item = "miragefairy2019:fairy_materials", data = FairyMaterialCard.ANNIHILATION_POTTERY.metadata)
            )
        }
    }

    fun makeRecipe(fluidName: String) {
        makeRecipe(fluidName, "bottle", 250)
        makeRecipe(fluidName, "bucket", 1000)
    }

    makeRecipe("water")
    makeRecipe("lava")
    makeRecipe("miragium_water")
    makeRecipe("mirage_flower_extract")
    makeRecipe("mirage_flower_oil")
    makeRecipe("mirage_fairy_blood")
    makeRecipe("carbonated_water")
    makeRecipe("mirage_fairy_varnish")

}
