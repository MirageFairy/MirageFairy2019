package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.mod.ModMirageFairy2019

object WandRecipe {
    val module: Module = {
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "packed_ice"),
            DataShapedRecipe(
                pattern = listOf(
                    "###",
                    "#f#",
                    "###"
                ),
                key = mapOf(
                    "#" to DataOreIngredient(ore = "ice"),
                    "f" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandFreezing")
                ),
                result = DataResult(
                    item = "minecraft:packed_ice"
                )
            )
        )
    }
}
