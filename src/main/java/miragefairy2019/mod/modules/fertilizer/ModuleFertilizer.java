package miragefairy2019.mod.modules.fertilizer;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleFertilizer
{

	public static ItemFertilizer itemFertilizer;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 肥料のアイテム登録
			{
				ItemFertilizer item = new ItemFertilizer();
				item.setRegistryName(ModMirageFairy2019.MODID, "fertilizer");
				item.setUnlocalizedName("fertilizer");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) {
					ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				}
				itemFertilizer = item;
			}

		});
	}

}
