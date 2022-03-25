package miragefairy2019.mod3.recipe

import miragefairy2019.libkt.copy
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.red
import mirrg.kotlin.toUpperCamelCase
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AnvilUpdateEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object AnvilPulverizationRecipes {
    val module = module {
        onInit {
            fun r(ingredient: Ingredient, outputOreName: String) {
                MinecraftForge.EVENT_BUS.register(object {
                    @SubscribeEvent
                    fun handle(event: AnvilUpdateEvent) {
                        if (!ingredient.test(event.left)) return
                        if (!ingredient.test(event.right)) return
                        val count = event.left.count + event.right.count
                        if (count > 64) return
                        val outputItemStack = outputOreName.oreIngredient.matchingStacks.getOrNull(0) ?: return
                        event.output = outputItemStack.copy(count)
                        event.cost = count
                    }

                    @SideOnly(Side.CLIENT)
                    @SubscribeEvent
                    fun handle(event: ItemTooltipEvent) {

                        // 入力アイテムへのツールチップ
                        if (outputOreName.oreIngredient.matchingStacks.isNotEmpty()) {
                            if (ingredient.test(event.itemStack)) {
                                event.toolTip += formattedText { (!"金床で同アイテムを組み合わせて粉砕可能").red } // TODO translate
                            }
                        }

                        // 出力アイテムへのツールチップ
                        if (ingredient.matchingStacks.isNotEmpty()) {
                            if (outputOreName.oreIngredient.test(event.itemStack)) {
                                event.toolTip += formattedText { (!"金床で${ingredient.matchingStacks[0].displayName}を組み合わせて入手可能").red } // TODO translate TODO 例外処理
                            }
                        }

                    }
                })
            }

            fun r(materialName: String) = r("gem${materialName.toUpperCamelCase()}".oreIngredient, "dust${materialName.toUpperCamelCase()}")

            r("coal")
            r("charcoal")
            r("diamond")
            r("lapis")
            r("emerald")
            r("quartz")
            r("enderpearl".oreIngredient, "dustEnderPearl")

            r("apatite")
            r("fluorite")
            r("sulfur")
            r("cinnabar")
            r("moonstone")
            r("magnetite")

            r("saltpeter")
            r("pyrope")
            r("smithsonite")
            r("nephrite")
            r("topaz")
            r("tourmaline")
            r("heliolite")
            r("labradorite")
            r("peridot")
            r("ruby")
            r("sapphire")

        }
    }
}
