package miragefairy2019.jei

import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.recipe.IRecipeWrapper
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.mod3.fairystick.itemFairyStick
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft
import miragefairy2019.mod3.fairystickcraft.api.IFairyStickCraftRecipe
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack

@JEIPlugin
class PluginFairyStickCraft : IModPlugin {
    companion object {
        const val uid = "miragefairy2019.fairyStickCraft"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<RecipeWrapperFairyStickCraft> {
            override fun getUid() = Companion.uid
            override fun getTitle() = "Fairy Stick Craft"
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 18 * 9
                override fun getHeight() = 120
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    (0..8).forEach { drawSlot(18f * it, 0f) }
                    minecraft.fontRenderer.drawStringCentered("|", width / 2, 50, 0x444444)
                    minecraft.fontRenderer.drawStringCentered("""\|/""", width / 2, 50 + 10, 0x444444)
                    (0..8).forEach { drawSlot(18f * it, 50f + 20f) }
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(ItemStack(itemFairyStick.get()))
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: RecipeWrapperFairyStickCraft, ingredients: IIngredients) {
                recipeWrapper.listListItemStackInput.forEachIndexed { i, _ ->
                    recipeLayout.itemStacks.init(i, true, 18 * i, 0)
                }
                recipeWrapper.listListItemStackOutput.forEachIndexed { i, _ ->
                    recipeLayout.itemStacks.init(recipeWrapper.listListItemStackInput.size + i, false, 18 * i, 50 + 20)
                }
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(ApiFairyStickCraft.fairyStickCraftRegistry.recipes.toList(), uid)
        registry.handleRecipes(FairyStickCraftRecipe::class.java, { RecipeWrapperFairyStickCraft(registry, it) }, uid)
        registry.addRecipeCatalyst(ItemStack(itemFairyStick.get()), uid)
    }

    class RecipeWrapperFairyStickCraft(registry: IModRegistry, recipe: IFairyStickCraftRecipe) : IRecipeWrapper {
        val listListItemStackInput: List<List<ItemStack>> = registry.jeiHelpers.stackHelper.expandRecipeItemStackInputs(recipe.conditions.flatMap { it.ingredientsInput })
        val listListItemStackOutput: List<List<ItemStack>> = registry.jeiHelpers.stackHelper.expandRecipeItemStackInputs(recipe.conditions.flatMap { it.ingredientsOutput })
        val listStringInput: List<String> = recipe.conditions.flatMap { it.stringsInput }
        val listStringOutput: List<String> = recipe.conditions.flatMap { it.stringsOutput }

        override fun getIngredients(ingredients: IIngredients) {
            ingredients.setInputLists(VanillaTypes.ITEM, listListItemStackInput)
            ingredients.setOutputLists(VanillaTypes.ITEM, listListItemStackOutput)
        }

        override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
            listStringInput.forEachIndexed { i, _ ->
                minecraft.fontRenderer.drawString(listStringInput[i], 0, 18 + 9 * i, 0x444444)
            }
            listStringOutput.forEachIndexed { i, _ ->
                minecraft.fontRenderer.drawString(listStringOutput[i], 0, 50 + 20 + 18 + 9 * i, 0x444444)
            }
        }
    }
}
