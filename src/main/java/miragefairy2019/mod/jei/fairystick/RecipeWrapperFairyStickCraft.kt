package miragefairy2019.mod.jei.fairystick

import mezz.jei.api.IModRegistry
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import miragefairy2019.mod.common.fairystick.FairyStickCraftRecipe
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack

class RecipeWrapperFairyStickCraft(registry: IModRegistry, recipe: FairyStickCraftRecipe) : IRecipeWrapper {

    @JvmField
    val listListItemStackInput: List<List<ItemStack>> = registry.jeiHelpers.stackHelper.expandRecipeItemStackInputs(recipe.conditions
        .flatMap { condition -> condition.ingredientsInput }
        .toList())

    @JvmField
    val listListItemStackOutput: List<List<ItemStack>> = registry.jeiHelpers.stackHelper.expandRecipeItemStackInputs(recipe.conditions
        .flatMap { condition -> condition.ingredientsOutput }
        .toList())

    @JvmField
    val listStringInput: List<String> = recipe.conditions
        .flatMap { condition -> condition.stringsInput }
        .toList()

    @JvmField
    val listStringOutput: List<String> = recipe.conditions
        .flatMap { condition -> condition.stringsOutput }
        .toList()

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, listListItemStackInput)
        ingredients.setOutputLists(VanillaTypes.ITEM, listListItemStackOutput)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        listStringInput.indices.forEach { i ->
            minecraft.fontRenderer.drawString(
                listStringInput[i],
                0,
                18 + 9 * i,
                0x444444
            )
        }
        listStringOutput.indices.forEach { i ->
            minecraft.fontRenderer.drawString(
                listStringOutput[i],
                0,
                50 + 20 + 18 + 9 * i,
                0x444444
            )
        }
    }

}
