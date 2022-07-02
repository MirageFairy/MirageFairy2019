package miragefairy2019.lib

import miragefairy2019.api.Erg
import miragefairy2019.api.FairyCentrifugeCraftRegistry
import miragefairy2019.api.IFairyCentrifugeCraftArguments
import miragefairy2019.api.IFairyCentrifugeCraftHandler
import miragefairy2019.api.IFairyCentrifugeCraftInput
import miragefairy2019.api.IFairyCentrifugeCraftOutput
import miragefairy2019.api.IFairyCentrifugeCraftProcess
import miragefairy2019.api.IFairyCentrifugeCraftRecipe
import miragefairy2019.api.Mana
import miragefairy2019.libkt.containerItem
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.randomInt
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import java.util.Random

fun getFairyCentrifugeCraftRecipe(inventory: IInventory) = FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers.asSequence().mapNotNull { it.test(inventory) }.firstOrNull()


class FairyCentrifugeCraftHandlerScope {
    val processes = mutableListOf<IFairyCentrifugeCraftProcess>()

    class FairyCentrifugeCraftScoreScope(val arguments: IFairyCentrifugeCraftArguments) {
        operator fun Mana.not() = arguments.getMana(this)
        operator fun Erg.not() = arguments.getErg(this)
    }

    fun process(scoreFunction: FairyCentrifugeCraftScoreScope.() -> Double) {
        processes += IFairyCentrifugeCraftProcess { arguments -> FairyCentrifugeCraftScoreScope(arguments).scoreFunction() }
    }


    class Input(val ingredient: Ingredient, val count: Int)

    val inputs = mutableListOf<Input>()

    fun input(ingredient: Ingredient, count: Int) {
        inputs += Input(ingredient, count)
    }


    class Output(val itemStack: ItemStack, val count: Double, val fortuneFactor: Double) {
        init {
            require(!itemStack.isEmpty)
            require(itemStack.count == 1)
        }
    }

    val outputs = mutableListOf<Output>()

    fun output(itemStack: ItemStack, count: Double, fortuneFactor: Double = 0.0) {
        if (!itemStack.isEmpty) outputs += Output(itemStack, count, fortuneFactor)
    }


    class Canceled : Throwable()

    fun cancel(): Nothing = throw Canceled()
}

fun fairyCentrifugeCraftHandler(norma: Double, block: FairyCentrifugeCraftHandlerScope.() -> Unit) {
    val scope = FairyCentrifugeCraftHandlerScope()
    try {
        scope.block()
    } catch (_: FairyCentrifugeCraftHandlerScope.Canceled) {
        return
    }
    require(scope.processes.size == 3)
    require(scope.inputs.size >= 1)
    require(scope.outputs.size >= 1)
    class FairyCentrifugeCraftHandler : IFairyCentrifugeCraftHandler {
        override fun getNorma() = norma

        override fun getInputs(): NonNullList<IFairyCentrifugeCraftInput> = scope.inputs.map { input ->
            object : IFairyCentrifugeCraftInput {
                override fun getIngredient() = input.ingredient
                override fun getCount() = input.count
            }
        }.toNonNullList()

        override fun getOutputs(): NonNullList<IFairyCentrifugeCraftOutput> = scope.outputs.map { output ->
            object : IFairyCentrifugeCraftOutput {
                override fun getItemStack() = output.itemStack
                override fun getCount() = output.count
                override fun getFortuneFactor() = output.fortuneFactor
            }
        }.toNonNullList()

        override fun test(inventory: IInventory): IFairyCentrifugeCraftRecipe? {
            val matcher = RecipeMatcher(inventory)

            // 素材判定
            val recipeInputs = scope.inputs.map { input ->
                matcher.pull {
                    if (input.ingredient.test(it) && it.count >= input.count) input else null
                } ?: return null
            }

            return object : IFairyCentrifugeCraftRecipe {
                override fun getHandler() = this@FairyCentrifugeCraftHandler

                override fun getProcess(index: Int) = when (index) {
                    0 -> scope.processes[0]
                    1 -> scope.processes[1]
                    2 -> scope.processes[2]
                    else -> null
                }

                override fun craft(random: Random, fortune: Double): NonNullList<ItemStack>? {

                    // 素材判定
                    recipeInputs.forEach {
                        if (inventory[it.index].count < it.tag.count) return null // 足りなくなったら失敗
                    }

                    // 消費
                    recipeInputs.forEach {
                        inventory[it.index] = inventory[it.index].copy()
                        inventory[it.index].shrink(it.tag.count) // 減らす
                    }

                    val outputItemStacks = mutableListOf<ItemStack>()

                    // 成果物判定
                    scope.outputs.forEach { output ->
                        var outputCount = random.randomInt(output.count * (1.0 + output.fortuneFactor * fortune))
                        while (outputCount > 0) {
                            val count = outputCount atMost output.itemStack.maxStackSize
                            outputItemStacks += output.itemStack.copy(count)
                            outputCount -= count
                        }
                    }

                    // コンテナ返却
                    recipeInputs.forEach { input ->
                        repeat(input.tag.count) {
                            val containerItemStack = input.itemStack.copy(1).containerItem
                            if (containerItemStack != null) outputItemStacks += containerItemStack
                        }
                    }

                    return outputItemStacks.toNonNullList()
                }
            }
        }
    }
    FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += FairyCentrifugeCraftHandler()
}
