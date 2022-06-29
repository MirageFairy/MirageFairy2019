package miragefairy2019.lib

import miragefairy2019.api.IMortarRecipe
import miragefairy2019.api.IMortarRecipeHandler
import miragefairy2019.api.MortarRecipeRegistry
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient

class MortarRecipeHandler(private val level: Int, private val input: Ingredient, private val outputSupplier: () -> ItemStack?) : IMortarRecipeHandler {
    private val _recipes by lazy {
        val output = outputSupplier() ?: return@lazy listOf<IMortarRecipe>().toNonNullList()
        listOf<IMortarRecipe>(MortarRecipe(level, input, output)).toNonNullList()
    }

    override fun getRecipes() = _recipes
}

class MortarRecipe(private val level: Int, private val input: Ingredient, private val output: ItemStack) : IMortarRecipe {
    override fun getLevel() = level
    override fun getInput() = input
    override fun getOutput() = output
}

fun getMortarRecipe(machineLevel: Int, inputItemStack: ItemStack?): IMortarRecipe? {
    if (inputItemStack == null) return null
    if (inputItemStack.isEmpty) return null
    if (inputItemStack.count > 1) return null
    val recipes: Sequence<IMortarRecipe> = MortarRecipeRegistry.mortarRecipeHandlers.asSequence().flatMap { it.recipes.asSequence() }
    return recipes.firstOrNull { it.level <= machineLevel && it.input.test(inputItemStack) }
}
