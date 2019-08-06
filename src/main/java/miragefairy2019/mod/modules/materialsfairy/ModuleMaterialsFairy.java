package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiMaterialsFairy;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import mirrg.boron.util.struct.Tuple;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleMaterialsFairy
{

	public static ItemMultiMaterial<ItemVariantMaterial> itemMaterialsFairy;

	public static ItemVariantMaterial variantManaRodShine;
	public static ItemVariantMaterial variantManaRodFire;
	public static ItemVariantMaterial variantManaRodWind;
	public static ItemVariantMaterial variantManaRodGaia;
	public static ItemVariantMaterial variantManaRodAqua;
	public static ItemVariantMaterial variantManaRodDark;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// マテリアル
			ApiMaterialsFairy.itemMaterialsFairy = itemMaterialsFairy = new ItemMultiMaterial<>();
			itemMaterialsFairy.setRegistryName(ModMirageFairy2019.MODID, "fairy_materials");
			itemMaterialsFairy.setUnlocalizedName("materialsFairy");
			itemMaterialsFairy.setCreativeTab(ApiMain.creativeTab);
			itemMaterialsFairy.registerVariant(0, variantManaRodShine = new ItemVariantMaterial("shine_mana_rod", "manaRodShine", "mirageFairy2019ManaRodShine"));
			itemMaterialsFairy.registerVariant(1, variantManaRodFire = new ItemVariantMaterial("fire_mana_rod", "manaRodFire", "mirageFairy2019ManaRodFire"));
			itemMaterialsFairy.registerVariant(2, variantManaRodWind = new ItemVariantMaterial("wind_mana_rod", "manaRodWind", "mirageFairy2019ManaRodWind"));
			itemMaterialsFairy.registerVariant(3, variantManaRodGaia = new ItemVariantMaterial("gaia_mana_rod", "manaRodGaia", "mirageFairy2019ManaRodGaia"));
			itemMaterialsFairy.registerVariant(4, variantManaRodAqua = new ItemVariantMaterial("aqua_mana_rod", "manaRodAqua", "mirageFairy2019ManaRodAqua"));
			itemMaterialsFairy.registerVariant(5, variantManaRodDark = new ItemVariantMaterial("dark_mana_rod", "manaRodDark", "mirageFairy2019ManaRodDark"));
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
			for (Tuple<Integer, ItemVariantMaterial> tuple : itemMaterialsFairy.getVariants()) {
				OreDictionary.registerOre(tuple.y.oreName, tuple.y.createItemStack());
			}
		});
	}

}
