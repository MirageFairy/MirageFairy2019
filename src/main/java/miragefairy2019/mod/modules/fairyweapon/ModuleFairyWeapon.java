package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyWeapon;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairyWeapon
{

	public static ItemCraftingFairyWand itemCraftingFairyWand;
	public static ItemMeltingFairyWand itemMeltingFairyWand;
	public static ItemFairySword itemFairySword;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 加工のステッキ
			{
				ItemCraftingFairyWand item = new ItemCraftingFairyWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "crafting_fairy_wand");
				item.setUnlocalizedName("craftingFairyWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemCraftingFairyWand = itemCraftingFairyWand = item;
			}

			// 融解のステッキ
			{
				ItemMeltingFairyWand item = new ItemMeltingFairyWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "melting_fairy_wand");
				item.setUnlocalizedName("meltingFairyWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemMeltingFairyWand = itemMeltingFairyWand = item;
			}

			// 妖精剣
			{
				ItemFairySword item = new ItemFairySword();
				item.setRegistryName(ModMirageFairy2019.MODID, "fairy_sword");
				item.setUnlocalizedName("fairySword");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemFairySword = itemFairySword = item;
			}

		});
		erMod.createItemStack.register(ic -> {
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandCrafting", new ItemStack(itemCraftingFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandMelting", new ItemStack(itemMeltingFairyWand, 1, OreDictionary.WILDCARD_VALUE));
		});
	}

}
