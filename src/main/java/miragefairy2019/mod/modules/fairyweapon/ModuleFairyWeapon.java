package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
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
			ApiFairy.itemFairyWandCrafting = itemFairyWandCrafting = new ItemFairyWandCrafting();
			itemFairyWandCrafting.setRegistryName(ModMirageFairy2019.MODID, "crafting_fairy_wand");
			itemFairyWandCrafting.setUnlocalizedName("fairyWandCrafting");
			itemFairyWandCrafting.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.ITEMS.register(itemFairyWandCrafting);
			if (ApiMain.side.isClient()) {
				ModelLoader.setCustomModelResourceLocation(itemFairyWandCrafting, 0, new ModelResourceLocation(itemFairyWandCrafting.getRegistryName(), null));
			}

			// 妖精剣
			ApiFairy.itemFairySword = itemFairySword = new ItemFairySword();
			itemFairySword.setRegistryName(ModMirageFairy2019.MODID, "fairy_sword");
			itemFairySword.setUnlocalizedName("fairySword");
			itemFairySword.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.ITEMS.register(itemFairySword);
			if (ApiMain.side.isClient()) {
				ModelLoader.setCustomModelResourceLocation(itemFairySword, 0, new ModelResourceLocation(itemFairySword.getRegistryName(), null));
			}

		});
		erMod.createItemStack.register(ic -> {
			OreDictionary.registerOre("mirageFairyWandCrafting", new ItemStack(itemFairyWandCrafting, 1, OreDictionary.WILDCARD_VALUE));
		});
	}

}
