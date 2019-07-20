package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiOre;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleOre
{

	public static BlockOreSeed blockOreSeed;
	public static BlockOre<EnumVariantOre1> blockOre1;

	public static ItemOre<EnumVariantOre1> itemOre1;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerBlock.register(b -> {

			// 鉱石の種
			ApiOre.blockOreSeed = blockOreSeed = new BlockOreSeed();
			blockOreSeed.setRegistryName(ModMirageFairy2019.MODID, "ore_seed");
			blockOreSeed.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.BLOCKS.register(blockOreSeed);

			// 鉱石
			ApiOre.blockOre1 = blockOre1 = new BlockOre<>(EnumVariantOre1.variantList);
			blockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
			blockOre1.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.BLOCKS.register(blockOre1);

		});
		erMod.registerItem.register(b -> {

			// 鉱石
			ApiOre.itemOre1 = itemOre1 = new ItemOre<>(blockOre1);
			itemOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
			itemOre1.setUnlocalizedName("ore1");
			itemOre1.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.ITEMS.register(itemOre1);
			for (IOreVariant variant : blockOre1.variantList) {
				ModelLoader.setCustomModelResourceLocation(
					itemOre1,
					variant.getMetadata(),
					new ModelResourceLocation(new ResourceLocation(itemOre1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
			}

		});
		erMod.hookDecorator.register(() -> {

			// 地形生成
			MinecraftForge.ORE_GEN_BUS.register(new Object() {

				private WorldGenMinable worldGenOreSeed1 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.TINY), 5);
				private WorldGenMinable worldGenOreSeed2 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.LAPIS), 7);
				private WorldGenMinable worldGenOreSeed3 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.DIAMOND), 8);
				private WorldGenMinable worldGenOreSeed4 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.IRON), 9);
				private WorldGenMinable worldGenOreSeed5 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.MEDIUM), 13);
				private WorldGenMinable worldGenOreSeed6 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.COAL), 17);
				private WorldGenMinable worldGenOreSeed7 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.LARGE), 25);
				private WorldGenMinable worldGenOreSeed8 = new WorldGenMinable(blockOreSeed.getState(BlockOreSeed.EnumVariant.DIRT), 33);

				@SubscribeEvent
				public void accept(OreGenEvent.Post event)
				{
					genStandardOre(event, 256, worldGenOreSeed1, 0, 255);
					genStandardOre(event, 192, worldGenOreSeed2, 0, 255);
					genStandardOre(event, 128, worldGenOreSeed3, 0, 255);
					genStandardOre(event, 96, worldGenOreSeed4, 0, 255);
					genStandardOre(event, 64, worldGenOreSeed5, 0, 255);
					genStandardOre(event, 48, worldGenOreSeed6, 0, 255);
					genStandardOre(event, 32, worldGenOreSeed7, 0, 255);
					genStandardOre(event, 16, worldGenOreSeed8, 0, 255);
				}

				private void genStandardOre(OreGenEvent.Post event, int count, WorldGenerator generator, int minHeightInclusive, int maxHeightExclusive)
				{
					for (int j = 0; j < count; ++j) {
						generator.generate(event.getWorld(), event.getRand(), event.getPos().add(
							event.getRand().nextInt(16),
							event.getRand().nextInt(maxHeightExclusive - minHeightInclusive) + minHeightInclusive,
							event.getRand().nextInt(16)));
					}
				}

			});

		});
	}

}
