package miragefairy2019.mod.modules.fairy;

import static net.minecraft.util.text.TextFormatting.*;

import miragefairy2019.mod.api.fairy.IAbilityType;
import net.minecraft.util.text.TextFormatting;

public enum EnumAbilityType implements IAbilityType
{
	attack(DARK_RED),
	craft(GOLD),
	fell(DARK_GREEN),
	light(YELLOW),
	flame(RED),
	water(BLUE),
	crystal(AQUA),
	art(GREEN),
	store(GOLD),
	warp(DARK_PURPLE),
	shoot(GREEN),
	breaking(DARK_RED),
	chemical(DARK_AQUA),
	slash(DARK_RED),
	food(YELLOW),
	knowledge(DARK_GREEN),
	energy(GOLD),
	submission(DARK_GRAY),
	christmas(DARK_GREEN),
	;

	public final TextFormatting textColor;

	private EnumAbilityType(TextFormatting textColor)
	{
		this.textColor = textColor;
	}

	@Override
	public String getName()
	{
		return name();
	}

	@Override
	public TextFormatting getTextColor()
	{
		return textColor;
	}

	@Override
	public String getUnlocalizedName()
	{
		return "mirageFairy2019.ability." + getName() + ".name";
	}

}
