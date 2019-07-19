package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiOre;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleOre
{

	public static BlockOreSeed blockOreSeed;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerBlock.register(b -> {

			// ブロック
			ApiOre.blockOreSeed = blockOreSeed = new BlockOreSeed();
			blockOreSeed.setRegistryName(ModMirageFairy2019.MODID, "ore_seed");
			blockOreSeed.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.BLOCKS.register(blockOreSeed);

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
					genStandardOre(event, 16, worldGenOreSeed1, 64, 255);
					genStandardOre(event, 16, worldGenOreSeed2, 64, 255);
					genStandardOre(event, 16, worldGenOreSeed3, 64, 255);
					genStandardOre(event, 16, worldGenOreSeed4, 64, 255);
					genStandardOre(event, 16, worldGenOreSeed5, 64, 255);
					genStandardOre(event, 16, worldGenOreSeed6, 64, 255);
					genStandardOre(event, 16, worldGenOreSeed7, 64, 255);
					genStandardOre(event, 16, worldGenOreSeed8, 64, 255);
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
