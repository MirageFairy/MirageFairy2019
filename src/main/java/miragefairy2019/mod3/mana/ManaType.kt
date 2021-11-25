package miragefairy2019.mod3.mana

import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod3.mana.api.EnumManaType
import net.minecraft.util.text.TextFormatting

val EnumManaType.displayName get() = let { manaType -> buildText { translate("mirageFairy2019.mana.$manaType.name").color(textColor) } }

val EnumManaType.color
    get() = when (this) {
        EnumManaType.SHINE -> 0x007068
        EnumManaType.FIRE -> 0xFF0000
        EnumManaType.WIND -> 0x107A00
        EnumManaType.GAIA -> 0x664E00
        EnumManaType.AQUA -> 0x3535FF
        EnumManaType.DARK -> 0xB000D3
    }

val EnumManaType.textColor
    get() = when (this) {
        EnumManaType.SHINE -> TextFormatting.WHITE
        EnumManaType.FIRE -> TextFormatting.RED
        EnumManaType.WIND -> TextFormatting.GREEN
        EnumManaType.GAIA -> TextFormatting.YELLOW
        EnumManaType.AQUA -> TextFormatting.BLUE
        EnumManaType.DARK -> TextFormatting.DARK_GRAY
    }
