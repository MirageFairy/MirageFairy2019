package miragefairy2019.mod3.erg

import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.IErgEntry
import miragefairy2019.mod3.erg.api.IErgSet
import net.minecraft.util.text.TextFormatting

class ErgSet(private val iterable: Iterable<IErgEntry>) : IErgSet {
    private val list = iterable.toList()
    private val map = list.associateBy { it.type }
    override fun getEntries() = list
    override fun getPower(type: EnumErgType) = map[type]?.power ?: 0.0
}

class ErgEntry(private val type: EnumErgType, private val power: Double) : IErgEntry {
    override fun getPower() = power
    override fun getType() = type
}

val EnumErgType.textColor
    get() = when (this) {
        EnumErgType.ATTACK -> TextFormatting.DARK_RED
        EnumErgType.CRAFT -> TextFormatting.GREEN
        EnumErgType.HARVEST -> TextFormatting.DARK_GREEN
        EnumErgType.LIGHT -> TextFormatting.YELLOW
        EnumErgType.FLAME -> TextFormatting.RED
        EnumErgType.WATER -> TextFormatting.BLUE
        EnumErgType.CRYSTAL -> TextFormatting.AQUA
        EnumErgType.SOUND -> TextFormatting.GRAY
        EnumErgType.SPACE -> TextFormatting.DARK_PURPLE
        EnumErgType.WARP -> TextFormatting.DARK_PURPLE
        EnumErgType.SHOOT -> TextFormatting.GREEN
        EnumErgType.DESTROY -> TextFormatting.DARK_RED
        EnumErgType.CHEMICAL -> TextFormatting.DARK_AQUA
        EnumErgType.SLASH -> TextFormatting.DARK_RED
        EnumErgType.LIFE -> TextFormatting.LIGHT_PURPLE
        EnumErgType.KNOWLEDGE -> TextFormatting.DARK_GREEN
        EnumErgType.ENERGY -> TextFormatting.GOLD
        EnumErgType.SUBMISSION -> TextFormatting.DARK_GRAY
        EnumErgType.CHRISTMAS -> TextFormatting.DARK_GREEN
        EnumErgType.FREEZE -> TextFormatting.AQUA
        EnumErgType.THUNDER -> TextFormatting.YELLOW
        EnumErgType.LEVITATE -> TextFormatting.BLUE
        EnumErgType.SENSE -> TextFormatting.GREEN
    }

val EnumErgType.displayName get() = let { ergType -> buildText { translate("mirageFairy2019.erg.$ergType.name").color(textColor) } }
