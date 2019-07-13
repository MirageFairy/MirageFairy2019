package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMain;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleFairy
{

	public static ItemMirageFairy itemMirageFairy;
	public static ItemFairyCrystal itemFairyCrystal;

	public static VariantMirageFairy variantMirageFairyAir;
	public static VariantMirageFairy variantMirageFairyWater;
	public static VariantMirageFairy variantMirageFairyDirt;
	public static VariantMirageFairy variantMirageFairyFire;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 妖精
			ApiFairy.itemMirageFairy = itemMirageFairy = new ItemMirageFairy();
			itemMirageFairy.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy");
			itemMirageFairy.setUnlocalizedName("mirageFairy");
			itemMirageFairy.setCreativeTab(ApiMain.creativeTab);
			itemMirageFairy.registerVariant(0, variantMirageFairyAir = new VariantMirageFairy("mirage_fairy_air", "mirageFairyAir",
				new MirageFairyType("air", 1, 1, 20, 1, 0, 9, 0, 0, 0, new MirageFairyColorSet(0xFFBE80, 0xDEFFFF, 0xDEFFFF, 0xB0FFFF))));
			itemMirageFairy.registerVariant(1, variantMirageFairyWater = new VariantMirageFairy("mirage_fairy_water", "mirageFairyWater",
				new MirageFairyType("water", 1, 1, 40, 0, 0, 0, 0, 8, 2, new MirageFairyColorSet(0x5469F2, 0x172AD3, 0x5985FF, 0x2D40F4))));
			itemMirageFairy.registerVariant(2, variantMirageFairyDirt = new VariantMirageFairy("mirage_fairy_dirt", "mirageFairyDirt",
				new MirageFairyType("dirt", 1, 1, 60, 0, 0, 0, 8, 0, 2, new MirageFairyColorSet(0xB87440, 0x593D29, 0xB9855C, 0x914A18))));
			itemMirageFairy.registerVariant(3, variantMirageFairyFire = new VariantMirageFairy("mirage_fairy_fire", "mirageFairyFire",
				new MirageFairyType("fire", 1, 1, 20, 1, 9, 0, 0, 0, 0, new MirageFairyColorSet(0xFF6C01, 0xFF7324, 0xF9DFA4, 0xFF4000))));
			ForgeRegistries.ITEMS.register(itemMirageFairy);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantMirageFairy> tuple : itemMirageFairy.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(itemMirageFairy, tuple.x, new ModelResourceLocation(itemMirageFairy.getRegistryName(), null));
				}
			}

			// 妖精結晶
			ApiFairy.itemFairyCrystal = itemFairyCrystal = new ItemFairyCrystal();
			itemFairyCrystal.setRegistryName(ModMirageFairy2019.MODID, "fairy_crystal");
			itemFairyCrystal.setUnlocalizedName("fairyCrystal");
			itemFairyCrystal.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.ITEMS.register(itemFairyCrystal);
			ModelLoader.setCustomModelResourceLocation(itemFairyCrystal, 0, new ModelResourceLocation(itemFairyCrystal.getRegistryName(), null));

		});
		erMod.registerItemColorHandler.register(() -> {

			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int colorMultiplier(ItemStack stack, int tintIndex)
				{
					VariantMirageFairy variant = itemMirageFairy.getVariant(stack).orElse(null);
					if (tintIndex == 0) return variant.type.colorSet.skin;
					if (tintIndex == 1) return 0x8888ff;
					if (tintIndex == 2) return variant.type.colorSet.bright;
					if (tintIndex == 3) return variant.type.colorSet.dark;
					if (tintIndex == 4) return variant.type.colorSet.hair;
					return 0xFFFFFF;
				}
			}, itemMirageFairy);

		});
		erMod.createItemStack.register(ic -> {
			ApiFairy.itemStackMirageFairyMain = variantMirageFairyAir.createItemStack();
			ApiFairy.itemStackFairyCrystal = new ItemStack(itemFairyCrystal);
		});
	}

}
