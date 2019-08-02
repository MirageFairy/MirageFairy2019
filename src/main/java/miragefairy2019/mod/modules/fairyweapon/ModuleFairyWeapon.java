package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyWeapon;
import miragefairy2019.mod.api.ApiMain;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairyWeapon
{

	public static ItemFairyWandCrafting itemFairyWandCrafting;
	public static ItemFairySword itemFairySword;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 加工のステッキ
			{
				ItemFairyWandCrafting item = new ItemFairyWandCrafting();
				item.setRegistryName(ModMirageFairy2019.MODID, "crafting_fairy_wand");
				item.setUnlocalizedName("fairyWandCrafting");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) {
					ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				}
				ApiFairyWeapon.itemFairyWandCrafting = itemFairyWandCrafting = item;
			}

			// 妖精剣
			{
				ItemFairySword item = new ItemFairySword();
				item.setRegistryName(ModMirageFairy2019.MODID, "fairy_sword");
				item.setUnlocalizedName("fairySword");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) {
					ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				}
				ApiFairyWeapon.itemFairySword = itemFairySword = item;
			}

		});
		erMod.createItemStack.register(ic -> {
			OreDictionary.registerOre("mirageFairyWandCrafting", new ItemStack(itemFairyWandCrafting, 1, OreDictionary.WILDCARD_VALUE));
		});
	}

}
