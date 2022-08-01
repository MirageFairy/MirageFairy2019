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
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.recipes.Mfa
import net.minecraft.client.Minecraft
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

@JEIPlugin
class PluginMfa : IModPlugin {
    companion object {
        private const val uid = "miragefairy2019.mfa"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = translateToLocal("jei.$uid.title")
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 160
                override fun getHeight() = 120
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    drawSlot(1f, 1f)
                    repeat(7) { drawSlot(33f + 18f * it, 1f) }
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(ItemStack(Items.WRITABLE_BOOK))
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 1, 1)
                repeat(7) { recipeLayout.itemStacks.init(1 + it, true, 33 + 18 * it, 1) }
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(Mfa.mfaPages.map { mfaPage ->
            object : IRecipeWrapper {
                override fun getIngredients(ingredients: IIngredients) {
                    ingredients.setInputLists(VanillaTypes.ITEM, listOf(mfaPage.mainIngredient) + (0 until 7).map { mfaPage.subIngredients.getOrNull(it) ?: listOf() })
                }

                override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
                    if (canTranslate(mfaPage.unlocalizedContent)) {
                        translateToLocal(mfaPage.unlocalizedContent).split("\\n").forEachIndexed { i, it ->
                            minecraft.fontRenderer.drawString(it, 4, 21 + 10 * i, 0x444444)
                        }
                    } else {
                        // TRANSLATE
                        minecraft.fontRenderer.drawString("The fairy laboratory failed", 4, 21 + 10 * 0, 0x444444)
                        minecraft.fontRenderer.drawString("to decipher this article.", 4, 21 + 10 * 1, 0x444444)
                    }
                }
            }
        }, uid)
    }
}
