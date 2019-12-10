package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.util.text.TextFormatting;

public enum EnumManaType
{
	shine(TextFormatting.WHITE),
	fire(TextFormatting.RED),
	wind(TextFormatting.GREEN),
	gaia(TextFormatting.YELLOW),
	aqua(TextFormatting.BLUE),
	dark(TextFormatting.DARK_GRAY),
	;

	public final TextFormatting colorText;

	private EnumManaType(TextFormatting colorText)
	{
		this.colorText = colorText;
	}

	public String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocal("mirageFairy2019.mana." + name() + ".name");
	}

}
