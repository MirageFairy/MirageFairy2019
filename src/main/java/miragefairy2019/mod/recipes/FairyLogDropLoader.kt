package miragefairy2019.mod.recipes

import miragefairy2019.api.FairyLogDropRegistry
import miragefairy2019.lib.FairyLogDropRecipe
import miragefairy2019.lib.FairyLogDropRequirementCanRain
import miragefairy2019.lib.FairyLogDropRequirementHasBiomeType
import miragefairy2019.lib.FairyLogDropRequirementOverworld
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.createItemStack
import miragefairy2019.mod.fairyrelation.FairyRelationRegistries
import net.minecraftforge.common.BiomeDictionary

val fairyLogDropLoaderModule = module {
    onCreateItemStack {
        fun FairyLogDropRecipe.biome(biome: BiomeDictionary.Type) = also { requirements += FairyLogDropRequirementHasBiomeType(biome) }
        fun FairyLogDropRecipe.overworld() = also { requirements += FairyLogDropRequirementOverworld() }
        fun FairyLogDropRecipe.canRain() = also { requirements += FairyLogDropRequirementCanRain() }

        operator fun FairyCard.invoke(weight: Double, block: FairyLogDropRecipe.() -> Unit) {
            val recipe = FairyLogDropRecipe(weight) { createItemStack() }
            recipe.block()
            FairyLogDropRegistry.fairyLogDropRecipes += recipe
        }

        // 概念系
        FairyCard.AIR(0.01) { }
        FairyCard.TIME(0.01) { }
        FairyCard.GRAVITY(0.01) { }

        // 天体系
        FairyCard.SUN(0.01) { overworld() }
        FairyCard.MOON(0.01) { overworld() }
        FairyCard.STAR(0.01) { overworld() }

        // 時間帯
        FairyCard.DAYTIME(0.1) { overworld() }
        FairyCard.NIGHT(0.1) { overworld() }
        FairyCard.MORNING(0.1) { overworld() }
        FairyCard.SUNRISE(0.02) { overworld() }

        // 季節
        FairyCard.AUTUMN(0.1) { overworld() }

        // 天候
        FairyCard.FINE(0.1) { overworld() }
        FairyCard.RAIN(0.1) { overworld().canRain() }
        FairyCard.THUNDER(0.02) { overworld().canRain() }

        // バイオーム
        FairyRelationRegistries.biomeType.forEach { relation ->
            relation.fairyCard(relation.relevance * relation.weight) { biome(relation.key) }
        }

    }
}
