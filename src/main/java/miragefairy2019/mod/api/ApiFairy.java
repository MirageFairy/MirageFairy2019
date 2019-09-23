package miragefairy2019.mod.api;

import static net.minecraft.util.text.TextFormatting.*;

import miragefairy2019.mod.api.fairy.IAbilityType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ApiFairy
{

	public static CreativeTabs creativeTab;

	public static Item[] itemFairyList;

	public static ItemStack itemStackFairyMain;

	public static enum EnumAbilityType implements IAbilityType
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

}
