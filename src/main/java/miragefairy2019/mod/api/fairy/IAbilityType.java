package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.util.text.TextFormatting;

public interface IAbilityType
{

	/**
	 * @return すべて小文字
	 */
	public String getName();

	public TextFormatting getTextColor();

	public default String getUnlocalizedName()
	{
		return "mirageFairy2019.ability." + getName() + ".name";
	}

	public default String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocal(getUnlocalizedName());
	}

}
