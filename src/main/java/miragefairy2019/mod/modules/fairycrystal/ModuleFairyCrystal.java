package miragefairy2019.mod.modules.fairycrystal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.OreIngredientComplex;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.struct.Tuple3;
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

	public static VariantFairyCrystal variantFairyCrystal;
	public static VariantFairyCrystal variantFairyCrystalChristmas;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 妖晶のアイテム登録
			{
				ItemFairyCrystal item = new ItemFairyCrystal();
				item.setRegistryName(ModMirageFairy2019.MODID, "fairy_crystal");
				item.setUnlocalizedName("fairyCrystal");
				item.setCreativeTab(ApiMain.creativeTab);
				item.registerVariant(0, variantFairyCrystal = new VariantFairyCrystal("fairy_crystal", "fairyCrystal", "fairyCrystal"));
				item.registerVariant(1, variantFairyCrystalChristmas = new VariantFairyCrystal("christmas_fairy_crystal", "fairyCrystalChristmas", "fairyCrystalChristmas"));
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) {
					for (Tuple<Integer, VariantFairyCrystal> tuple : item.getVariants()) {
						ModelLoader.setCustomModelResourceLocation(
							item,
							tuple.x,
							new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, tuple.y.registryName), null));
					}
				}
				itemFairyCrystal = item;
			}

		});
		erMod.createItemStack.register(ic -> {

			// スフィアの鉱石辞書
			for (Tuple<Integer, VariantFairyCrystal> tuple : itemFairyCrystal.getVariants()) {
				OreDictionary.registerOre(tuple.y.oreName, tuple.y.createItemStack());
			}

			// 妖晶ドロップ登録
			LoaderFairyCrystalDrop.loadFairyCrystalDrop();

		});
		erMod.addRecipe.register(() -> {

			// 妖精の確定レシピ
			LoaderFairyCrystalDrop.loadFairyRecipe(new Consumer<Tuple3<Ingredient, ItemStack, String>>() {
				@Override
				public void accept(Tuple3<Ingredient, ItemStack, String> t)
				{
					Ingredient in = t.x;
					ItemStack out = t.y;
					String name = t.z;

					GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_" + name),
						out,
						new OreIngredientComplex("mirageFairy2019CraftingToolFairyWandSummoning"),
						new OreIngredient("mirageFairyCrystal"),
						in).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_" + name + "_from_fairy_wand_summoning" + suffix(name)));

					GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_" + name),
						out,
						new OreIngredient("gemDiamond"),
						new OreIngredient("mirageFairyCrystal"),
						in).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_" + name + "_from_diamond_gem" + suffix(name)));

				}

				private Map<String, Integer> counter = new HashMap<>();

				private String suffix(String name)
				{
					int i = counter.compute(name, (k, v) -> v != null ? v + 1 : 1);
					return i > 1 ? "_" + i : "";
				}
			});

		});
	}

}
