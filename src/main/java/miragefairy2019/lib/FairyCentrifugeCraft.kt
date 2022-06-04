package miragefairy2019.lib

import miragefairy2019.api.Erg
import miragefairy2019.api.FairyCentrifugeCraftRegistry
import miragefairy2019.api.IFairyCentrifugeCraftArguments
import miragefairy2019.api.IFairyCentrifugeCraftHandler
import miragefairy2019.api.IFairyCentrifugeCraftProcess
import miragefairy2019.api.IFairyCentrifugeCraftRecipe
import miragefairy2019.api.Mana
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.textComponent
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.text.ITextComponent
import java.util.Random

fun getFairyCentrifugeCraftRecipe(inventory: IInventory) = FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers.asSequence().mapNotNull { it.test(inventory) }.firstOrNull()

fun fairyCentrifugeCraftHandler(
    process0: IFairyCentrifugeCraftProcess?,
    process1: IFairyCentrifugeCraftProcess?,
    process2: IFairyCentrifugeCraftProcess?,
    outputGetter: () -> ItemStack?,
    fortuneFactor: Double,
    ingredientEntry0: Pair<Ingredient, Int>,
    ingredientEntry1: Pair<Ingredient, Int>? = null
): IFairyCentrifugeCraftHandler {
    val ingredientEntries = listOfNotNull(ingredientEntry0, ingredientEntry1)
    return object : IFairyCentrifugeCraftHandler {
        override fun test(inventory: IInventory): IFairyCentrifugeCraftRecipe? {
            val matcher = RecipeMatcher(inventory)

            // 素材判定
            val recipeInputs = ingredientEntries.map { ingredientEntry ->
                matcher.pull {
                    if (ingredientEntry.first.test(it) && it.count >= ingredientEntry.second) ingredientEntry else null
                } ?: return null
            }

            val output = outputGetter()?.orNull ?: return null

            return object : IFairyCentrifugeCraftRecipe {
                override fun getProcess(index: Int) = when (index) {
                    0 -> process0
                    1 -> process1
                    2 -> process2
                    else -> null
                }

                override fun craft(random: Random, fortune: Double): NonNullList<ItemStack>? {

                    // 素材判定
                    recipeInputs.forEach {
                        if (inventory[it.index].count < it.tag.second) return null // 足りなくなったら失敗
                    }

                    // 消費
                    recipeInputs.forEach {
                        inventory[it.index] = inventory[it.index].copy()
                        inventory[it.index].shrink(it.tag.second) // 減らす
                    }

                    // 成果物判定
                    val outputCount = random.randomInt((1.0 + fortuneFactor * fortune) * output.count) atMost 64 // TODO 溢れた場合のアイテム分割
                    val outputItemStack = output.copy(outputCount)

                    return listOf(outputItemStack).toNonNullList()
                }
            }
        }
    }
}

class FairyCentrifugeCraftScoreScope(val arguments: IFairyCentrifugeCraftArguments) {
    operator fun Mana.not() = arguments.getMana(this)
    operator fun Erg.not() = arguments.getErg(this)
}

fun process(name: String, norma: Double, scoreFunction: FairyCentrifugeCraftScoreScope.() -> Double) = object : IFairyCentrifugeCraftProcess {
    override fun getName() = textComponent { name() } // TODO translate
    override fun getNorma() = norma
    override fun getScore(arguments: IFairyCentrifugeCraftArguments) = FairyCentrifugeCraftScoreScope(arguments).scoreFunction()
}

val IFairyCentrifugeCraftProcess.factors: List<ITextComponent>
    get() {
        val factors = mutableListOf<ITextComponent>()
        getScore(object : IFairyCentrifugeCraftArguments {
            override fun getMana(mana: Mana) = 0.0.also { factors.add(mana.displayName) }
            override fun getErg(erg: Erg) = 0.0.also { factors.add(erg.displayName) }
        })
        return factors
    }
