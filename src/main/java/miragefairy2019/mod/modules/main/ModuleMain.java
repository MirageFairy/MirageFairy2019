package miragefairy2019.mod.modules.main;

import org.apache.logging.log4j.Logger;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleMain
{

	public static Logger logger;
	public static Side side;

	public static CreativeTabs creativeTab;

	public static void init(EventRegistryMod erMod)
	{

		// ネットワークラッパー初期化
		erMod.initNetworkChannel.register(() -> {
			ApiMain.simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID);
		});

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
