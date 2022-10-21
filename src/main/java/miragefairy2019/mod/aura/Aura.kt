package miragefairy2019.mod.aura

import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.proxy
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.formattedText
import miragefairy2019.mod.Main
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.hydrogen.floorToInt
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.log

lateinit var itemAuraBook: () -> ItemAuraBook

val auraModule = module {

    auraManager.module(this)

    // オーラ検査キット
    itemAuraBook = item({ ItemAuraBook() }, "aura_book") {
        setUnlocalizedName("auraBook")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        makeItemModel { generated }
        makeRecipe {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataSimpleIngredient(item = "minecraft:writable_book"),
                    DataOreIngredient(ore = "mirageFairyCrystal"),
                    DataOreIngredient(ore = "dustMiragium"),
                    DataOreIngredient(ore = "gemQuartz")
                ),
                result = DataResult(item = "miragefairy2019:aura_book")
            )
        }
    }
    lang("item.auraBook.name", "Aura Test Kit", "オーラ検査キット")

    onInit {
        MinecraftForge.EVENT_BUS.register(object {

            // 食べ物を食べた処理
            @Suppress("unused")
            @SubscribeEvent
            fun hook(event: LivingEntityUseItemEvent.Finish) {
                if (event.entity.world.isRemote) return
                val player = event.entityLiving as? EntityPlayerMP ?: return
                val auraData = player.proxy.auraData
                val itemAuraSet = event.item.getAuraSet() ?: return
                auraData.auraSet += itemAuraSet
                auraManager.send(player)
            }

            // 食べ物のツールチップ
            @Suppress("unused")
            @SubscribeEvent
            @SideOnly(Side.CLIENT)
            fun hook(event: ItemTooltipEvent) {
                val player = event.entityPlayer ?: return // なぜかクライアント起動時に呼び出される
                val auraData = player.proxy.auraData
                val itemAuraSet = event.itemStack.getAuraSet() ?: return
                val oldPower = auraData.auraSet.getPower()
                val newPower = (auraData.auraSet + itemAuraSet).getPower()
                val powerDiff = newPower - oldPower
                event.toolTip += formattedText { "オーラ影響力: ${format(powerDiff)}"().aqua } // TRANSLATE
            }

        })
    }

}

private fun format(value: Double): String {
    if (value == 0.0) return value formatAs "%+.0f"
    val a = 1 - log(value, 10.0).floorToInt() atLeast 0 atMost 10
    return value formatAs "%+.${a}f"
}
