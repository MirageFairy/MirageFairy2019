package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.fairycrystal.RightClickDropFixed;
import miragefairy2019.mod.api.fairycrystal.RightClickDropInventory;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

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
			{
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.air[0].createItemStack(), 1));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.water[0].createItemStack(), 0.5));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.lava[0].createItemStack(), 0.15));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.fire[0].createItemStack(), 0.015));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.thunder[0].createItemStack(), 0.0045));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.sun[0].createItemStack(), 0.000081));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.moon[0].createItemStack(), 0.000081));

				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.star[0].createItemStack(), 0.00027));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.stone[0].createItemStack(), 0.5));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.dirt[0].createItemStack(), 0.5));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.sand[0].createItemStack(), 0.5));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.gravel[0].createItemStack(), 0.15));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.iron[0].createItemStack(), 0.06));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.gold[0].createItemStack(), 0.018));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.diamond[0].createItemStack(), 0.0054));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.emerald[0].createItemStack(), 0.0054));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.redstone[0].createItemStack(), 0.018, new OreIngredient("dustRedstone")));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.lapislazuli[0].createItemStack(), 0.0054, new OreIngredient("gemLapis")));

				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.enderman[0].createItemStack(), 0.0027));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.spider[0].createItemStack(), 0.03));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.enderdragon[0].createItemStack(), 0.00081));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.chicken[0].createItemStack(), 0.1));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.skeleton[0].createItemStack(), 0.1));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.zombie[0].createItemStack(), 0.1));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.witherskeleton[0].createItemStack(), 0.0027));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.wither[0].createItemStack(), 0.00081));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.creeper[0].createItemStack(), 0.03));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.wheat[0].createItemStack(), 0.03));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.lilac[0].createItemStack(), 0.009));

				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.torch[0].createItemStack(), 0.1));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.furnace[0].createItemStack(), 0.03));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.magentaglazedterracotta[0].createItemStack(), 0.009));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.bread[0].createItemStack(), 0.03));

				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.daytime[0].createItemStack(), 0.02));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.night[0].createItemStack(), 0.02));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.morning[0].createItemStack(), 0.006));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.fine[0].createItemStack(), 0.02));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.rain[0].createItemStack(), 0.006));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.plains[0].createItemStack(), 0.05));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropFixed(ModuleFairy.FairyTypes.forest[0].createItemStack(), 0.015));
			}

		});
	}

}
