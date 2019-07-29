package miragefairy2019.mod.modules.fairy;

import static miragefairy2019.mod.modules.fairy.EnumAbilityType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.multi.ItemMulti;
import miragefairy2019.mod.lib.multi.ItemVariant;
import miragefairy2019.mod.modules.fairy.ItemFairyCrystal.Drop;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairy
{

	public static ItemMirageFairy itemMirageFairyR1;
	public static ItemMirageFairy itemMirageFairyR2;
	public static ItemMirageFairy itemMirageFairyR3;
	public static ItemMirageFairy itemMirageFairyR4;
	public static ItemMirageFairy itemMirageFairyR5;
	public static ItemFairyCrystal itemFairyCrystal;
	public static ItemMulti<VariantAbility> itemMirageSpheres;

	public static VariantAbility variantMirageSphereAttack;
	public static VariantAbility variantMirageSphereCraft;

	public static class FairyTypes
	{

		public static ImmutableArray<Tuple<Integer, VariantMirageFairy[]>> variants;

		public static VariantMirageFairy[] air;
		public static VariantMirageFairy[] water;
		public static VariantMirageFairy[] fire;
		public static VariantMirageFairy[] sun;
		public static VariantMirageFairy[] stone;
		public static VariantMirageFairy[] dirt;
		public static VariantMirageFairy[] iron;
		public static VariantMirageFairy[] diamond;
		public static VariantMirageFairy[] redstone;
		public static VariantMirageFairy[] enderman;
		public static VariantMirageFairy[] moon;
		public static VariantMirageFairy[] sand;
		public static VariantMirageFairy[] gold;
		public static VariantMirageFairy[] spider;
		public static VariantMirageFairy[] skeleton;
		public static VariantMirageFairy[] zombie;
		public static VariantMirageFairy[] creeper;
		public static VariantMirageFairy[] wheat;
		public static VariantMirageFairy[] lilac;
		public static VariantMirageFairy[] torch;
		public static VariantMirageFairy[] lava;
		public static VariantMirageFairy[] star;
		public static VariantMirageFairy[] gravel;
		public static VariantMirageFairy[] emerald;
		public static VariantMirageFairy[] lapislazuli;
		public static VariantMirageFairy[] enderdragon;
		public static VariantMirageFairy[] witherskeleton;
		public static VariantMirageFairy[] wither;
		public static VariantMirageFairy[] thunder;
		public static VariantMirageFairy[] chicken;
		public static VariantMirageFairy[] furnace;
		public static VariantMirageFairy[] magentaglazedterracotta;
		public static VariantMirageFairy[] bread;
		public static VariantMirageFairy[] daytime;
		public static VariantMirageFairy[] night;
		public static VariantMirageFairy[] morning;
		public static VariantMirageFairy[] fine;
		public static VariantMirageFairy[] rain;
		public static VariantMirageFairy[] plains;
		public static VariantMirageFairy[] forest;

		//

		private List<Tuple<Integer, VariantMirageFairy[]>> variants2 = new ArrayList<>();

		protected void init()
		{
			{
				r(0, air = v(t("air", 1, 15, m(1, 0, 10, 0, 0, 0), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xFFBE80, 0xDEFFFF, 0xDEFFFF, 0xB0FFFF))));
				r(1, water = v(t("water", 1, 50, m(0, 0, 0, 1, 10, 6), a(e(ATTACK, 4), e(CRAFT, 0)), c(0x5469F2, 0x5985FF, 0x172AD3, 0x2D40F4))));
				r(2, fire = v(t("fire", 2, 20, m(1, 10, 0, 0, 0, 0), a(e(ATTACK, 9), e(CRAFT, 4)), c(0xFF6C01, 0xF9DFA4, 0xFF7324, 0xFF4000))));
				r(3, sun = v(t("sun", 5, 99, m(10, 28, 24, 18, 18, 0), a(e(ATTACK, 5), e(CRAFT, 2)), c(0xff2f00, 0xff972b, 0xff7500, 0xffe7b2))));
				r(4, stone = v(t("stone", 1, 83, m(0, 0, 0, 10, 0, 15), a(e(ATTACK, 6), e(CRAFT, 1)), c(0x333333, 0x8F8F8F, 0x686868, 0x747474))));
				r(5, dirt = v(t("dirt", 1, 70, m(0, 0, 0, 10, 3, 28), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xB87440, 0xB9855C, 0x593D29, 0x914A18))));
				r(6, iron = v(t("iron", 2, 86, m(0, 3, 0, 10, 4, 6), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xA0A0A0, 0xD8D8D8, 0x727272, 0xD8AF93))));
				r(7, diamond = v(t("diamond", 4, 76, m(10, 13, 21, 26, 0, 0), a(e(ATTACK, 0), e(CRAFT, 0)), c(0x97FFE3, 0xD1FAF3, 0x70FFD9, 0x30DBBD))));
				r(8, redstone = v(t("redstone", 3, 54, m(1, 35, 11, 10, 0, 6), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xFF5959, 0xFF0000, 0xCD0000, 0xBA0000))));
				r(9, enderman = v(t("enderman", 4, 48, m(1, 18, 18, 12, 10, 0), a(e(ATTACK, 11), e(CRAFT, 7)), c(0x000000, 0x161616, 0x161616, 0xEF84FA))));
				r(10, moon = v(t("moon", 5, 95, m(10, 24, 28, 9, 9, 25), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xD9E4FF, 0x747D93, 0x0C121F, 0x2D4272))));
				r(11, sand = v(t("sand", 1, 64, m(1, 0, 0, 10, 0, 16), a(e(ATTACK, 2), e(CRAFT, 1)), c(0xB87440, 0xEEE4B6, 0xC2BC84, 0xD8D09B))));
				r(12, gold = v(t("gold", 3, 93, m(1, 0, 9, 10, 0, 0), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xA0A0A0, 0xFFFF0B, 0xDC7613, 0xDEDE00))));
				r(13, spider = v(t("spider", 2, 43, m(0, 0, 0, 0, 10, 4), a(e(ATTACK, 10), e(CRAFT, 0)), c(0x494422, 0x61554A, 0x52483F, 0xA80E0E))));
				r(14, skeleton = v(t("skeleton", 1, 49, m(0, 8, 10, 5, 0, 8), a(e(ATTACK, 12), e(CRAFT, 5)), c(0xCACACA, 0xCFCFCF, 0xCFCFCF, 0x494949))));
				r(15, zombie = v(t("zombie", 1, 55, m(0, 9, 10, 0, 2, 9), a(e(ATTACK, 13), e(CRAFT, 0)), c(0x2B4219, 0x00AAAA, 0x322976, 0x2B4219))));
				r(16, creeper = v(t("creeper", 2, 35, m(0, 0, 10, 5, 12, 4), a(e(ATTACK, 10), e(CRAFT, 0)), c(0x5BAA53, 0xD6FFCF, 0x5EE74C, 0x000000))));
				r(17, wheat = v(t("wheat", 2, 31, m(0, 0, 0, 0, 10, 5), a(e(ATTACK, 0), e(CRAFT, 0)), c(0x168700, 0xD5DA45, 0x716125, 0x9E8714))));
				r(18, lilac = v(t("lilac", 3, 28, m(0, 0, 1, 1, 10, 0), a(e(ATTACK, 0), e(CRAFT, 0)), c(0x63D700, 0xF0C9FF, 0xDC8CE6, 0xA22CFF))));
				r(19, torch = v(t("torch", 1, 19, m(1, 5, 1, 10, 4, 0), a(e(ATTACK, 1), e(CRAFT, 0)), c(0xFFFFFF, 0xFFC52C, 0xFF5800, 0xFFE6A5))));
				r(20, lava = v(t("lava", 2, 58, m(0, 22, 0, 8, 10, 0), a(e(ATTACK, 7), e(CRAFT, 3)), c(0xCD4208, 0xEDB54A, 0xCC4108, 0x4C1500))));
				r(21, star = v(t("star", 4, 98, m(10, 16, 16, 9, 9, 38), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xffffff, 0x2C2C2E, 0x0E0E10, 0x191919))));
				r(22, gravel = v(t("gravel", 2, 77, m(0, 0, 0, 10, 0, 8), a(e(ATTACK, 3), e(CRAFT, 0)), c(0x333333, 0xC0B5B6, 0x968B8E, 0x63565C))));
				r(23, emerald = v(t("emerald", 4, 73, m(10, 16, 15, 0, 0, 36), a(e(ATTACK, 0), e(CRAFT, 0)), c(0x9FF9B5, 0x81F99E, 0x17DD62, 0x008A25))));
				r(24, lapislazuli = v(t("lapislazuli", 4, 62, m(2, 0, 8, 10, 18, 0), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xA2B7E8, 0x4064EC, 0x224BD5, 0x0A33C2))));
				r(25, enderdragon = v(t("enderdragon", 5, 61, m(3, 0, 34, 0, 10, 0), a(e(ATTACK, 20), e(CRAFT, 0)), c(0x000000, 0x181818, 0x181818, 0xA500E2))));
				r(26, witherskeleton = v(t("witherskeleton", 4, 69, m(0, 11, 10, 4, 1, 0), a(e(ATTACK, 17), e(CRAFT, 6)), c(0x505252, 0x1C1C1C, 0x1C1C1C, 0x060606))));
				r(27, wither = v(t("wither", 5, 52, m(0, 8, 10, 3, 1, 1), a(e(ATTACK, 25), e(CRAFT, 0)), c(0x181818, 0x3C3C3C, 0x141414, 0x557272))));
				r(28, thunder = v(t("thunder", 3, 18, m(3, 10, 3, 8, 0, 0), a(e(ATTACK, 8), e(CRAFT, 2)), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0xFFEB00))));
				r(29, chicken = v(t("chicken", 1, 39, m(0, 0, 1, 0, 10, 7), a(e(ATTACK, 1), e(CRAFT, 0)), c(0xFFDFA3, 0xFFFFFF, 0xFFFFFF, 0xD93117))));
				r(30, furnace = v(t("furnace", 2, 72, m(0, 2, 0, 10, 0, 11), a(e(ATTACK, 1), e(CRAFT, 13)), c(0xFFFFFF, 0xFF7F19, 0x8E8E8E, 0x383838))));
				r(31, magentaglazedterracotta = v(t("magentaglazedterracotta", 3, 60, m(0, 1, 0, 10, 6, 4), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xFFFFFF, 0xF4B5CB, 0xCB58C2, 0x9D2D95))));
				r(32, bread = v(t("bread", 2, 35, m(0, 0, 0, 10, 9, 8), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xCC850C, 0x9E7325, 0x654B17, 0x3F2E0E))));
				r(33, daytime = v(t("daytime", 1, 88, m(1, 0, 10, 7, 7, 6), a(e(ATTACK, 0), e(CRAFT, 1)), c(0xFFE260, 0xAACAEF, 0x84B5EF, 0xFFE7B2))));
				r(34, night = v(t("night", 1, 83, m(0, 7, 10, 0, 7, 26), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xFFE260, 0x2C2C2E, 0x0E0E10, 0x2D4272))));
				r(35, morning = v(t("morning", 2, 85, m(1, 8, 10, 8, 12, 8), a(e(ATTACK, 0), e(CRAFT, 0)), c(0xFFE260, 0x91C4D9, 0x4570A6, 0xFF7017))));
				r(36, fine = v(t("fine", 1, 22, m(1, 0, 10, 0, 12, 0), a(e(ATTACK, 0), e(CRAFT, 1)), c(0xB4FFFF, 0xAACAEF, 0x84B5EF, 0xffe7b2))));
				r(37, rain = v(t("rain", 2, 25, m(0, 0, 10, 0, 12, 0), a(e(ATTACK, 1), e(CRAFT, 0)), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0x2D40F4))));
				r(38, plains = v(t("plains", 1, 79, m(0, 0, 0, 3, 18, 10), a(e(ATTACK, 0), e(CRAFT, 0)), c(0x80FF00, 0xD4FF82, 0x86C91C, 0xBB5400))));
				r(39, forest = v(t("forest", 2, 83, m(0, 0, 2, 12, 32, 10), a(e(ATTACK, 1), e(CRAFT, 0)), c(0x80FF00, 0x7B9C62, 0x89591D, 0x2E6E14))));
			}

			variants = ImmutableArray.ofList(variants2);
		}

		private void r(int id, VariantMirageFairy[] variants)
		{
			variants2.add(Tuple.of(id, variants));
		}

		private VariantMirageFairy[] v(MirageFairyType[] types)
		{
			return ISuppliterator.ofObjArray(types)
				.map(type -> {
					return new VariantMirageFairy("mirage_fairy_" + type.name, "mirageFairy" + Utils.toUpperCaseHead(type.name), "mirageFairy." + type.name, type);
				})
				.toArray(VariantMirageFairy[]::new);
		}

		private MirageFairyType[] t(String name, int rare, int cost, MirageFairyManaSet manaSet, MirageFairyAbilitySet abilitySet, MirageFairyColorSet colorSet)
		{
			IntFunction<MirageFairyType> f = rank -> {

				double rateRare = Math.pow(2, (rare + rank - 2) / 4.0);
				double rateVariance = Math.pow(0.5, ((manaSet.shine / manaSet.max + manaSet.fire / manaSet.max + manaSet.wind / manaSet.max
					+ manaSet.gaia / manaSet.max + manaSet.aqua / manaSet.max + manaSet.dark / manaSet.max) - 1) / 5.0);
				double sum = cost * rateRare * rateVariance;
				MirageFairyManaSet manaSetReal = new MirageFairyManaSet(
					manaSet.shine * sum / manaSet.sum,
					manaSet.fire * sum / manaSet.sum,
					manaSet.wind * sum / manaSet.sum,
					manaSet.gaia * sum / manaSet.sum,
					manaSet.aqua * sum / manaSet.sum,
					manaSet.dark * sum / manaSet.sum);

				MirageFairyAbilitySet abilitySetReal = new MirageFairyAbilitySet(abilitySet.tuples.suppliterator()
					.map(tuple -> Tuple.of(tuple.x, tuple.y * rateRare * rateVariance))
					.toImmutableArray());

				return new MirageFairyType(ModMirageFairy2019.MODID, name, rare, rank, cost, manaSetReal, abilitySetReal, colorSet);
			};
			return new MirageFairyType[] {
				f.apply(1),
				f.apply(2),
				f.apply(3),
				f.apply(4),
				f.apply(5),
			};
		}

		private MirageFairyManaSet m(double shine, double fire, double wind, double gaia, double aqua, double dark)
		{
			return new MirageFairyManaSet(shine, fire, wind, gaia, aqua, dark);
		}

		@SafeVarargs
		private final MirageFairyAbilitySet a(Tuple<EnumAbilityType, Double>... tuples)
		{
			return new MirageFairyAbilitySet(ImmutableArray.ofObjArray(tuples));
		}

		private Tuple<EnumAbilityType, Double> e(EnumAbilityType abilityType, double value)
		{
			return Tuple.of(abilityType, value);
		}

		private MirageFairyColorSet c(int skin, int bright, int dark, int hair)
		{
			return new MirageFairyColorSet(skin, bright, dark, hair);
		}

	}

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 妖精タイプ登録
			new FairyTypes().init();

			// 妖精
			ApiFairy.itemMirageFairyR1 = itemMirageFairyR1 = new ItemMirageFairy();
			itemMirageFairyR1.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy");
			itemMirageFairyR1.setUnlocalizedName("mirageFairy");
			itemMirageFairyR1.setCreativeTab(ApiMain.creativeTab);
			for (Tuple<Integer, VariantMirageFairy[]> tuple : FairyTypes.variants) {
				itemMirageFairyR1.registerVariant(tuple.x, tuple.y[0]);
			}
			ForgeRegistries.ITEMS.register(itemMirageFairyR1);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantMirageFairy> tuple : itemMirageFairyR1.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(itemMirageFairyR1, tuple.x, new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy"), null));
				}
			}

			// 妖精
			ApiFairy.itemMirageFairyR2 = itemMirageFairyR2 = new ItemMirageFairy();
			itemMirageFairyR2.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_r2");
			itemMirageFairyR2.setUnlocalizedName("mirageFairy_2");
			itemMirageFairyR2.setCreativeTab(ApiMain.creativeTab);
			for (Tuple<Integer, VariantMirageFairy[]> tuple : FairyTypes.variants) {
				itemMirageFairyR2.registerVariant(tuple.x, tuple.y[1]);
			}
			ForgeRegistries.ITEMS.register(itemMirageFairyR2);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantMirageFairy> tuple : itemMirageFairyR2.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(itemMirageFairyR2, tuple.x, new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy"), null));
				}
			}

			// 妖精
			ApiFairy.itemMirageFairyR3 = itemMirageFairyR3 = new ItemMirageFairy();
			itemMirageFairyR3.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_r3");
			itemMirageFairyR3.setUnlocalizedName("mirageFairy");
			itemMirageFairyR3.setCreativeTab(ApiMain.creativeTab);
			for (Tuple<Integer, VariantMirageFairy[]> tuple : FairyTypes.variants) {
				itemMirageFairyR3.registerVariant(tuple.x, tuple.y[2]);
			}
			ForgeRegistries.ITEMS.register(itemMirageFairyR3);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantMirageFairy> tuple : itemMirageFairyR3.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(itemMirageFairyR3, tuple.x, new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy"), null));
				}
			}

			// 妖精
			ApiFairy.itemMirageFairyR4 = itemMirageFairyR4 = new ItemMirageFairy();
			itemMirageFairyR4.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_r4");
			itemMirageFairyR4.setUnlocalizedName("mirageFairy");
			itemMirageFairyR4.setCreativeTab(ApiMain.creativeTab);
			for (Tuple<Integer, VariantMirageFairy[]> tuple : FairyTypes.variants) {
				itemMirageFairyR4.registerVariant(tuple.x, tuple.y[3]);
			}
			ForgeRegistries.ITEMS.register(itemMirageFairyR4);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantMirageFairy> tuple : itemMirageFairyR4.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(itemMirageFairyR4, tuple.x, new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy"), null));
				}
			}

			// 妖精
			ApiFairy.itemMirageFairyR5 = itemMirageFairyR5 = new ItemMirageFairy();
			itemMirageFairyR5.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_r5");
			itemMirageFairyR5.setUnlocalizedName("mirageFairy");
			itemMirageFairyR5.setCreativeTab(ApiMain.creativeTab);
			for (Tuple<Integer, VariantMirageFairy[]> tuple : FairyTypes.variants) {
				itemMirageFairyR5.registerVariant(tuple.x, tuple.y[4]);
			}
			ForgeRegistries.ITEMS.register(itemMirageFairyR5);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantMirageFairy> tuple : itemMirageFairyR5.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(itemMirageFairyR5, tuple.x, new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy"), null));
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

			// スフィア
			ApiFairy.itemMirageSpheres = itemMirageSpheres = new ItemMulti<>();
			itemMirageSpheres.setRegistryName(ModMirageFairy2019.MODID, "mirage_spheres");
			itemMirageSpheres.setUnlocalizedName("mirageSpheres");
			itemMirageSpheres.setCreativeTab(ApiMain.creativeTab);
			itemMirageSpheres.registerVariant(0, variantMirageSphereAttack = new VariantAbility("attack_mirage_sphere", "mirageSphereAttack", EnumAbilityType.ATTACK));
			itemMirageSpheres.registerVariant(1, variantMirageSphereCraft = new VariantAbility("craft_mirage_sphere", "mirageSphereCraft", EnumAbilityType.CRAFT));
			ForgeRegistries.ITEMS.register(itemMirageSpheres);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantAbility> tuple : itemMirageSpheres.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(
						itemMirageSpheres,
						tuple.x,
						new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_sphere"), null));
				}
			}

		});
		erMod.registerItemColorHandler.register(new Runnable() {
			@SideOnly(Side.CLIENT)
			@Override
			public void run()
			{
				{
					@SideOnly(Side.CLIENT)
					class ItemColorImpl implements IItemColor
					{

						private final ItemMirageFairy itemMirageFairy;
						private final int colorCloth;

						public ItemColorImpl(ItemMirageFairy itemMirageFairy, int colorCloth)
						{
							this.itemMirageFairy = itemMirageFairy;
							this.colorCloth = colorCloth;
						}

						@Override
						public int colorMultiplier(ItemStack stack, int tintIndex)
						{
							VariantMirageFairy variant = itemMirageFairy.getVariant(stack).orElse(null);
							if (variant == null) return 0xFFFFFF;
							if (tintIndex == 0) return variant.type.colorSet.skin;
							if (tintIndex == 1) return colorCloth;
							if (tintIndex == 2) return variant.type.colorSet.dark;
							if (tintIndex == 3) return variant.type.colorSet.bright;
							if (tintIndex == 4) return variant.type.colorSet.hair;
							return 0xFFFFFF;
						}

					}
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(itemMirageFairyR1, 0x888888), itemMirageFairyR1);
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(itemMirageFairyR2, 0xFF8888), itemMirageFairyR2);
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(itemMirageFairyR3, 0x8888FF), itemMirageFairyR3);
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(itemMirageFairyR4, 0x88FF88), itemMirageFairyR4);
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(itemMirageFairyR5, 0xFFFF88), itemMirageFairyR5);
				}
				{
					@SideOnly(Side.CLIENT)
					class ItemColorImpl implements IItemColor
					{

						@Override
						public int colorMultiplier(ItemStack stack, int tintIndex)
						{
							VariantAbility variant = itemMirageSpheres.getVariant(stack).orElse(null);
							if (variant == null) return 0xFFFFFF;
							if (tintIndex == 0) return variant.abilityType.background;
							if (tintIndex == 1) return variant.abilityType.plasma;
							if (tintIndex == 2) return variant.abilityType.core;
							if (tintIndex == 3) return variant.abilityType.highlight;
							return 0xFFFFFF;
						}

					}
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(), itemMirageSpheres);
				}
			}
		});
		erMod.createItemStack.register(ic -> {
			ApiFairy.itemStackMirageFairyMain = FairyTypes.magentaglazedterracotta[0].createItemStack();
			ApiFairy.itemStackFairyCrystal = new ItemStack(itemFairyCrystal);

			// 鉱石辞書登録
			for (Tuple<Integer, VariantMirageFairy[]> variant : FairyTypes.variants) {
				for (int i = 0; i < 4; i++) {
					for (Tuple<EnumAbilityType, Double> tuple : variant.y[i].type.abilitySet.tuples) {
						if (tuple.y >= 10) {
							OreDictionary.registerOre(
								"mirageFairyAbility" + Utils.toUpperCaseHead(tuple.x.name().toLowerCase()),
								variant.y[i].createItemStack());
						}
					}
				}
			}

			// ドロップ登録
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
		erMod.addRecipe.register(() -> {
			for (Tuple<Integer, VariantMirageFairy[]> tuple : FairyTypes.variants) {

				for (int i = 0; i < 4; i++) {

					GameRegistry.addShapelessRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "condense_r" + i + "_" + tuple.y[i].registryName),
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "condense_r" + i),
						tuple.y[i + 1].createItemStack(),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()));
					GameRegistry.addShapelessRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i + "_" + tuple.y[i].registryName),
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i),
						tuple.y[i].createItemStack(9),
						Ingredient.fromStacks(tuple.y[i + 1].createItemStack()));

				}

			}
		});
	}

}
