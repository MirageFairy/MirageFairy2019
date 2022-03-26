package miragefairy2019.mod3.erg

import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod3.erg.api.Erg
import miragefairy2019.mod3.erg.api.IErgEntry
import miragefairy2019.mod3.erg.api.IErgSet
import net.minecraft.util.text.TextFormatting

class ErgSet(private val iterable: Iterable<IErgEntry>) : IErgSet {
    private val list = iterable.toList()
    private val map = list.associateBy { it.type }
    override fun getEntries() = list
    override fun getPower(type: Erg) = map[type]?.power ?: 0.0
}

class ErgEntry(private val type: Erg, private val power: Double) : IErgEntry {
    override fun getPower() = power
    override fun getType() = type
}

val Erg.textColor
    get() = when (this) {
        Erg.ATTACK -> TextFormatting.DARK_RED
        Erg.CRAFT -> TextFormatting.GREEN
        Erg.HARVEST -> TextFormatting.DARK_GREEN
        Erg.LIGHT -> TextFormatting.YELLOW
        Erg.FLAME -> TextFormatting.RED
        Erg.WATER -> TextFormatting.BLUE
        Erg.CRYSTAL -> TextFormatting.AQUA
        Erg.SOUND -> TextFormatting.GRAY
        Erg.SPACE -> TextFormatting.DARK_PURPLE
        Erg.WARP -> TextFormatting.DARK_PURPLE
        Erg.SHOOT -> TextFormatting.GREEN
        Erg.DESTROY -> TextFormatting.DARK_RED
        Erg.CHEMICAL -> TextFormatting.DARK_AQUA
        Erg.SLASH -> TextFormatting.DARK_RED
        Erg.LIFE -> TextFormatting.LIGHT_PURPLE
        Erg.KNOWLEDGE -> TextFormatting.DARK_GREEN
        Erg.ENERGY -> TextFormatting.GOLD
        Erg.SUBMISSION -> TextFormatting.DARK_GRAY
        Erg.CHRISTMAS -> TextFormatting.DARK_GREEN
        Erg.FREEZE -> TextFormatting.AQUA
        Erg.THUNDER -> TextFormatting.YELLOW
        Erg.LEVITATE -> TextFormatting.BLUE
        Erg.SENSE -> TextFormatting.GREEN
    }

val Erg.displayName get() = let { ergType -> buildText { translate("mirageFairy2019.erg.$ergType.name").color(textColor) } }
