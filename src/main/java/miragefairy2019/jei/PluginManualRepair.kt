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
import miragefairy2019.libkt.getSubItems
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.manualrepair.api.IManualRepairableItem
import miragefairy2019.mod3.sphere.get
import miragefairy2019.mod3.sphere.itemSpheres
import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

@JEIPlugin
class PluginManualRepair : IModPlugin {
    companion object {
        const val uid = "miragefairy2019.manualRepair"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = "Manual Repair"
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 1 + 18 + 4 + 18 * 8 + 1
                override fun getHeight() = 20
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    drawSlot(1f, 1f)
                    repeat(8) { drawSlot(1f + 18f + 4f + 18f * it, 1f) }
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(itemSpheres[EnumErgType.CRAFT])
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 1, 1)
                repeat(8) { recipeLayout.itemStacks.init(1 + it, true, 1 + 18 + 4 + 18 * it, 1) }
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(mutableListOf<IRecipeWrapper>().apply {

            val registered = mutableSetOf<ItemStack>()
            CreativeTabs.CREATIVE_TAB_ARRAY.forEach { creativeTab ->
                Item.REGISTRY.forEach { item ->
                    (item as Item/* これをしないと謎レシーバ解決不能がCI上で発生する */).getSubItems(creativeTab).forEach a@{ itemStack ->
                        if (item !is IManualRepairableItem) return@a // 手動修理不可の場合無視
                        if (!item.canManualRepair(itemStack)) return@a // 手動修理不可の場合無視
                        if (registered.any { ItemStack.areItemStacksEqualUsingNBTShareTag(itemStack, it) }) return@a // 既に登録済みなら無視

                        registered += itemStack
                        add(object : IRecipeWrapper {
                            override fun getIngredients(ingredients: IIngredients) {
                                val inputTarget = listOf(listOf(itemStack))
                                val inputSubstitute = registry.jeiHelpers.stackHelper.expandRecipeItemStackInputs(item.getManualRepairSubstitute(itemStack))
                                ingredients.setInputLists(VanillaTypes.ITEM, listOf(inputTarget, inputSubstitute).flatten())
                            }

                            override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) = Unit
                        })
                    }
                }
            }

        }, uid)
    }
}
