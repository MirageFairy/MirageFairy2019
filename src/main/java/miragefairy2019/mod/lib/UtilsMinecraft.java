package miragefairy2019.mod.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("deprecation")
public class UtilsMinecraft
{

	public static boolean canTranslate(String key)
	{
		return I18n.canTranslate(key);
	}

	public static String translateToLocal(String key)
	{
		return I18n.translateToLocal(key).trim();
	}

	public static String translateToLocalFormatted(String key, Object... format)
	{
		return I18n.translateToLocalFormatted(key, format).trim();
	}

	public static ItemStack getItemStack(String oreName)
	{
		return OreDictionary.getOres(oreName).get(0);
	}

}
