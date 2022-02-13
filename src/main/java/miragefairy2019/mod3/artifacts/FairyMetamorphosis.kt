package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.toUpperCamelCase

object FairyMetamorphosis {
    val module: Module = {
        fun make(resultName: String, fairyMotif: String, result: DataResult) {
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "fairy_metamorphosis/${resultName}_from_${fairyMotif}"),
                DataShapelessRecipe(
                    ingredients = listOf(
                        DataOreIngredient(
                            type = "miragefairy2019:ore_dict_complex",
                            ore = "mirageFairyStick"
                        ),
                        DataOreIngredient(
                            ore = "container250MiragiumWater"
                        ),
                        DataOreIngredient(
                            ore = "mirageFairy2019Fairy${fairyMotif.toUpperCamelCase()}Rank1"
                        ),
                        DataSimpleIngredient(
                            item = "minecraft:dirt",
                            data = 0
                        )
                    ),
                    result = result
                )
            )
        }
        make("chicken", "chicken", DataResult(item = "minecraft:chicken"))
        make("beef", "cow", DataResult(item = "minecraft:beef"))
        make("bone_meal", "skeleton", DataResult(item = "minecraft:dye", data = 15))
        make("rotten_flesh", "zombie", DataResult(item = "minecraft:rotten_flesh"))
        make("slime_ball", "slime", DataResult(item = "minecraft:slime_ball"))
        make("magma_cream", "magma_cube", DataResult(item = "minecraft:magma_cream"))
        make("blaze_powder", "blaze", DataResult(item = "minecraft:blaze_powder"))
    }
}
