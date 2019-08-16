package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairyCrystal
{

	public static ItemFairyCrystal itemFairyCrystal;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 妖晶のアイテム登録
			{
				ItemFairyCrystal item = new ItemFairyCrystal();
				item.setRegistryName(ModMirageFairy2019.MODID, "fairy_crystal");
				item.setUnlocalizedName("fairyCrystal");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) {
					ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				}
				ApiFairyCrystal.itemFairyCrystal = itemFairyCrystal = item;
			}

		});
		erMod.createItemStack.register(ic -> {

			ApiFairyCrystal.itemStackFairyCrystal = new ItemStack(itemFairyCrystal);

			// 妖晶の鉱石辞書登録
			OreDictionary.registerOre("mirageFairyCrystal", new ItemStack(itemFairyCrystal));

			// 妖晶ドロップ登録
			LoaderFairyCrystalDrop.loadFairyCrystalDrop();

		});
	}

}
