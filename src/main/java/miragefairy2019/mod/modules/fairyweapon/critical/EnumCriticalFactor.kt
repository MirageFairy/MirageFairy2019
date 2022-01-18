package miragefairy2019.mod.modules.fairyweapon.critical

import net.minecraft.util.text.TextFormatting

enum class EnumCriticalFactor(val coefficient: Double, val color: TextFormatting) {
    red(0.2, TextFormatting.DARK_RED),
    orange(0.5, TextFormatting.RED),
    yellow(0.8, TextFormatting.YELLOW),
    green(1.0, TextFormatting.GREEN),
    blue(1.5, TextFormatting.BLUE),
    white(2.0, TextFormatting.WHITE),
    purple(4.0, TextFormatting.LIGHT_PURPLE),
    cyan(8.0, TextFormatting.AQUA),
}
