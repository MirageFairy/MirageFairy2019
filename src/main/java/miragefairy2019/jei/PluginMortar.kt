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
import miragefairy2019.api.MortarRecipeRegistry
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.fairy.pedestal.MortarVariant
import miragefairy2019.mod.fairy.pedestal.itemBlockMortar
import net.minecraft.client.Minecraft

@JEIPlugin
class PluginMortar : IModPlugin {
    companion object {
        private const val uid = "miragefairy2019.mortar"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = translateToLocal("jei.$uid.title")
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 18 + 4 + 18 + 4 + 18
                override fun getHeight() = 18 * 1
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    drawSlot(0f, 0f)
                    drawSlot(18f + 4f, 0f)
                    drawSlot(18f + 4f + 18f + 4f, 0f)
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(itemBlockMortar().createItemStack(metadata = MortarVariant.DIAMOND.metadata))
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 0, 0)
                recipeLayout.itemStacks.init(1, true, 18 + 4, 0)
                recipeLayout.itemStacks.init(2, false, 18 + 4 + 18 + 4, 0)
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(MortarRecipeRegistry.mortarRecipes.map { recipe ->
            IRecipeWrapper { ingredients ->
                val inputList = registry.jeiHelpers.stackHelper.toItemStackList(recipe.input)
                val machineList = MortarVariant.values().filter { it.level >= recipe.level }.map { itemBlockMortar().createItemStack(metadata = it.metadata) }
                ingredients.setInputLists(VanillaTypes.ITEM, listOf(inputList, machineList))
                ingredients.setOutputLists(VanillaTypes.ITEM, listOf(listOf(recipe.output)))
            }
        }, uid)
    }
}
