package miragefairy2019.mod.modules.fairyweapon.critical;

import net.minecraft.util.text.TextFormatting;

public enum EnumCriticalFactor
{
	red(0.2, TextFormatting.DARK_RED),
	orange(0.5, TextFormatting.RED),
	yellow(0.8, TextFormatting.YELLOW),
	green(1.0, TextFormatting.GREEN),
	blue(1.5, TextFormatting.BLUE),
	white(2.0, TextFormatting.WHITE),
	purple(4.0, TextFormatting.LIGHT_PURPLE),
	cyan(8.0, TextFormatting.AQUA),
	;

	public final double coefficient;
	public final TextFormatting color;

	private EnumCriticalFactor(double coefficient, TextFormatting color)
	{
		this.coefficient = coefficient;
		this.color = color;
	}

}
