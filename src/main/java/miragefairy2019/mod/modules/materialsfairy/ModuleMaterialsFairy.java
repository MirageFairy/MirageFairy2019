package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiMaterialsFairy;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleMaterialsFairy
{

	public static ItemMultiMaterial<ItemVariantMaterial> itemMaterialsFairy;

	//public static ItemVariant variantBucketFairyWater; // TODO

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// マテリアル
			ApiMaterialsFairy.itemMaterialsFairy = itemMaterialsFairy = new ItemMultiMaterial<>();
			itemMaterialsFairy.setRegistryName(ModMirageFairy2019.MODID, "fairy_materials");
			itemMaterialsFairy.setUnlocalizedName("materialsFairy");
			itemMaterialsFairy.setCreativeTab(ApiMain.creativeTab);
			//itemMaterialsFairy.registerVariant(0, variantBucketFairyWater = new ItemVariant("fairy_water_bucket", "bucketFairyWater")); // TODO
			ForgeRegistries.ITEMS.register(itemMaterialsFairy);
			if (ApiMain.side.isClient()) itemMaterialsFairy.setCustomModelResourceLocations();

		});
		erMod.createItemStack.register(ic -> {

			// TODO

		});
	}

}
