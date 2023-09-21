package miragefairy2019.jei

import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.recipe.IRecipeWrapper
import miragefairy2019.api.Erg
import miragefairy2019.lib.displayName
import miragefairy2019.lib.get
import miragefairy2019.lib.textColor
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.shrink
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.withColor
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.createItemStack
import miragefairy2019.mod.fairy.maxRank
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.item.ItemStack

@JEIPlugin
class PluginErgResistance : IModPlugin {
    companion object {
        private const val uid = "miragefairy2019.ergResistance"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = translateToLocal("jei.$uid.title")
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 180
                override fun getHeight() = 10 * ((Erg.values().size - 1) / 8 + 1)
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    Gui.drawRect(20, 0, width, height, 0xF0100010.toInt())
                    drawSlot(1f, (height / 2 - 10).toFloat() + 1f)
                }
            }

            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 1, 1 + background.height / 2 - 10)
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(FairyCard.values().map { fairyCard ->
            object : IRecipeWrapper {
                override fun getIngredients(ingredients: IIngredients) {
                    ingredients.setInputLists(ItemStack::class.java, listOf((1..maxRank).map { fairyCard.createItemStack(rank = it) }))
                }

                val drawListeners = mutableListOf<(Minecraft) -> Unit>()
                val tooltipListeners = mutableListOf<Pair<RectangleInt, () -> List<String>>>()

                init {
                    Erg.values().forEachIndexed { i, erg ->
                        val value = fairyCard.rawErgSet[erg]
                        val rectangle = RectangleInt(20 + 20 * (i % 8), 0 + 10 * (i / 8), 20, 10)
                        drawListeners += { minecraft ->
                            minecraft.fontRenderer.drawStringCentered(formattedText { (value formatAs "%.0f")().withColor(erg.textColor) }, rectangle, 0x444444)
                        }
                        tooltipListeners += Pair(rectangle) { listOf(formattedText { erg.displayName() }) }
                    }
                }

                override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) = drawListeners.forEach { it(minecraft) }
                override fun getTooltipStrings(mouseX: Int, mouseY: Int) = tooltipListeners.filter { PointInt(mouseX, mouseY) in it.first.shrink(0, 0, 1, 1) }.flatMap { it.second() }
            }
        }, uid)
    }
}
