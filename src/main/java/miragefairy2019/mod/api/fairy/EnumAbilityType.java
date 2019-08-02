package miragefairy2019.mod.api.fairy;

import static net.minecraft.util.text.TextFormatting.*;

import net.minecraft.util.text.TextFormatting;

public enum EnumAbilityType implements IMirageFairyAbilityType
{
	attack(DARK_RED),
	craft(GOLD),
	fell(DARK_RED),
	light(WHITE),
	flame(RED),
	water(AQUA),
	;

	private final TextFormatting colorText;

	private EnumAbilityType(TextFormatting colorText)
	{
		this.colorText = colorText;
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

}
