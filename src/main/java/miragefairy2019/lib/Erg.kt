package miragefairy2019.lib

import miragefairy2019.api.Erg
import miragefairy2019.api.ErgSet
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.withColor
import net.minecraft.util.text.TextFormatting

operator fun ErgSet.get(erg: Erg) = getValue(erg)
val ErgSet.ergs get() = (0 until size).map { getErg(it) }
val ErgSet.entries get() = ergs.map { it to this[it] }

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

val Erg.registryName get() = this.name.toLowerCase()
val Erg.displayName get() = let { ergType -> textComponent { translate("mirageFairy2019.erg.$ergType.name").withColor(textColor) } }
