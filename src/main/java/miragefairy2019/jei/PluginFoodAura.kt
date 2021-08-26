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
import miragefairy2019.jei.JeiUtilities.Companion.drawStringCentered
import miragefairy2019.libkt.getSubItems
import miragefairy2019.modkt.api.IManaType
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura
import miragefairy2019.modkt.impl.ManaType
import miragefairy2019.modkt.impl.getMana
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack

@JEIPlugin
class PluginFoodAura : IModPlugin {
    companion object {
        const val uid = "miragefairy2019.foodAura"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = "Food Aura"
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 160
                override fun getHeight() = 20
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    JeiUtilities.drawSlot(1f, 1f)
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(ItemStack(Items.CAKE))
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 1, 1)
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(mutableListOf<IRecipeWrapper>().apply {

            val registered = mutableSetOf<ItemStack>()
            CreativeTabs.CREATIVE_TAB_ARRAY.forEach { creativeTab ->
                Item.REGISTRY.forEach { item ->
                    (item as Item/* これをしないと謎レシーバ解決不能がCI上で発生する */).getSubItems(creativeTab).forEach { itemStack ->

                        if (itemStack.item !is ItemFood) return@forEach // 食べ物以外は無視

                        val foodAura = ApiPlayerAura.playerAuraManager.getGlobalFoodAura(itemStack) ?: return@forEach // オーラが登録されていない食べ物は無視

                        if (registered.any { ItemStack.areItemStacksEqualUsingNBTShareTag(itemStack, it) }) return@forEach // 既に登録済みの食べ物は無視
                        registered += itemStack

                        // 登録
                        add(object : IRecipeWrapper {
                            override fun getIngredients(ingredients: IIngredients) = ingredients.setInputLists(VanillaTypes.ITEM, listOf(listOf(itemStack)))
                            override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
                                fun d(manaType: IManaType, x: Int) {
                                    val value = foodAura.getMana(manaType)
                                    Gui.drawRect(x + 2, (20 - 20 * value / 50.0).toInt(), x + 18, 20, manaType.color or 0xFF000000.toInt())
                                    val string = String.format("%.0f", value)
                                    val stringWidth = minecraft.fontRenderer.getStringWidth(string)
                                    Gui.drawRect(x + 10 - stringWidth / 2 - 1, 10, x + 10 + stringWidth / 2 + 1, 18, 0xFFC6C6C6.toInt())
                                    minecraft.fontRenderer.drawStringCentered(string, x + 10, 10, manaType.color)
                                }
                                d(ManaType.shine, 30)
                                d(ManaType.fire, 50)
                                d(ManaType.wind, 70)
                                d(ManaType.gaia, 90)
                                d(ManaType.aqua, 110)
                                d(ManaType.dark, 130)
                            }
                        })

                    }
                }
            }

        }, uid)
    }
}
