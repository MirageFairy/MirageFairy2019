package miragefairy2019.mod.modules;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleMain
{

	public static void init(EventRegistryMod erMod)
	{
		erMod.initCreativeTab.register(() -> {
			ApiMain.creativeTab = new CreativeTabs("mirageFairy2019") {
				@Override
				@SideOnly(Side.CLIENT)
				public ItemStack getTabIconItem()
				{
					return new ItemStack(ApiFairy.itemFairyCrystal);
				}
			};
		});
		erMod.preInit.register(e -> {
			ApiMain.logger = e.getModLog();
			ApiMain.side = e.getSide();
		});
	}

}
