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
        EnumErgType.attack -> TextFormatting.DARK_RED
        EnumErgType.craft -> TextFormatting.GREEN
        EnumErgType.harvest -> TextFormatting.DARK_GREEN
        EnumErgType.light -> TextFormatting.YELLOW
        EnumErgType.flame -> TextFormatting.RED
        EnumErgType.water -> TextFormatting.BLUE
        EnumErgType.crystal -> TextFormatting.AQUA
        EnumErgType.sound -> TextFormatting.GRAY
        EnumErgType.space -> TextFormatting.DARK_PURPLE
        EnumErgType.warp -> TextFormatting.DARK_PURPLE
        EnumErgType.shoot -> TextFormatting.GREEN
        EnumErgType.destroy -> TextFormatting.DARK_RED
        EnumErgType.chemical -> TextFormatting.DARK_AQUA
        EnumErgType.slash -> TextFormatting.DARK_RED
        EnumErgType.life -> TextFormatting.LIGHT_PURPLE
        EnumErgType.knowledge -> TextFormatting.DARK_GREEN
        EnumErgType.energy -> TextFormatting.GOLD
        EnumErgType.submission -> TextFormatting.DARK_GRAY
        EnumErgType.christmas -> TextFormatting.DARK_GREEN
        EnumErgType.freeze -> TextFormatting.AQUA
        EnumErgType.thunder -> TextFormatting.YELLOW
        EnumErgType.levitate -> TextFormatting.BLUE
        EnumErgType.sense -> TextFormatting.WHITE
    }

val EnumErgType.displayName get() = let { ergType -> buildText { translate("mirageFairy2019.erg.$ergType.name").color(textColor) } }
