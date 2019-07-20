package miragefairy2019.mod.modules.fairy;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.Utils;
import mirrg.boron.util.struct.ImmutableArray;
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

	public static class FairyTypes
	{

		public static ImmutableArray<Tuple<Integer, VariantMirageFairy>> variants;

		public static VariantMirageFairy air;
		public static VariantMirageFairy water;
		public static VariantMirageFairy fire;
		public static VariantMirageFairy sun;
		public static VariantMirageFairy stone;
		public static VariantMirageFairy dirt;
		public static VariantMirageFairy iron;
		public static VariantMirageFairy diamond;
		public static VariantMirageFairy redstone;
		public static VariantMirageFairy enderman;
		public static VariantMirageFairy moon;
		public static VariantMirageFairy sand;
		public static VariantMirageFairy gold;
		public static VariantMirageFairy spider;
		public static VariantMirageFairy skeleton;
		public static VariantMirageFairy zombie;
		public static VariantMirageFairy creeper;
		public static VariantMirageFairy wheat;
		public static VariantMirageFairy lilac;
		public static VariantMirageFairy torch;
		public static VariantMirageFairy lava;
		public static VariantMirageFairy star;
		public static VariantMirageFairy gravel;
		public static VariantMirageFairy emerald;
		public static VariantMirageFairy lapislazuli;
		public static VariantMirageFairy enderdragon;
		public static VariantMirageFairy witherskeleton;
		public static VariantMirageFairy wither;
		public static VariantMirageFairy thunder;
		public static VariantMirageFairy chicken;
		public static VariantMirageFairy furnace;
		public static VariantMirageFairy magentaglazedterracotta;
		public static VariantMirageFairy bread;
		public static VariantMirageFairy daytime;
		public static VariantMirageFairy night;
		public static VariantMirageFairy morning;
		public static VariantMirageFairy fine;
		public static VariantMirageFairy rain;
		public static VariantMirageFairy plains;
		public static VariantMirageFairy forest;

		//

		private static List<Tuple<Integer, VariantMirageFairy>> variants2 = new ArrayList<>();

		protected void init()
		{
			{
				r(0, air = v(t("air", 1, 1, 15, m(1.34, 0.00, 13.45, 0.00, 0.00, 0.00), c(0xFFBE80, 0xDEFFFF, 0xDEFFFF, 0xB0FFFF))));
				r(1, water = v(t("water", 1, 1, 50, m(0.00, 0.00, 0.00, 2.67, 26.69, 16.02), c(0x5469F2, 0x5985FF, 0x172AD3, 0x2D40F4))));
				r(2, fire = v(t("fire", 2, 1, 20, m(2.13, 21.32, 0.00, 0.00, 0.00, 0.00), c(0xFF6C01, 0xF9DFA4, 0xFF7324, 0xFF4000))));
				r(3, sun = v(t("sun", 5, 1, 99, m(14.29, 40.00, 34.29, 25.72, 25.72, 0.00), c(0xff2f00, 0xff972b, 0xff7500, 0xffe7b2))));
				r(4, stone = v(t("stone", 1, 1, 83, m(0.00, 0.00, 0.00, 30.27, 0.00, 45.40), c(0x333333, 0x8F8F8F, 0x686868, 0x747474))));
				r(5, dirt = v(t("dirt", 1, 1, 70, m(0.00, 0.00, 0.00, 16.01, 4.80, 44.82), c(0xB87440, 0xB9855C, 0x593D29, 0x914A18))));
				r(6, iron = v(t("iron", 2, 1, 86, m(0.00, 11.14, 0.00, 37.13, 14.85, 22.28), c(0xA0A0A0, 0xD8D8D8, 0x727272, 0xD8AF93))));
				r(7, diamond = v(t("diamond", 4, 1, 76, m(14.44, 18.77, 30.33, 37.55, 0.00, 0.00), c(0x97FFE3, 0xD1FAF3, 0x70FFD9, 0x30DBBD))));
				r(8, redstone = v(t("redstone", 3, 1, 54, m(1.08, 37.97, 11.93, 10.85, 0.00, 6.51), c(0xFF5959, 0xFF0000, 0xCD0000, 0xBA0000))));
				r(9, enderman = v(t("enderman", 4, 1, 48, m(1.00, 17.96, 17.96, 11.97, 9.98, 0.00), c(0x000000, 0x161616, 0x161616, 0xEF84FA))));
				r(10, moon = v(t("moon", 5, 1, 95, m(12.36, 29.66, 34.61, 11.12, 11.12, 30.90), c(0xD9E4FF, 0x747D93, 0x0C121F, 0x2D4272))));
				r(11, sand = v(t("sand", 1, 1, 64, m(2.15, 0.00, 0.00, 21.55, 0.00, 34.48), c(0xB87440, 0xEEE4B6, 0xC2BC84, 0xD8D09B))));
				r(12, gold = v(t("gold", 3, 1, 93, m(5.72, 0.00, 51.52, 57.25, 0.00, 0.00), c(0xA0A0A0, 0xFFFF0B, 0xDC7613, 0xDEDE00))));
				r(13, spider = v(t("spider", 2, 1, 43, m(0.00, 0.00, 0.00, 0.00, 34.56, 13.82), c(0x494422, 0x61554A, 0x52483F, 0xA80E0E))));
				r(14, skeleton = v(t("skeleton", 1, 1, 49, m(0.00, 4.53, 15.10, 7.55, 0.00, 12.08), c(0xCACACA, 0xCFCFCF, 0xCFCFCF, 0x494949))));
				r(15, zombie = v(t("zombie", 1, 1, 55, m(0.00, 2.12, 21.17, 0.00, 4.23, 19.05), c(0x2B4219, 0x00AAAA, 0x322976, 0x2B4219))));
				r(16, creeper = v(t("creeper", 2, 1, 35, m(0.00, 0.00, 10.78, 5.39, 12.94, 4.31), c(0x5BAA53, 0xD6FFCF, 0x5EE74C, 0x000000))));
				r(17, wheat = v(t("wheat", 2, 1, 31, m(0.00, 0.00, 0.00, 0.00, 22.93, 11.47), c(0x168700, 0xD5DA45, 0x716125, 0x9E8714))));
				r(18, lilac = v(t("lilac", 3, 1, 28, m(0.00, 0.00, 3.21, 3.21, 32.10, 0.00), c(0x63D700, 0xF0C9FF, 0xDC8CE6, 0xA22CFF))));
				r(19, torch = v(t("torch", 1, 1, 19, m(0.78, 3.88, 0.78, 7.77, 3.11, 0.00), c(0xFFFFFF, 0xFFC52C, 0xFF5800, 0xFFE6A5))));
				r(20, lava = v(t("lava", 2, 1, 58, m(0.00, 33.87, 0.00, 12.32, 15.39, 0.00), c(0xCD4208, 0xEDB54A, 0xCC4108, 0x4C1500))));
				r(21, star = v(t("star", 4, 1, 98, m(13.51, 21.62, 21.62, 12.16, 12.16, 51.34), c(0xffffff, 0x2C2C2E, 0x0E0E10, 0x191919))));
				r(22, gravel = v(t("gravel", 2, 1, 77, m(0.00, 0.00, 0.00, 45.53, 0.00, 36.43), c(0x333333, 0xC0B5B6, 0x968B8E, 0x63565C))));
				r(23, emerald = v(t("emerald", 4, 1, 73, m(13.62, 21.78, 20.42, 0.00, 0.00, 49.02), c(0x9FF9B5, 0x81F99E, 0x17DD62, 0x008A25))));
				r(24, lapislazuli = v(t("lapislazuli", 4, 1, 62, m(4.70, 0.00, 18.82, 23.52, 42.34, 0.00), c(0xA2B7E8, 0x4064EC, 0x224BD5, 0x0A33C2))));
				r(25, enderdragon = v(t("enderdragon", 5, 1, 61, m(7.39, 0.00, 83.70, 0.00, 24.62, 0.00), c(0x000000, 0x181818, 0x181818, 0xA500E2))));
				r(26, witherskeleton = v(t("witherskeleton", 4, 1, 69, m(0.00, 21.56, 53.91, 21.56, 5.39, 0.00), c(0x505252, 0x1C1C1C, 0x1C1C1C, 0x060606))));
				r(27, wither = v(t("wither", 5, 1, 52, m(0.00, 22.63, 45.27, 13.58, 4.53, 4.53), c(0x181818, 0x3C3C3C, 0x141414, 0x557272))));
				r(28, thunder = v(t("thunder", 3, 1, 18, m(2.62, 8.74, 2.62, 6.99, 0.00, 0.00), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0xFFEB00))));
				r(29, chicken = v(t("chicken", 1, 1, 39, m(0.00, 0.00, 1.94, 0.00, 19.39, 13.57), c(0xFFDFA3, 0xFFFFFF, 0xFFFFFF, 0xD93117))));
				r(30, furnace = v(t("furnace", 2, 1, 72, m(0.00, 6.40, 0.00, 32.00, 0.00, 35.20), c(0xFFFFFF, 0xFF7F19, 0x8E8E8E, 0x383838))));
				r(31, magentaglazedterracotta = v(t("magentaglazedterracotta", 3, 1, 60, m(0.00, 3.47, 0.00, 34.69, 20.81, 13.88), c(0xFFFFFF, 0xF4B5CB, 0xCB58C2, 0x9D2D95))));
				r(32, bread = v(t("bread", 2, 1, 35, m(0.00, 0.00, 0.00, 12.18, 10.96, 9.74), c(0xCC850C, 0x9E7325, 0x654B17, 0x3F2E0E))));
				r(33, daytime = v(t("daytime", 1, 1, 88, m(2.12, 0.00, 21.22, 14.85, 14.85, 12.73), c(0xFFE260, 0xAACAEF, 0x84B5EF, 0xFFE7B2))));
				r(34, night = v(t("night", 1, 1, 83, m(0.00, 10.22, 14.61, 0.00, 10.22, 37.98), c(0xFFE260, 0x2C2C2E, 0x0E0E10, 0x2D4272))));
				r(35, morning = v(t("morning", 2, 1, 85, m(1.44, 11.48, 14.35, 11.48, 17.22, 11.48), c(0xFFE260, 0x91C4D9, 0x4570A6, 0xFF7017))));
				r(36, fine = v(t("fine", 1, 1, 22, m(0.84, 0.00, 8.42, 0.00, 10.11, 0.00), c(0xB4FFFF, 0xAACAEF, 0x84B5EF, 0xffe7b2))));
				r(37, rain = v(t("rain", 2, 1, 25, m(0.00, 0.00, 12.04, 0.00, 14.45, 0.00), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0x2D40F4))));
				r(38, plains = v(t("plains", 1, 1, 79, m(0.00, 0.00, 0.00, 6.92, 41.50, 23.06), c(0x80FF00, 0xD4FF82, 0x86C91C, 0xBB5400))));
				r(39, forest = v(t("forest", 2, 1, 83, m(0.00, 0.00, 3.18, 19.06, 50.83, 15.89), c(0x80FF00, 0x7B9C62, 0x89591D, 0x2E6E14))));
			}

			variants = ImmutableArray.ofList(variants2);
		}

		private void r(int id, VariantMirageFairy variant)
		{
			variants2.add(Tuple.of(id, variant));
		}

		private VariantMirageFairy v(MirageFairyType type)
		{
			return new VariantMirageFairy("mirage_fairy_" + type.name, "mirageFairy" + Utils.toUpperCaseHead(type.name), "mirageFairy." + type.name, type);
		}

		private MirageFairyType t(String name, int rare, int rank, int cost, MirageFairyManaSet manaSet, MirageFairyColorSet colorSet)
		{
			return new MirageFairyType(ModMirageFairy2019.MODID, name, rare, rank, cost, manaSet, colorSet);
		}

		private MirageFairyManaSet m(double shine, double fire, double wind, double gaia, double aqua, double dark)
		{
			return new MirageFairyManaSet(shine, fire, wind, gaia, aqua, dark);
		}

		private MirageFairyColorSet c(int skin, int bright, int dark, int hair)
		{
			return new MirageFairyColorSet(skin, bright, dark, hair);
		}

	}

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 妖精
			ApiFairy.itemMirageFairy = itemMirageFairy = new ItemMirageFairy();
			itemMirageFairy.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy");
			itemMirageFairy.setUnlocalizedName("mirageFairy");
			itemMirageFairy.setCreativeTab(ApiMain.creativeTab);
			new FairyTypes().init();
			for (Tuple<Integer, VariantMirageFairy> tuple : FairyTypes.variants) {
				itemMirageFairy.registerVariant(tuple.x, tuple.y);
			}
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
			if (ApiMain.side.isClient()) {
				ModelLoader.setCustomModelResourceLocation(itemFairyCrystal, 0, new ModelResourceLocation(itemFairyCrystal.getRegistryName(), null));
			}

		});
		erMod.registerItemColorHandler.register(() -> {

			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int colorMultiplier(ItemStack stack, int tintIndex)
				{
					VariantMirageFairy variant = itemMirageFairy.getVariant(stack).orElse(null);
					if (tintIndex == 0) return variant.type.colorSet.skin;
					if (tintIndex == 1) return 0x8888ff;
					if (tintIndex == 2) return variant.type.colorSet.dark;
					if (tintIndex == 3) return variant.type.colorSet.bright;
					if (tintIndex == 4) return variant.type.colorSet.hair;
					return 0xFFFFFF;
				}
			}, itemMirageFairy);

		});
		erMod.createItemStack.register(ic -> {
			ApiFairy.itemStackMirageFairyMain = FairyTypes.magentaglazedterracotta.createItemStack();
			ApiFairy.itemStackFairyCrystal = new ItemStack(itemFairyCrystal);
		});
	}

}
