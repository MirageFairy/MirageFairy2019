package miragefairy2019.mod.api.fairy;

import net.minecraft.util.text.TextFormatting;

public enum EnumMirageFairyManaType
{
	shine(TextFormatting.WHITE),
	fire(TextFormatting.RED),
	wind(TextFormatting.GREEN),
	gaia(TextFormatting.YELLOW),
	aqua(TextFormatting.BLUE),
	dark(TextFormatting.DARK_GRAY),
	;

	public final TextFormatting colorText;

	private EnumMirageFairyManaType(TextFormatting colorText)
	{
		this.colorText = colorText;
	}

}
