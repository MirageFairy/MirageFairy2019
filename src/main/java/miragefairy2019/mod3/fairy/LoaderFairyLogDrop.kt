package miragefairy2019.mod3.fairy

import miragefairy2019.api.FairyLogDropRegistry
import miragefairy2019.lib.FairyLogDropRecipe
import miragefairy2019.lib.FairyLogDropRequirementCanRain
import miragefairy2019.lib.FairyLogDropRequirementHasBiomeType
import miragefairy2019.lib.FairyLogDropRequirementOverworld
import miragefairy2019.libkt.module
import miragefairy2019.mod3.fairy.relation.FairyRelationRegistries
import net.minecraftforge.common.BiomeDictionary

val loaderFairyLogDrop = module {
    onCreateItemStack {
        fun FairyLogDropRecipe.biome(biome: BiomeDictionary.Type) = also { requirements += FairyLogDropRequirementHasBiomeType(biome) }
        fun FairyLogDropRecipe.overworld() = also { requirements += FairyLogDropRequirementOverworld() }
        fun FairyLogDropRecipe.canRain() = also { requirements += FairyLogDropRequirementCanRain() }

        operator fun RankedFairyTypeBundle.invoke(weight: Double, block: FairyLogDropRecipe.() -> Unit) {
            val recipe = FairyLogDropRecipe(weight) { main.createItemStack() }
            recipe.block()
            FairyLogDropRegistry.fairyLogDropRecipes += recipe
        }

        FairyTypes.instance.run {

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
