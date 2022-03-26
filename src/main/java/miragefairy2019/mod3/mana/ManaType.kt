package miragefairy2019.mod3.mana

import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod3.mana.api.Mana
import net.minecraft.util.text.TextFormatting

val Mana.displayName get() = let { manaType -> buildText { translate("mirageFairy2019.mana.$manaType.name").color(textColor) } }

val Mana.color
    get() = when (this) {
        Mana.SHINE -> 0x007068
        Mana.FIRE -> 0xFF0000
        Mana.WIND -> 0x107A00
        Mana.GAIA -> 0x664E00
        Mana.AQUA -> 0x3535FF
        Mana.DARK -> 0xB000D3
    }

val Mana.textColor
    get() = when (this) {
        Mana.SHINE -> TextFormatting.WHITE
        Mana.FIRE -> TextFormatting.RED
        Mana.WIND -> TextFormatting.GREEN
        Mana.GAIA -> TextFormatting.YELLOW
        Mana.AQUA -> TextFormatting.BLUE
        Mana.DARK -> TextFormatting.DARK_GRAY
    }
