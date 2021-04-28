package miragefairy2019.mod.modules.materialsfairy;

import static miragefairy2019.mod.api.composite.ApiComposite.*;
import static miragefairy2019.mod.api.composite.Components.*;
import static miragefairy2019.mod.lib.Configurator.*;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.materialsfairy.ApiMaterialsFairy;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.ItemBlockMulti;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleMaterialsFairy
{

	public static BlockTwinkleStone blockTwinkleStone;
	public static ItemBlockMulti<BlockTwinkleStone, EnumVariantTwinkleStone> itemBlockTwinkleStone;

	public static void init(EventRegistryMod erMod)
	{

		// マテリアル
		item(erMod, ItemMultiMaterial<ItemVariantMaterial>::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_materials"), "materialsFairy")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(c -> {

				itemVariant(c.erMod, c, 0, () -> new ItemVariantMaterial("shine_mana_rod", "manaRodShine"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(moonstone.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodShine"));

				itemVariant(c.erMod, c, 1, () -> new ItemVariantMaterial("fire_mana_rod", "manaRodFire"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(cinnabar.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodFire"));

				itemVariant(c.erMod, c, 2, () -> new ItemVariantMaterial("wind_mana_rod", "manaRodWind"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(fluorite.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodWind"));

				itemVariant(c.erMod, c, 3, () -> new ItemVariantMaterial("gaia_mana_rod", "manaRodGaia"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(sulfur.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodGaia"));

				itemVariant(c.erMod, c, 4, () -> new ItemVariantMaterial("aqua_mana_rod", "manaRodAqua"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(apatite.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodAqua"));

				itemVariant(c.erMod, c, 5, () -> new ItemVariantMaterial("dark_mana_rod", "manaRodDark"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(magnetite.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodDark"));

				itemVariant(c.erMod, c, 6, () -> new ItemVariantMaterial("quartz_mana_rod", "manaRodQuartz"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(quartz.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodQuartz"));

				itemVariant(c.erMod, c, 7, () -> new ItemVariantMaterial("mirage_flower_stick", "stickMirageFlower"))
					.bind(addOreName("stickMirageFlower"));

				erMod.registerItem.register(b -> {
					if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();
				});

				return Monad.of(c);
			});

		erMod.registerBlock.register(b -> {

			// トゥインクルストーン
			ApiMaterialsFairy.blockTwinkleStone = blockTwinkleStone = new BlockTwinkleStone();
			blockTwinkleStone.setRegistryName(ModMirageFairy2019.MODID, "twinkle_stone");
			blockTwinkleStone.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockTwinkleStone);

		});
		erMod.registerItem.register(b -> {

			// トゥインクルストーン
			ApiMaterialsFairy.itemBlockTwinkleStone = itemBlockTwinkleStone = new ItemBlockMulti<>(blockTwinkleStone);
			itemBlockTwinkleStone.setRegistryName(ModMirageFairy2019.MODID, "twinkle_stone");
			itemBlockTwinkleStone.setUnlocalizedName("twinkle_stone");
			itemBlockTwinkleStone.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.ITEMS.register(itemBlockTwinkleStone);
			if (ApiMain.side().isClient()) {
				for (EnumVariantTwinkleStone variant : blockTwinkleStone.variantList) {
					ModelLoader.setCustomModelResourceLocation(
						itemBlockTwinkleStone,
						variant.getMetadata(),
						new ModelResourceLocation(new ResourceLocation(itemBlockTwinkleStone.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
				}
			}

		});
		erMod.createItemStack.register(ic -> {
			for (EnumVariantTwinkleStone variant : EnumVariantTwinkleStone.values()) {
				for (String oreName : variant.oreNames) {
					OreDictionary.registerOre(oreName, new ItemStack(itemBlockTwinkleStone, 1, variant.metadata));
				}
			}
		});

	}

}
