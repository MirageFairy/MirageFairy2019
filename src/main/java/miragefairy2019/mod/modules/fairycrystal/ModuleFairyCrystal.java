package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.mod.modules.fairycrystal.ItemFairyCrystal.Drop;
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

			ApiFairy.itemStackFairyCrystal = new ItemStack(itemFairyCrystal);

			// 妖晶の鉱石辞書登録
			OreDictionary.registerOre("mirageFairyCrystal", new ItemStack(itemFairyCrystal));

			// 妖晶ドロップ登録 TODO 参照違反
			{
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.air[0].createItemStack(), 1));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.water[0].createItemStack(), 0.5));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.lava[0].createItemStack(), 0.15));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.fire[0].createItemStack(), 0.015));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.thunder[0].createItemStack(), 0.0045));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.sun[0].createItemStack(), 0.000081));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.moon[0].createItemStack(), 0.000081));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.star[0].createItemStack(), 0.00027));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.stone[0].createItemStack(), 0.5));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.dirt[0].createItemStack(), 0.5));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.sand[0].createItemStack(), 0.5));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.gravel[0].createItemStack(), 0.15));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.iron[0].createItemStack(), 0.06));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.gold[0].createItemStack(), 0.018));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.diamond[0].createItemStack(), 0.0054));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.emerald[0].createItemStack(), 0.0054));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.redstone[0].createItemStack(), 0.018));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.lapislazuli[0].createItemStack(), 0.0054));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.enderman[0].createItemStack(), 0.0027));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.spider[0].createItemStack(), 0.03));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.enderdragon[0].createItemStack(), 0.00081));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.chicken[0].createItemStack(), 0.1));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.skeleton[0].createItemStack(), 0.1));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.zombie[0].createItemStack(), 0.1));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.witherskeleton[0].createItemStack(), 0.0027));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.wither[0].createItemStack(), 0.00081));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.creeper[0].createItemStack(), 0.03));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.wheat[0].createItemStack(), 0.03));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.lilac[0].createItemStack(), 0.009));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.torch[0].createItemStack(), 0.1));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.furnace[0].createItemStack(), 0.03));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.magentaglazedterracotta[0].createItemStack(), 0.009));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.bread[0].createItemStack(), 0.03));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.daytime[0].createItemStack(), 0.02));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.night[0].createItemStack(), 0.02));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.morning[0].createItemStack(), 0.006));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.fine[0].createItemStack(), 0.02));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.rain[0].createItemStack(), 0.006));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.plains[0].createItemStack(), 0.05));
				ItemFairyCrystal.drops.add(new Drop(ModuleFairy.FairyTypes.forest[0].createItemStack(), 0.015));
			}

		});
	}

}
