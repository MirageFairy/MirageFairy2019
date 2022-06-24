package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.item
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredient
import miragefairy2019.mod.fairyrelation.FairyRelationRegistries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreIngredient

val fairySummoningRecipeModule = module {
    onAddRecipe {
        var counter = 0

        // レシピに変換可能な妖精関係の関係性順のリスト
        // この順序のまま登録すれば競合したレシピが関係性の強いものが優先して使われる
        // ingredient, entry
        val pairs = listOf(
            FairyRelationRegistries.ingredient.entries.withoutPartiallyMatch.map { Pair(it.key, it) },
            FairyRelationRegistries.item.entries.withoutPartiallyMatch.map { Pair(it.key.ingredient, it) },
            FairyRelationRegistries.block.entries.withoutPartiallyMatch.mapNotNull { it.key.item?.ingredient?.let { ingredient -> Pair(ingredient, it) } }
        ).flatten().sortedByDescending { it.second.relevance }

        // 登録
        pairs.forEach { (ingredient, entry) ->

            // 召喚のワンド使用
            GameRegistry.addShapelessRecipe(
                ResourceLocation("${ModMirageFairy2019.MODID}:mirage_fairy_from_item_$counter"),
                null,
                entry.fairy.main.createItemStack(),
                WandType.SUMMONING.ingredient,
                OreIngredient("mirageFairyCrystal"),
                ingredient
            )
            counter++

            // 月長石使用
            GameRegistry.addShapelessRecipe(
                ResourceLocation("${ModMirageFairy2019.MODID}:mirage_fairy_from_item_$counter"),
                null,
                entry.fairy.main.createItemStack(),
                OreIngredient("gemMoonstone"),
                OreIngredient("mirageFairyCrystal"),
                ingredient
            )
            counter++

        }

    }
}
