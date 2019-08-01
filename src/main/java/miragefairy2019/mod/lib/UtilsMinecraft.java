package miragefairy2019.mod.lib;

import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class UtilsMinecraft
{

	public static String translateToLocal(String key)
	{
		return I18n.translateToLocal(key).trim();
	}

	public static String translateToLocalFormatted(String key, Object... format)
	{
		return I18n.translateToLocalFormatted(key, format).trim();
	}

}
