package miragefairy2019.mod.modules.fairyweapon.critical

import net.minecraft.util.text.TextFormatting

enum class EnumCriticalFactor(val coefficient: Double, val color: TextFormatting) {
    RED(0.2, TextFormatting.DARK_RED),
    ORANGE(0.5, TextFormatting.RED),
    YELLOW(0.8, TextFormatting.YELLOW),
    GREEN(1.0, TextFormatting.GREEN),
    BLUE(1.5, TextFormatting.BLUE),
    WHITE(2.0, TextFormatting.WHITE),
    PURPLE(4.0, TextFormatting.LIGHT_PURPLE),
    CYAN(8.0, TextFormatting.AQUA),
}
