package miragefairy2019.mod.app;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMain;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleFairy
{

	public static ItemMirageFairy itemMirageFairy;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 妖精
			ApiFairy.itemMirageFairy = itemMirageFairy = new ItemMirageFairy();
			itemMirageFairy.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy");
			itemMirageFairy.setCreativeTab(ApiMain.creativeTab);
			itemMirageFairy.registerVariant(0, new VariantMirageFairy("mirage_fairy_air", "mirageFairyAir", "air", 1, 1, 20, 1, 0, 9, 0, 0, 0));
			itemMirageFairy.registerVariant(1, new VariantMirageFairy("mirage_fairy_water", "mirageFairyWater", "water", 1, 1, 40, 0, 0, 0, 0, 8, 2));
			itemMirageFairy.registerVariant(2, new VariantMirageFairy("mirage_fairy_dirt", "mirageFairyDirt", "dirt", 1, 1, 60, 0, 0, 0, 8, 0, 2));
			itemMirageFairy.registerVariant(3, new VariantMirageFairy("mirage_fairy_fire", "mirageFairyFire", "fire", 1, 1, 20, 1, 9, 0, 0, 0, 0));
			ForgeRegistries.ITEMS.register(itemMirageFairy);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantMirageFairy> tuple : itemMirageFairy.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(itemMirageFairy, tuple.x, new ModelResourceLocation(itemMirageFairy.getRegistryName(), null));
				}
			}

		});
		erMod.createItemStack.register(ic -> {
			ApiFairy.itemStackMirageFairyMain = new ItemStack(Items.LEAD); // TODO
		});
	}

}
