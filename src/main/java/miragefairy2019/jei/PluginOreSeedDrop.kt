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
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.artifacts.EnumVariantOre1
import miragefairy2019.mod.artifacts.Ores
import miragefairy2019.mod.artifacts.oreseed.ApiOreSeedDrop
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack

@JEIPlugin
class PluginOreSeedDrop : IModPlugin {
    companion object {
        const val uid = "miragefairy2019.oreSeedDrop"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = translateToLocal("jei.$uid.title")
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 160
                override fun getHeight() = 80
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    drawSlot(20f, 0f)
                    drawSlot(120f, 0f)
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(ItemStack(Ores.blockOre1(), 1, EnumVariantOre1.APATITE_ORE.metadata))
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 20, 0)
                recipeLayout.itemStacks.init(1, false, 120, 0)
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(ApiOreSeedDrop.oreSeedDropRegistry.recipes.map { recipe ->
            object : IRecipeWrapper {
                override fun getIngredients(ingredients: IIngredients) {
                    ingredients.setInputLists(VanillaTypes.ITEM, listOf(listOf(recipe.getType().getItemStack())))
                    ingredients.setOutputLists(VanillaTypes.ITEM, listOf(recipe.getOutputItemStacks().toList()))
                }

                override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
                    minecraft.fontRenderer.drawStringCentered(String.format("%.0f%%", recipe.getWeight() * 100), 80, 0, 0x444444)
                    minecraft.fontRenderer.drawStringCentered(recipe.getShape().name, 80, 10, 0x444444)
                    recipe.getRequirements().forEachIndexed { i, it -> minecraft.fontRenderer.drawString(it, 0, 20 + 10 * i, 0x444444) }
                }
            }
        }.toList(), uid)
    }
}
