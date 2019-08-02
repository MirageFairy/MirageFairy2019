package miragefairy2019.mod.api;

import static net.minecraft.util.text.TextFormatting.*;

import miragefairy2019.mod.api.fairy.IMirageFairyAbilityType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ApiFairy
{

	public static CreativeTabs creativeTab;

	public static Item itemMirageFairyR1;
	public static Item itemMirageFairyR2;
	public static Item itemMirageFairyR3;
	public static Item itemMirageFairyR4;
	public static Item itemMirageFairyR5;
	public static Item itemMaterialsFairy;

	public static ItemStack itemStackMirageFairyMain;
	public static ItemStack itemStackFairyCrystal;

	public static enum EnumAbilityType implements IMirageFairyAbilityType
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

}
