package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.mod.artifacts.ingredientData
import mirrg.kotlin.hydrogen.unit

val miniaRecipe = module {

    class PatternBuilder {
        val list = mutableListOf<String>()
        operator fun String.not() = unit { list += this }
    }

    fun register(name: String, output: DataResult, inputRank: Int, pattern: PatternBuilder.() -> Unit) = makeRecipe("minia/${name}_from_minia") {
        DataShapedRecipe(
            pattern = PatternBuilder().also { pattern(it) }.list,
            key = mapOf(
                "s" to miragefairy2019.mod.artifacts.WandType.SUMMONING.ingredientData,
                "#" to DataOreIngredient(ore = "mirageFairy2019FairyMinaRank$inputRank")
            ),
            result = output
        )
    }

    register("cobblestone", DataResult(item = "minecraft:cobblestone"), 2) { // 丸石
        !"#  "
        !" s "
        !"   "
    }
    register("charcoal", DataResult(item = "minecraft:coal", data = 1), 3) { // 木炭
        !"   "
        !" s "
        !"  #"
    }
    register("iron_ingot", DataResult(item = "minecraft:iron_ingot"), 3) { // 鉄
        !"#  "
        !" s "
        !"   "
    }
    register("gold_ingot", DataResult(item = "minecraft:gold_ingot"), 3) { // 金
        !"## "
        !" s "
        !"   "
    }
    register("magnetite", DataResult(item = "miragefairy2019:materials", data = 8), 5) { // 磁鉄鉱
        !"   "
        !" s "
        !" # "
    }
    register("sulfur", DataResult(item = "miragefairy2019:materials", data = 2), 5) { // 硫黄
        !"   "
        !" s "
        !"## "
    }
    register("apatite", DataResult(item = "miragefairy2019:materials", data = 0), 5) { // 燐灰石
        !"   "
        !" s "
        !" ##"
    }
    register("cinnabar", DataResult(item = "miragefairy2019:materials", data = 6), 5) { // 辰砂
        !"#  "
        !"#s "
        !"## "
    }
    register("fluorite", DataResult(item = "miragefairy2019:materials", data = 1), 5) { // 蛍石
        !"  #"
        !" s#"
        !" ##"
    }
    register("moonstone", DataResult(item = "miragefairy2019:materials", data = 7), 5) { // 月長石
        !"###"
        !"#s#"
        !"###"
    }

}
