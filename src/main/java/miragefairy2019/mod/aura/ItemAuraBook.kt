package miragefairy2019.mod.aura

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.displayName
import miragefairy2019.lib.textColor
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.withColor
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemAuraBook : Item() {
    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val data = auraManager.getClientData()

        tooltip += formattedText { "オーラ影響力: ${data.auraSet.getPower() formatAs "%.2f"}"().aqua } // TRANSLATE

        tooltip += formattedText { "オーラ蓄積値:"() } // TRANSLATE

        Mana.values().toList().chunked(3).forEach { manas ->
            tooltip += formattedText {
                "  "() + manas.map { mana ->
                    (mana.displayName() + ": ${data.auraSet[mana] formatAs "%5.0f"}"()).withColor(mana.textColor)
                }.sandwich { "  "() }.flatten()
            }
        }

        Erg.values().toList().chunked(3).forEach { ergs ->
            tooltip += formattedText {
                "  "() + ergs.map { erg ->
                    (erg.displayName() + ": ${data.auraSet[erg] formatAs "%5.0f"}"()).withColor(erg.textColor)
                }.sandwich { "  "() }.flatten()
            }
        }

    }
}
