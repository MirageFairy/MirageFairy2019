package miragefairy2019.mod.modules.fairy;

import static net.minecraft.util.text.TextFormatting.*;

import net.minecraft.util.text.TextFormatting;

public enum EnumAbilityType
{
	ATTACK(DARK_RED, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000),
	CRAFT(DARK_GREEN, 0xADADAD, 0xFFC8A4, 0xB57919, 0xDCDCDC),
	;

	public final TextFormatting color;
	public final int core;
	public final int highlight;
	public final int background;
	public final int plasma;

	private EnumAbilityType(TextFormatting color, int core, int highlight, int background, int plasma)
	{
		this.color = color;
		this.core = core;
		this.highlight = highlight;
		this.background = background;
		this.plasma = plasma;
	}

}
