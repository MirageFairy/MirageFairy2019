package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.api.composite.ApiComposite.*;
import static miragefairy2019.mod.api.composite.Components.*;
import static miragefairy2019.mod.lib.Configurator.*;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.IBlockVariant;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import miragefairy2019.mod.modules.ore.material.BlockMaterials;
import miragefairy2019.mod.modules.ore.material.EnumVariantMaterials1;
import miragefairy2019.mod.modules.ore.material.ItemBlockMaterials;
import miragefairy2019.mod.modules.ore.ore.BlockOre;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;
import miragefairy2019.mod.modules.ore.ore.IBlockVariantOre;
import miragefairy2019.mod.modules.ore.ore.ItemBlockOre;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleOre
{

	public static BlockOre<EnumVariantOre1> blockOre1;
	public static BlockMaterials<EnumVariantMaterials1> blockMaterials1;

	public static ItemBlockOre<EnumVariantOre1> itemBlockOre1;
	public static ItemBlockMaterials<EnumVariantMaterials1> itemBlockMaterials1;

	public static void init(EventRegistryMod erMod)
	{

		LoaderOreSeedDrop.loadOreSeedDrop();

		// マテリアル
		item(erMod, ItemMultiMaterial<ItemVariantMaterial>::new, new ResourceLocation(ModMirageFairy2019.MODID, "materials"), "materials")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(c -> {

				itemVariant(c.erMod, c, 0, () -> new ItemVariantMaterial("apatite_gem", "gemApatite"))
					.bind(addComponent(instance(apatite.get(), 1.000)))
					.bind(addOreName("gemApatite"));

				itemVariant(c.erMod, c, 1, () -> new ItemVariantMaterial("fluorite_gem", "gemFluorite"))
					.bind(addComponent(instance(fluorite.get(), 1.000)))
					.bind(addOreName("gemFluorite"));

				itemVariant(c.erMod, c, 2, () -> new ItemVariantMaterial("sulfur_gem", "gemSulfur"))
					.bind(addComponent(instance(sulfur.get(), 1.000)))
					.bind(addOreName("gemSulfur"));

				itemVariant(c.erMod, c, 3, () -> new ItemVariantMaterial("miragium_dust", "dustMiragium"))
					.bind(addComponent(instance(miragium.get(), 1.000)))
					.bind(addOreName("dustMiragium"));

				itemVariant(c.erMod, c, 4, () -> new ItemVariantMaterial("miragium_tiny_dust", "dustTinyMiragium"))
					.bind(addComponent(instance(miragium.get(), 0.111)))
					.bind(addOreName("dustTinyMiragium"));

				itemVariant(c.erMod, c, 5, () -> new ItemVariantMaterial("miragium_ingot", "ingotMiragium"))
					.bind(addComponent(instance(miragium.get(), 1.000)))
					.bind(addOreName("ingotMiragium"));

				itemVariant(c.erMod, c, 6, () -> new ItemVariantMaterial("cinnabar_gem", "gemCinnabar"))
					.bind(addComponent(instance(cinnabar.get(), 1.000)))
					.bind(addOreName("gemCinnabar"));

				itemVariant(c.erMod, c, 7, () -> new ItemVariantMaterial("moonstone_gem", "gemMoonstone"))
					.bind(addComponent(instance(moonstone.get(), 1.000)))
					.bind(addOreName("gemMoonstone"));

				itemVariant(c.erMod, c, 8, () -> new ItemVariantMaterial("magnetite_gem", "gemMagnetite"))
					.bind(addComponent(instance(magnetite.get(), 1.000)))
					.bind(addOreName("gemMagnetite"));

				itemVariant(c.erMod, c, 9, () -> new ItemVariantMaterial("saltpeter_gem", "gemSaltpeter"))
					.bind(addComponent(instance(saltpeter.get(), 1.000)))
					.bind(addOreName("gemSaltpeter"));

				itemVariant(c.erMod, c, 10, () -> new ItemVariantMaterial("pyrope_gem", "gemPyrope"))
					.bind(addComponent(instance(pyrope.get(), 1.000)))
					.bind(addOreName("gemPyrope"));

				itemVariant(c.erMod, c, 11, () -> new ItemVariantMaterial("smithsonite_gem", "gemSmithsonite"))
					.bind(addComponent(instance(smithsonite.get(), 1.000)))
					.bind(addOreName("gemSmithsonite"));

				itemVariant(c.erMod, c, 12, () -> new ItemVariantMaterial("miragium_rod", "rodMiragium"))
					.bind(addComponent(instance(miragium.get(), 0.500)))
					.bind(addOreName("rodMiragium"));

				itemVariant(c.erMod, c, 13, () -> new ItemVariantMaterial("miragium_nugget", "nuggetMiragium"))
					.bind(addComponent(instance(miragium.get(), 0.111)))
					.bind(addOreName("nuggetMiragium"));

				erMod.registerItem.register(b -> {
					if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();
				});

				return Monad.of(c);
			});

		erMod.registerBlock.register(b -> {

			// 鉱石
			blockOre1 = new BlockOre<>(EnumVariantOre1.variantList);
			blockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
			blockOre1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockOre1);

			// ブロック
			blockMaterials1 = new BlockMaterials<>(EnumVariantMaterials1.variantList);
			blockMaterials1.setRegistryName(ModMirageFairy2019.MODID, "materials1");
			blockMaterials1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockMaterials1);

		});
		erMod.registerItem.register(b -> {

			// 鉱石
			itemBlockOre1 = new ItemBlockOre<>(blockOre1);
			itemBlockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
			itemBlockOre1.setUnlocalizedName("ore1");
			itemBlockOre1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.ITEMS.register(itemBlockOre1);
			if (ApiMain.side().isClient()) {
				for (IBlockVariantOre variant : blockOre1.variantList) {
					ModelLoader.setCustomModelResourceLocation(
						itemBlockOre1,
						variant.getMetadata(),
						new ModelResourceLocation(new ResourceLocation(itemBlockOre1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
				}
			}

			// ブロック
			itemBlockMaterials1 = new ItemBlockMaterials<>(blockMaterials1);
			itemBlockMaterials1.setRegistryName(ModMirageFairy2019.MODID, "materials1");
			itemBlockMaterials1.setUnlocalizedName("materials1");
			itemBlockMaterials1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.ITEMS.register(itemBlockMaterials1);
			if (ApiMain.side().isClient()) {
				for (IBlockVariant variant : blockMaterials1.variantList) {
					ModelLoader.setCustomModelResourceLocation(
						itemBlockMaterials1,
						variant.getMetadata(),
						new ModelResourceLocation(new ResourceLocation(itemBlockMaterials1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
				}
			}

		});
		erMod.createItemStack.register(ic -> {
			for (EnumVariantMaterials1 variant : EnumVariantMaterials1.values()) {
				OreDictionary.registerOre(variant.oreName, new ItemStack(itemBlockMaterials1, 1, variant.metadata));
			}
			OreDictionary.registerOre("container1000Water", Items.WATER_BUCKET);
			OreDictionary.registerOre("container1000Lava", Items.LAVA_BUCKET);
			OreDictionary.registerOre("wool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("ice", new ItemStack(Blocks.ICE));
		});
		erMod.addRecipe.register(() -> {

			// 製錬
			//GameRegistry.addSmelting(ApiOre.itemStackDustMiragium, ApiOre.itemStackIngotMiragium, 0);

		});
	}

}
