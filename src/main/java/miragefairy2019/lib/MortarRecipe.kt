package miragefairy2019.lib

import miragefairy2019.api.IMortarRecipe
import miragefairy2019.api.MortarRecipeRegistry
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient

class MortarRecipe(private val level: Int, private val input: Ingredient, private val output: () -> ItemStack) : IMortarRecipe {
    override fun getLevel() = level
    override fun getInput() = input
    override fun getOutput() = output()
}

fun getMortarRecipe(machineLevel: Int, inputItemStack: ItemStack?): IMortarRecipe? {
    if (inputItemStack == null) return null
    if (inputItemStack.isEmpty) return null
    if (inputItemStack.count > 1) return null
    return MortarRecipeRegistry.mortarRecipes.firstOrNull { it.level <= machineLevel && it.input.test(inputItemStack) }
}
