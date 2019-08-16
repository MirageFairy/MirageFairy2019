package miragefairy2019.mod.modules.fairycrystal;

import java.util.function.Predicate;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.fairycrystal.RightClickDropFixed;
import miragefairy2019.mod.api.fairycrystal.RightClickDropInventory;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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

				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.stone[0].createItemStack(), 0.5, block(Blocks.STONE)));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.dirt[0].createItemStack(), 0.5, block(Blocks.STONE)));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.sand[0].createItemStack(), 0.5, block(Blocks.STONE)));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.gravel[0].createItemStack(), 0.15, block(Blocks.STONE)));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.iron[0].createItemStack(), 0.06, ore("ingotIron", "blockIron")));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.gold[0].createItemStack(), 0.018, ore("ingotGold", "blockGold")));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.diamond[0].createItemStack(), 0.0054, ore("gemDiamond", "blockDiamond")));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.emerald[0].createItemStack(), 0.0054, ore("gemEmerald", "blockEmerald")));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.redstone[0].createItemStack(), 0.018, ore("dustRedstone", "blockRedstone")));
				ApiFairyCrystal.dropsFairyCrystal.add(new RightClickDropInventory(ModuleFairy.FairyTypes.lapislazuli[0].createItemStack(), 0.0054, ore("gemLapis", "blockLapis")));

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

	private static Predicate<ItemStack> a(ISuppliterator<Predicate<ItemStack>> sPredicates)
	{
		ImmutableArray<Predicate<ItemStack>> predicates = sPredicates
			.toImmutableArray();
		return itemStack -> {
			for (Predicate<ItemStack> predicate : predicates) {
				if (predicate.test(itemStack)) return true;
			}
			return false;
		};
	}

	private static Predicate<ItemStack> block(Block... blocks)
	{
		return a(ISuppliterator.ofObjArray(blocks).map(block -> Ingredient.fromItems(Item.getItemFromBlock(block))));
	}

	private static Predicate<ItemStack> ore(String... ores)
	{
		return a(ISuppliterator.ofObjArray(ores).map(ore -> new OreIngredient(ore)));
	}

}
