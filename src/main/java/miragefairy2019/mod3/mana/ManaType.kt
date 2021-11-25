package miragefairy2019.mod3.mana

import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod3.mana.api.EnumManaType
import net.minecraft.util.text.TextFormatting

val EnumManaType.displayName get() = let { manaType -> buildText { translate("mirageFairy2019.mana.$manaType.name").color(textColor) } }

val EnumManaType.color
    get() = when (this) {
        EnumManaType.shine -> 0x007068
        EnumManaType.fire -> 0xFF0000
        EnumManaType.wind -> 0x107A00
        EnumManaType.gaia -> 0x664E00
        EnumManaType.aqua -> 0x3535FF
        EnumManaType.dark -> 0xB000D3
    }

val EnumManaType.textColor
    get() = when (this) {
        EnumManaType.shine -> TextFormatting.WHITE
        EnumManaType.fire -> TextFormatting.RED
        EnumManaType.wind -> TextFormatting.GREEN
        EnumManaType.gaia -> TextFormatting.YELLOW
        EnumManaType.aqua -> TextFormatting.BLUE
        EnumManaType.dark -> TextFormatting.DARK_GRAY
    }
