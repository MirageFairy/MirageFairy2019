package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.mod.ModMirageFairy2019

object WandRecipe {
    val module: Module = {

        // 氷8＋氷結のワンド→氷塊
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

        // 塗れたスポンジ＋紅蓮のワンド→スポンジ
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "sponge_from_wet_sponge"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataSimpleIngredient(item = "minecraft:sponge", data = 1),
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting")
                ),
                result = DataResult(
                    item = "minecraft:sponge",
                    data = 0
                )
            )
        )

    }
}
