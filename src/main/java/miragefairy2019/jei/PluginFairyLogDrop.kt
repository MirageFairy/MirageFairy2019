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
import miragefairy2019.api.FairyLogDropRegistry
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.artifacts.itemBlockFairyLog
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack

@JEIPlugin
class PluginFairyLogDrop : IModPlugin {
    companion object {
        private const val uid = "miragefairy2019.fairyLogDrop"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = translateToLocal("jei.$uid.title")
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 18 * 9
                override fun getHeight() = 18 * 1
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    drawSlot(0f, 0f)
                    drawSlot(45f, 0f)
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(ItemStack(itemBlockFairyLog()))
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 0, 0)
                recipeLayout.itemStacks.init(1, false, 45, 0)
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(FairyLogDropRegistry.fairyLogDropRecipes.map { recipe ->
            object : IRecipeWrapper {
                override fun getIngredients(ingredients: IIngredients) {
                    ingredients.setInput(VanillaTypes.ITEM, ItemStack(itemBlockFairyLog()))
                    ingredients.setOutput(VanillaTypes.ITEM, recipe.output)
                }

                override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
                    val descriptions = recipe.requirements.chunked(2).map { it.joinToString(", ") { condition -> condition.description.unformattedText } }
                    minecraft.fontRenderer.drawStringRightAligned(String.format("%.0f%%", recipe.rate * 100), 45 - 2, 4, 0x444444)
                    minecraft.fontRenderer.drawString(recipe.output.displayName, 65, 0, 0x444444)
                    descriptions.forEachIndexed { i, it -> minecraft.fontRenderer.drawString(it, 65, 10 + 10 * i, 0x444444) }
                }
            }
        }, uid)
    }
}
