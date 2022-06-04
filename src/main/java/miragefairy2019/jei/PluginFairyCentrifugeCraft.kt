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
import miragefairy2019.api.FairyCentrifugeCraftRegistry
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gray
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.fairybox.FairyBox
import mirrg.kotlin.formatAs
import net.minecraft.client.Minecraft

@JEIPlugin
class PluginFairyCentrifugeCraft : IModPlugin {
    companion object {
        private const val uid = "miragefairy2019.fairyCentrifugeCraft"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = translateToLocal("tile.fairyCentrifuge.name")
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 18 * 9
                override fun getHeight() = 18 * 2 + 20
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    repeat(9) { c ->
                        drawSlot(18f * c, 0f)
                        minecraft.fontRenderer.drawStringCentered(formattedText { "IN"().gray }, 18 * c + 9, 9 - 4, 0x000000)
                    }
                    repeat(9) { c ->
                        drawSlot(18f * c, 18f)
                        minecraft.fontRenderer.drawStringCentered(formattedText { "OUT"().gray }, 18 * c + 9, 18 + 9 - 4, 0x000000)
                    }
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(FairyBox.blockFairyCentrifuge().createItemStack())
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                repeat(9) { c -> recipeLayout.itemStacks.init(c, true, 18 * c, 0) }
                repeat(9) { c -> recipeLayout.itemStacks.init(9 + c, false, 18 * c, 18) }
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipeCatalyst(FairyBox.blockFairyCentrifuge().createItemStack(), uid)
        registry.addRecipes(FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers.map { handler ->
            object : IRecipeWrapper {
                override fun getIngredients(ingredients: IIngredients) {
                    ingredients.setInputLists(VanillaTypes.ITEM, handler.inputs.map { input -> registry.jeiHelpers.stackHelper.toItemStackList(input.ingredient).map { it.copy(input.count) } })
                    ingredients.setOutputLists(VanillaTypes.ITEM, handler.outputs.map { output -> listOf(output.itemStack) })
                }

                val drawListeners = mutableListOf<(Minecraft) -> Unit>()
                val tooltipListeners = mutableListOf<Pair<RectangleInt, () -> List<String>>>()

                init {
                    handler.outputs.forEachIndexed next@{ c, output ->
                        if (c >= 9) return@next

                        // 個数
                        run {
                            val rectangle = RectangleInt(18 * c, 18 * 2, 18, 10)
                            drawListeners += { minecraft ->
                                val countPercentage = output.count * 100
                                val string = if (countPercentage < 1.0) countPercentage formatAs "%.1f%%" else countPercentage formatAs "%.0f%%"
                                minecraft.fontRenderer.drawStringCentered(string, rectangle, 0x000000)
                            }
                            tooltipListeners += Pair(rectangle) { listOf(output.count * 100 formatAs "%.6f%%") }
                        }

                        // 幸運係数
                        if (output.fortuneFactor != 0.0) {
                            val rectangle = RectangleInt(18 * c, 18 * 2 + 10, 18, 10)
                            drawListeners += { minecraft ->
                                val color = when {
                                    output.fortuneFactor < 1.0 -> 0x888888
                                    output.fortuneFactor == 1.0 -> 0x000000
                                    else -> 0xFF0000
                                }
                                minecraft.fontRenderer.drawStringCentered("幸運", rectangle, color) // TODO translate
                            }
                            tooltipListeners += Pair(rectangle) { listOf("幸運係数: ${output.fortuneFactor * 100 formatAs "%.6f%%"}") } // TODO translate
                        }

                    }
                }

                override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) = drawListeners.forEach { it(minecraft) }
                override fun getTooltipStrings(mouseX: Int, mouseY: Int) = tooltipListeners.filter { PointInt(mouseX, mouseY) in it.first }.flatMap { it.second() }
            }
        }, uid)
    }
}
