package miragefairy2019.modkt.impl

import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.modkt.api.IManaType
import net.minecraft.util.text.TextFormatting

class ManaType(private val name: String, private val color: Int, private val textColor: TextFormatting) : IManaType {
    companion object {
        val shine = ManaType("shine", 0xC9FFFF, TextFormatting.WHITE)
        val fire = ManaType("fire", 0xCE0000, TextFormatting.RED)
        val wind = ManaType("wind", 0x00C600, TextFormatting.GREEN)
        val gaia = ManaType("gaia", 0x777700, TextFormatting.YELLOW)
        val aqua = ManaType("aqua", 0x0000E2, TextFormatting.BLUE)
        val dark = ManaType("dark", 0x191919, TextFormatting.DARK_GRAY)
    }

    override fun getName() = name
    override fun getColor() = color
    override fun getTextColor() = textColor
}


val IManaType.displayName get() = text { translate("mirageFairy2019.mana.$name.name").color(textColor) }
