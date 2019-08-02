package miragefairy2019.mod.modules.fairy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.fairy.IMirageFairyAbilityType;
import miragefairy2019.mod.api.fairy.MirageFairyAbilitySet;
import miragefairy2019.mod.api.fairy.MirageFairyColorSet;
import miragefairy2019.mod.api.fairy.MirageFairyManaSet;
import miragefairy2019.mod.api.fairy.MirageFairyType;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import miragefairy2019.mod.modules.fairy.ItemFairyCrystal.Drop;
import miragefairy2019.mod.modules.fairyweapon.ItemFairySword;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyWandCrafting;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
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
	public static ItemMultiMaterial<ItemVariantMaterial> itemMaterialsFairy;
	public static ItemMirageSpheres itemMirageSpheres;
	public static ItemFairyWandCrafting itemFairyWandCrafting;
	public static ItemFairySword itemFairySword;

	//public static ItemVariant variantBucketFairyWater;

	public static VariantAbility variantMirageSphereAttack;
	public static VariantAbility variantMirageSphereCraft;
	public static VariantAbility variantMirageSphereFell;
	public static VariantAbility variantMirageSphereLight;
	public static VariantAbility variantMirageSphereFlame;
	public static VariantAbility variantMirageSphereWater;

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
				if (true) throw null; // TODO
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
				.map(type -> new VariantMirageFairy(type))
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
					.map(tuple -> Tuple.of(tuple.x, tuple.y * rateRare))
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
		private final MirageFairyAbilitySet a(Tuple<IMirageFairyAbilityType, Double>... tuples)
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
		erMod.initCreativeTab.register(() -> {
			ApiFairy.creativeTab = new CreativeTabs("mirageFairy2019.fairy") {
				@Override
				@SideOnly(Side.CLIENT)
				public ItemStack getTabIconItem()
				{
					return ApiFairy.itemStackMirageFairyMain;
				}

				@SideOnly(value = Side.CLIENT)
				public void displayAllRelevantItems(NonNullList<ItemStack> itemStacks)
				{
					for (Tuple<Integer, VariantMirageFairy[]> variant : FairyTypes.variants) {
						for (int i = 0; i <= 4; i++) {
							itemStacks.add(variant.y[i].createItemStack());
						}
					}
				}
			};
		});
		erMod.registerItem.register(b -> {

			// 妖精タイプ登録
			new FairyTypes().init();

			// 妖精
			ApiFairy.itemMirageFairyR1 = itemMirageFairyR1 = new ItemMirageFairy();
			itemMirageFairyR1.setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy");
			itemMirageFairyR1.setUnlocalizedName("mirageFairy");
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

			// マテリアル
			ApiFairy.itemMaterialsFairy = itemMaterialsFairy = new ItemMultiMaterial<>();
			itemMaterialsFairy.setRegistryName(ModMirageFairy2019.MODID, "fairy_materials");
			itemMaterialsFairy.setUnlocalizedName("materialsFairy");
			itemMaterialsFairy.setCreativeTab(ApiMain.creativeTab);
			//itemMaterialsFairy.registerVariant(0, variantBucketFairyWater = new ItemVariant("fairy_water_bucket", "bucketFairyWater"));
			ForgeRegistries.ITEMS.register(itemMaterialsFairy);
			if (ApiMain.side.isClient()) itemMaterialsFairy.setCustomModelResourceLocations();

			// スフィア
			ApiFairy.itemMirageSpheres = itemMirageSpheres = new ItemMirageSpheres();
			itemMirageSpheres.setRegistryName(ModMirageFairy2019.MODID, "mirage_spheres");
			itemMirageSpheres.setUnlocalizedName("mirageSpheres");
			itemMirageSpheres.setCreativeTab(ApiMain.creativeTab);
			itemMirageSpheres.registerVariant(0, variantMirageSphereAttack = new VariantAbility(EnumMirageSphere.attack));
			itemMirageSpheres.registerVariant(1, variantMirageSphereCraft = new VariantAbility(EnumMirageSphere.craft));
			itemMirageSpheres.registerVariant(2, variantMirageSphereFell = new VariantAbility(EnumMirageSphere.fell));
			itemMirageSpheres.registerVariant(3, variantMirageSphereLight = new VariantAbility(EnumMirageSphere.light));
			itemMirageSpheres.registerVariant(4, variantMirageSphereFlame = new VariantAbility(EnumMirageSphere.flame));
			itemMirageSpheres.registerVariant(5, variantMirageSphereWater = new VariantAbility(EnumMirageSphere.water));
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

				// 妖精のカスタム色
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

				// スフィアのカスタム色
				{
					@SideOnly(Side.CLIENT)
					class ItemColorImpl implements IItemColor
					{

						@Override
						public int colorMultiplier(ItemStack stack, int tintIndex)
						{
							VariantAbility variant = itemMirageSpheres.getVariant(stack).orElse(null);
							if (variant == null) return 0xFFFFFF;
							if (tintIndex == 0) return variant.mirageSphere.colorBackground;
							if (tintIndex == 1) return variant.mirageSphere.colorPlasma;
							if (tintIndex == 2) return variant.mirageSphere.colorCore;
							if (tintIndex == 3) return variant.mirageSphere.colorHighlight;
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

			// 妖精の鉱石辞書
			for (Tuple<Integer, VariantMirageFairy[]> variant : FairyTypes.variants) {
				for (int i = 0; i <= 4; i++) {
					for (Tuple<IMirageFairyAbilityType, Double> tuple : variant.y[i].type.abilitySet.tuples) {
						if (tuple.y >= 10) {
							OreDictionary.registerOre(
								"mirageFairyAbility" + Utils.toUpperCaseHead(tuple.x.getName()),
								variant.y[i].createItemStack());
						}
					}
				}
			}

			// スフィアの鉱石辞書
			for (Tuple<Integer, VariantAbility> tuple : itemMirageSpheres.getVariants()) {
				OreDictionary.registerOre(tuple.y.getOreName(), tuple.y.createItemStack());
			}

			// 妖晶の鉱石辞書
			OreDictionary.registerOre("mirageFairyCrystal", new ItemStack(itemFairyCrystal));

			// 妖晶ドロップ登録
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

			// 凝縮・分散レシピ
			for (Tuple<Integer, VariantMirageFairy[]> tuple : FairyTypes.variants) {

				for (int i = 0; i < 4; i++) {

					// 凝縮
					GameRegistry.addShapelessRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "condense_r" + i + "_" + tuple.y[i].getRegistryName()),
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

					// 分散
					GameRegistry.addShapelessRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i + "_" + tuple.y[i].getRegistryName()),
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i),
						tuple.y[i].createItemStack(9),
						Ingredient.fromStacks(tuple.y[i + 1].createItemStack()));

				}

			}

		});
	}

}
