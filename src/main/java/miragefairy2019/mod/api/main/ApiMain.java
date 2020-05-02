package miragefairy2019.mod.api.main;

import org.apache.logging.log4j.Logger;

import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;

public class ApiMain
{

	public static void init(EventRegistryMod erMod)
	{
		miragefairy2019.mod.modules.main.ModuleMain.init(erMod);
	}

	public static Logger logger()
	{
		return miragefairy2019.mod.modules.main.ModuleMain.logger;
	}

	public static Side side()
	{
		return miragefairy2019.mod.modules.main.ModuleMain.side;
	}

	public static CreativeTabs creativeTab()
	{
		return miragefairy2019.mod.modules.main.ModuleMain.creativeTab;
	}

}
