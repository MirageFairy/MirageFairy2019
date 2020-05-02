package miragefairy2019.mod.modules.main;

import org.apache.logging.log4j.Logger;

import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleMain
{

	public static Logger logger;
	public static Side side;

	public static CreativeTabs creativeTab;

	public static void init(EventRegistryMod erMod)
	{
		erMod.initCreativeTab.register(() -> {
			creativeTab = new CreativeTabs("mirageFairy2019") {
				@Override
				@SideOnly(Side.CLIENT)
				public ItemStack getTabIconItem()
				{
					return ModuleFairyCrystal.variantFairyCrystal.createItemStack();
				}
			};
		});
		erMod.preInit.register(e -> {
			logger = e.getModLog();
			side = e.getSide();
		});
	}

}
