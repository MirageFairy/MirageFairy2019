package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.mod3.fairylogdrop.FairyLogDropConditionCanRain
import miragefairy2019.mod3.fairylogdrop.FairyLogDropConditionHasBiomeType
import miragefairy2019.mod3.fairylogdrop.FairyLogDropConditionOverworld
import miragefairy2019.mod3.fairylogdrop.FairyLogDropRecipe
import miragefairy2019.mod3.worldgen.api.ApiWorldGen
import net.minecraftforge.common.BiomeDictionary

val loaderFairyLogDrop: Module = {
    onCreateItemStack {
        fun FairyLogDropRecipe.biome(biome: BiomeDictionary.Type) = also { addCondition(FairyLogDropConditionHasBiomeType(biome)) }
        fun FairyLogDropRecipe.overworld() = also { addCondition(FairyLogDropConditionOverworld()) }
        fun FairyLogDropRecipe.canRain() = also { addCondition(FairyLogDropConditionCanRain()) }

        operator fun RankedFairyTypeBundle.invoke(weight: Double, block: FairyLogDropRecipe.() -> Unit) {
            val recipe = FairyLogDropRecipe(weight) { main.createItemStack() }
            recipe.block()
            ApiWorldGen.fairyLogDropRegistry.addRecipe(recipe)
        }

        FairyTypes.instance.run {

            // 時間帯
            daytime(0.1) { overworld() }
            night(0.1) { overworld() }
            morning(0.1) { overworld() }

            // 天候
            fine(0.1) { overworld() }
            rain(0.1) { overworld().canRain() }
            thunder(0.02) { overworld().canRain() }

            // バイオーム
            plains(1.0) { biome(BiomeDictionary.Type.PLAINS) }
            forest(1.0) { biome(BiomeDictionary.Type.FOREST) }
            taiga(1.0) { biome(BiomeDictionary.Type.CONIFEROUS) }
            desert(1.0) { biome(BiomeDictionary.Type.SANDY) }
            mountain(1.0) { biome(BiomeDictionary.Type.MOUNTAIN) }

        }
    }
}
