package miragefairy2019.mod.modules.sphere;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiSphere;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Utils;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class ModuleSphere
{

	public static ItemSpheres itemSpheres;

	public static VariantSphere variantSphereAttack;
	public static VariantSphere variantSphereCraft;
	public static VariantSphere variantSphereFell;
	public static VariantSphere variantSphereLight;
	public static VariantSphere variantSphereFlame;
	public static VariantSphere variantSphereWater;
	public static VariantSphere variantSphereCrystal;
	public static VariantSphere variantSphereArt;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// スフィアのアイテム登録
			{
				ItemSpheres item = new ItemSpheres();
				item.setRegistryName(ModMirageFairy2019.MODID, "spheres");
				item.setUnlocalizedName("spheres");
				item.setCreativeTab(ApiMain.creativeTab);
				item.registerVariant(0, variantSphereAttack = new VariantSphere(EnumSphere.attack));
				item.registerVariant(1, variantSphereCraft = new VariantSphere(EnumSphere.craft));
				item.registerVariant(2, variantSphereFell = new VariantSphere(EnumSphere.fell));
				item.registerVariant(3, variantSphereLight = new VariantSphere(EnumSphere.light));
				item.registerVariant(4, variantSphereFlame = new VariantSphere(EnumSphere.flame));
				item.registerVariant(5, variantSphereWater = new VariantSphere(EnumSphere.water));
				item.registerVariant(6, variantSphereCrystal = new VariantSphere(EnumSphere.crystal));
				item.registerVariant(7, variantSphereArt = new VariantSphere(EnumSphere.art));
				ForgeRegistries.ITEMS.register(item);
				if (ApiMain.side.isClient()) {
					for (Tuple<Integer, VariantSphere> tuple : item.getVariants()) {
						ModelLoader.setCustomModelResourceLocation(
							item,
							tuple.x,
							new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "sphere"), null));
					}
				}
				ApiSphere.itemSpheres = itemSpheres = item;
			}

		});
		erMod.registerItemColorHandler.register(new Runnable() {
			@SideOnly(Side.CLIENT)
			@Override
			public void run()
			{

				// スフィアのカスタム色
				@SideOnly(Side.CLIENT)
				class ItemColorImpl implements IItemColor
				{

					@Override
					public int colorMultiplier(ItemStack stack, int tintIndex)
					{
						VariantSphere variant = itemSpheres.getVariant(stack).orElse(null);
						if (variant == null) return 0xFFFFFF;
						if (tintIndex == 0) return variant.sphere.colorBackground;
						if (tintIndex == 1) return variant.sphere.colorPlasma;
						if (tintIndex == 2) return variant.sphere.colorCore;
						if (tintIndex == 3) return variant.sphere.colorHighlight;
						return 0xFFFFFF;
					}

				}
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(), itemSpheres);

			}
		});
		erMod.createItemStack.register(ic -> {

			// スフィアの鉱石辞書
			for (Tuple<Integer, VariantSphere> tuple : itemSpheres.getVariants()) {
				OreDictionary.registerOre("mirageFairy2019Sphere" + Utils.toUpperCaseHead(tuple.y.sphere.abilityType.getName()), tuple.y.createItemStack());
			}

		});
		erMod.addRecipe.register(() -> {

			for (Tuple<Integer, VariantSphere> variant : itemSpheres.getVariants()) {
				GameRegistry.addShapelessRecipe(
					new ResourceLocation(ModMirageFairy2019.MODID + ":" + variant.y.sphere.abilityType.getName() + "_sphere"),
					new ResourceLocation(ModMirageFairy2019.MODID + ":" + variant.y.sphere.abilityType.getName() + "_sphere"),
					variant.y.createItemStack(),
					Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET)),
					new OreIngredient("dustMiragium"),
					new OreIngredient("gemFluorite"),
					new OreIngredient("mirageFairy2019FairyAbility" + Utils.toUpperCaseHead(variant.y.sphere.abilityType.getName())),
					variant.y.sphere.sIngredient.get());
			}

		});
	}

}
