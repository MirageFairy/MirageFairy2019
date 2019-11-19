package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiMaterialsFairy;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.multi.ItemMultiMaterialContained;
import miragefairy2019.mod.lib.multi.ItemVariantMaterialContained;
import mirrg.boron.util.struct.Tuple;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleMaterialsFairy
{

	public static ItemMultiMaterialContained<ItemVariantMaterialContained> itemMaterialsFairy;

	public static ItemVariantMaterialContained variantManaRodShine;
	public static ItemVariantMaterialContained variantManaRodFire;
	public static ItemVariantMaterialContained variantManaRodWind;
	public static ItemVariantMaterialContained variantManaRodGaia;
	public static ItemVariantMaterialContained variantManaRodAqua;
	public static ItemVariantMaterialContained variantManaRodDark;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// マテリアル
			ApiMaterialsFairy.itemMaterialsFairy = itemMaterialsFairy = new ItemMultiMaterialContained<>();
			itemMaterialsFairy.setRegistryName(ModMirageFairy2019.MODID, "fairy_materials");
			itemMaterialsFairy.setUnlocalizedName("materialsFairy");
			itemMaterialsFairy.setCreativeTab(ApiMain.creativeTab);
			itemMaterialsFairy.registerVariant(0,
				variantManaRodShine = new ItemVariantMaterialContained("shine_mana_rod", "manaRodShine", "mirageFairy2019ManaRodShine", "Miragium(1.000), Diamond(1.000)"));
			itemMaterialsFairy.registerVariant(1,
				variantManaRodFire = new ItemVariantMaterialContained("fire_mana_rod", "manaRodFire", "mirageFairy2019ManaRodFire", "Miragium(1.000), Cinnabar(1.000)"));
			itemMaterialsFairy.registerVariant(2,
				variantManaRodWind = new ItemVariantMaterialContained("wind_mana_rod", "manaRodWind", "mirageFairy2019ManaRodWind", "Miragium(1.000), Fluorite(1.000)"));
			itemMaterialsFairy.registerVariant(3,
				variantManaRodGaia = new ItemVariantMaterialContained("gaia_mana_rod", "manaRodGaia", "mirageFairy2019ManaRodGaia", "Miragium(1.000), Sulfur(1.000)"));
			itemMaterialsFairy.registerVariant(4,
				variantManaRodAqua = new ItemVariantMaterialContained("aqua_mana_rod", "manaRodAqua", "mirageFairy2019ManaRodAqua", "Miragium(1.000), Apatite(1.000)"));
			itemMaterialsFairy.registerVariant(5,
				variantManaRodDark = new ItemVariantMaterialContained("dark_mana_rod", "manaRodDark", "mirageFairy2019ManaRodDark", "Miragium(1.000), Coal(1.000)"));
			ForgeRegistries.ITEMS.register(itemMaterialsFairy);
			if (ApiMain.side.isClient()) itemMaterialsFairy.setCustomModelResourceLocations();

		});
		erMod.createItemStack.register(ic -> {
			ApiMaterialsFairy.itemStackManaRodShine = variantManaRodShine.createItemStack();
			ApiMaterialsFairy.itemStackManaRodFire = variantManaRodFire.createItemStack();
			ApiMaterialsFairy.itemStackManaRodWind = variantManaRodWind.createItemStack();
			ApiMaterialsFairy.itemStackManaRodGaia = variantManaRodGaia.createItemStack();
			ApiMaterialsFairy.itemStackManaRodAqua = variantManaRodAqua.createItemStack();
			ApiMaterialsFairy.itemStackManaRodDark = variantManaRodDark.createItemStack();
			for (Tuple<Integer, ItemVariantMaterialContained> tuple : itemMaterialsFairy.getVariants()) {
				OreDictionary.registerOre(tuple.y.oreName, tuple.y.createItemStack());
			}
		});
	}

}
