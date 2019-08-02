package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.util.text.TextFormatting;

public interface IMirageFairyAbilityType
{

	/**
	 * @return すべて小文字で。
	 */
	public String getName();

	public TextFormatting getTextColor();

	public int getCoreColor();

	public int getHighlightColor();

	public int getBackgroundColor();

	public int getPlasmaColor();

	public default String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocal("fairy.ability." + getName() + ".name");
	}

}
