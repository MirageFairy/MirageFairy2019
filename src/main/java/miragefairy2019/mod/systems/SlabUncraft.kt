package miragefairy2019.mod.systems

import miragefairy2019.libkt.DataIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.WandType

object SlabUncraft {
    val module = module {
        fun r(resourceName: String, slab: DataIngredient, block: DataResult) = makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "slab_uncraft/$resourceName"),
            DataShapelessRecipe(
                ingredients = listOf(
                    slab,
                    slab,
                    WandType.CRAFTING.ingredientData
                ),
                result = block
            )
        )
        r("stone", DataSimpleIngredient(item = "minecraft:stone_slab", data = 0), DataResult(item = "minecraft:stone", data = 0))
        r("sandstone", DataSimpleIngredient(item = "minecraft:stone_slab", data = 1), DataResult(item = "minecraft:sandstone", data = 0))
        r("cobblestone", DataSimpleIngredient(item = "minecraft:stone_slab", data = 3), DataResult(item = "minecraft:cobblestone"))
        r("brick_block", DataSimpleIngredient(item = "minecraft:stone_slab", data = 4), DataResult(item = "minecraft:brick_block"))
        r("stonebrick", DataSimpleIngredient(item = "minecraft:stone_slab", data = 5), DataResult(item = "minecraft:stonebrick", data = 0))
        r("nether_brick", DataSimpleIngredient(item = "minecraft:stone_slab", data = 6), DataResult(item = "minecraft:nether_brick"))
        r("quartz_block", DataSimpleIngredient(item = "minecraft:stone_slab", data = 7), DataResult(item = "minecraft:quartz_block", data = 0))

        r("oak_planks", DataSimpleIngredient(item = "minecraft:wooden_slab", data = 0), DataResult(item = "minecraft:planks", data = 0))
        r("spruce_planks", DataSimpleIngredient(item = "minecraft:wooden_slab", data = 1), DataResult(item = "minecraft:planks", data = 1))
        r("birch_planks", DataSimpleIngredient(item = "minecraft:wooden_slab", data = 2), DataResult(item = "minecraft:planks", data = 2))
        r("jungle_planks", DataSimpleIngredient(item = "minecraft:wooden_slab", data = 3), DataResult(item = "minecraft:planks", data = 3))
        r("acacia_planks", DataSimpleIngredient(item = "minecraft:wooden_slab", data = 4), DataResult(item = "minecraft:planks", data = 4))
        r("dark_oak_planks", DataSimpleIngredient(item = "minecraft:wooden_slab", data = 5), DataResult(item = "minecraft:planks", data = 5))

        r("red_sandstone", DataSimpleIngredient(item = "minecraft:stone_slab2", data = 0), DataResult(item = "minecraft:red_sandstone", data = 0))

        r("purpur_block", DataSimpleIngredient(item = "minecraft:purpur_slab", data = 0), DataResult(item = "minecraft:purpur_block"))
    }
}
