package miragefairy2019.mod.modules.fairy;

import static net.minecraft.util.text.TextFormatting.*;

import net.minecraft.util.text.TextFormatting;

public enum EnumAbilityType
{
	ATTACK(DARK_RED, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000),
	CRAFT(GOLD, 0xADADAD, 0xFFC8A4, 0xB57919, 0xDCDCDC),
	FELL(DARK_RED, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F),
	LIGHT(WHITE, 0xFFFFFF, 0x848484, 0x000000, 0xFFFFFF),
	FLAME(RED, 0xFF3600, 0xFF9900, 0xCA5B25, 0xFF0000),
	WATER(AQUA, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF),
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
