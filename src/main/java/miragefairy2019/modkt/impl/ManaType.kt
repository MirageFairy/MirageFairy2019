package miragefairy2019.modkt.impl

import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.modkt.api.IManaType
import net.minecraft.util.text.TextFormatting

class ManaType(private val name: String, private val textColor: TextFormatting) : IManaType {
    companion object {
        val shine = ManaType("shine", TextFormatting.WHITE)
        val fire = ManaType("fire", TextFormatting.RED)
        val wind = ManaType("wind", TextFormatting.GREEN)
        val gaia = ManaType("gaia", TextFormatting.YELLOW)
        val aqua = ManaType("aqua", TextFormatting.BLUE)
        val dark = ManaType("dark", TextFormatting.DARK_GRAY)
    }

    override fun getName() = name
    override fun getTextColor() = textColor
}


val IManaType.displayName get() = text { translate("mirageFairy2019.mana.$name.name").color(textColor) }
