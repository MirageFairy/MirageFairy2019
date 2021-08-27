package miragefairy2019.modkt.impl.mana

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.modkt.api.mana.IManaType
import miragefairy2019.modkt.api.mana.ManaTypes
import net.minecraft.util.text.TextFormatting

fun ModInitializer.init() {
    onInstantiation {
        val values = mutableListOf<IManaType>()
        operator fun String.invoke(color: Int, textColor: TextFormatting): ManaType {
            val manaType = ManaType(this, color, textColor)
            values += manaType
            return manaType
        }

        ManaTypes.shine = "shine"(0xC9FFFF, TextFormatting.WHITE)
        ManaTypes.fire = "fire"(0xCE0000, TextFormatting.RED)
        ManaTypes.wind = "wind"(0x00C600, TextFormatting.GREEN)
        ManaTypes.gaia = "gaia"(0x777700, TextFormatting.YELLOW)
        ManaTypes.aqua = "aqua"(0x0000E2, TextFormatting.BLUE)
        ManaTypes.dark = "dark"(0x191919, TextFormatting.DARK_GRAY)

        ManaTypes.values = values
    }
}


class ManaType(private val name: String, private val color: Int, private val textColor: TextFormatting) : IManaType {
    override fun getName() = name
    override fun getColor() = color
    override fun getTextColor() = textColor
}


val IManaType.displayName get() = text { translate("mirageFairy2019.mana.$name.name").color(textColor) }
