package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.OreIngredientComplex;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
		erMod.addRecipe.register(() -> {

			// 妖精の確定レシピ
			LoaderFairyCrystalDrop.loadFairyRecipe(t -> {
				Ingredient in = t.x;
				ItemStack out = t.y;
				String name = t.z;

				GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(
					new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_" + name),
					out,
					new OreIngredientComplex("mirageFairy2019CraftingToolFairyWandSummoning"),
					new OreIngredient("mirageFairyCrystal"),
					in).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_" + name + "_from_fairy_wand_summoning"));

				GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(
					new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_" + name),
					out,
					new OreIngredient("gemDiamond"),
					new OreIngredient("mirageFairyCrystal"),
					in).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_" + name + "_from_diamond_gem"));

			});

		});
	}

}
