package miragefairy2019.mod.modules.sphere;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiSphere;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleSphere
{

	public static ItemSpheres itemSpheres;

	public static VariantSphere variantSphereAttack;
	public static VariantSphere variantSphereCraft;
	public static VariantSphere variantSphereFell;
	public static VariantSphere variantSphereLight;
	public static VariantSphere variantSphereFlame;
	public static VariantSphere variantSphereWater;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// スフィアのアイテム登録
			ApiSphere.itemSpheres = itemSpheres = new ItemSpheres();
			itemSpheres.setRegistryName(ModMirageFairy2019.MODID, "spheres");
			itemSpheres.setUnlocalizedName("spheres");
			itemSpheres.setCreativeTab(ApiMain.creativeTab);
			itemSpheres.registerVariant(0, variantSphereAttack = new VariantSphere(EnumSphere.attack));
			itemSpheres.registerVariant(1, variantSphereCraft = new VariantSphere(EnumSphere.craft));
			itemSpheres.registerVariant(2, variantSphereFell = new VariantSphere(EnumSphere.fell));
			itemSpheres.registerVariant(3, variantSphereLight = new VariantSphere(EnumSphere.light));
			itemSpheres.registerVariant(4, variantSphereFlame = new VariantSphere(EnumSphere.flame));
			itemSpheres.registerVariant(5, variantSphereWater = new VariantSphere(EnumSphere.water));
			ForgeRegistries.ITEMS.register(itemSpheres);
			if (ApiMain.side.isClient()) {
				for (Tuple<Integer, VariantSphere> tuple : itemSpheres.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(
						itemSpheres,
						tuple.x,
						new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "sphere"), null));
				}
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
				OreDictionary.registerOre(tuple.y.getOreName(), tuple.y.createItemStack());
			}

		});
	}

}
