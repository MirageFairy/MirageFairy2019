package miragefairy2019.mod.recipes

import miragefairy2019.api.FairyLogDropRegistry
import miragefairy2019.lib.FairyLogDropRecipe
import miragefairy2019.lib.FairyLogDropRequirementCanRain
import miragefairy2019.lib.FairyLogDropRequirementHasBiomeType
import miragefairy2019.lib.FairyLogDropRequirementOverworld
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairy.FairyTypes
import miragefairy2019.mod.fairy.RankedFairyTypeBundle
import miragefairy2019.mod.fairyrelation.FairyRelationRegistries
import net.minecraftforge.common.BiomeDictionary

val fairyLogDropLoaderModule = module {
    onCreateItemStack {
        fun FairyLogDropRecipe.biome(biome: BiomeDictionary.Type) = also { requirements += FairyLogDropRequirementHasBiomeType(biome) }
        fun FairyLogDropRecipe.overworld() = also { requirements += FairyLogDropRequirementOverworld() }
        fun FairyLogDropRecipe.canRain() = also { requirements += FairyLogDropRequirementCanRain() }

        operator fun RankedFairyTypeBundle.invoke(weight: Double, block: FairyLogDropRecipe.() -> Unit) {
            val recipe = FairyLogDropRecipe(weight) { createItemStack() }
            recipe.block()
            FairyLogDropRegistry.fairyLogDropRecipes += recipe
        }

        FairyTypes.instance.run {

            // 概念系
            air(0.01) { }
            time(0.01) { }
            gravity(0.01) { }

            // 天体系
            sun(0.01) { overworld() }
            moon(0.01) { overworld() }
            star(0.01) { overworld() }

            // 時間帯
            daytime(0.1) { overworld() }
            night(0.1) { overworld() }
            morning(0.1) { overworld() }
            sunrise(0.02) { overworld() }

            // 天候
            fine(0.1) { overworld() }
            rain(0.1) { overworld().canRain() }
            thunder(0.02) { overworld().canRain() }

            // バイオーム
            FairyRelationRegistries.biomeType.forEach { relation ->
                relation.fairy(relation.relevance * relation.weight) { biome(relation.key) }
            }

        }
    }
}
