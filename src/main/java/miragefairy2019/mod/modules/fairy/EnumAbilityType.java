package miragefairy2019.mod.modules.fairy;

import static net.minecraft.util.text.TextFormatting.*;

import miragefairy2019.mod.api.fairy.IMirageFairyAbilityType;
import net.minecraft.util.text.TextFormatting;

public enum EnumAbilityType implements IMirageFairyAbilityType
{
	attack(DARK_RED, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000),
	craft(GOLD, 0xADADAD, 0xFFC8A4, 0xB57919, 0xDCDCDC),
	fell(DARK_RED, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F),
	light(WHITE, 0xFFFFFF, 0x848484, 0x000000, 0xFFFFFF),
	flame(RED, 0xFF3600, 0xFF9900, 0xCA5B25, 0xFF0000),
	water(AQUA, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF),
	;

	private final TextFormatting colorText;
	private final int colorCore;
	private final int colorHighlight;
	private final int colorBackground;
	private final int colorPlasma;

	private EnumAbilityType(TextFormatting colorText, int colorCore, int colorHighlight, int colorBackground, int colorPlasma)
	{
		this.colorText = colorText;
		this.colorCore = colorCore;
		this.colorHighlight = colorHighlight;
		this.colorBackground = colorBackground;
		this.colorPlasma = colorPlasma;
	}

	@Override
	public String getName()
	{
		return name();
	}

	@Override
	public TextFormatting getTextColor()
	{
		return colorText;
	}

	@Override
	public int getCoreColor()
	{
		return colorCore;
	}

	@Override
	public int getHighlightColor()
	{
		return colorHighlight;
	}

	@Override
	public int getBackgroundColor()
	{
		return colorBackground;
	}

	@Override
	public int getPlasmaColor()
	{
		return colorPlasma;
	}

}
