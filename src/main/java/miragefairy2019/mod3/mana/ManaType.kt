package miragefairy2019.mod3.mana

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod3.mana.api.IManaType
import miragefairy2019.mod3.mana.api.ManaTypes
import net.minecraft.util.text.TextFormatting

val moduleMana: Module = {
    onInstantiation {
        val values = mutableListOf<IManaType>()
        operator fun String.invoke(color: Int, textColor: TextFormatting): ManaType {
            val manaType = ManaType(this, color, textColor)
            values += manaType
            return manaType
        }

        ManaTypes.shine = "shine"(0x007068, TextFormatting.WHITE)
        ManaTypes.fire = "fire"(0xFF0000, TextFormatting.RED)
        ManaTypes.wind = "wind"(0x107A00, TextFormatting.GREEN)
        ManaTypes.gaia = "gaia"(0x664E00, TextFormatting.YELLOW)
        ManaTypes.aqua = "aqua"(0x3535FF, TextFormatting.BLUE)
        ManaTypes.dark = "dark"(0xB000D3, TextFormatting.DARK_GRAY)

        ManaTypes.values = values
    }
}


class ManaType(private val name: String, private val color: Int, private val textColor: TextFormatting) : IManaType {
    override fun getName() = name
    override fun getColor() = color
    override fun getTextColor() = textColor
}


val IManaType.displayName get() = buildText { translate("mirageFairy2019.mana.$name.name").color(textColor) }
