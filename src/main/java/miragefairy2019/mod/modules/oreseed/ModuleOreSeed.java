package miragefairy2019.mod.modules.oreseed;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleOreSeed
{

	public static BlockOreSeed blockOreSeed;
	public static BlockOreSeed blockOreSeedNether;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerBlock.register(b -> {

			// 鉱石の種
			blockOreSeed = new BlockOreSeed(() -> Blocks.STONE.getDefaultState());
			blockOreSeed.setRegistryName(ModMirageFairy2019.MODID, "ore_seed");
			blockOreSeed.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockOreSeed);

			blockOreSeedNether = new BlockOreSeed(() -> Blocks.NETHERRACK.getDefaultState());
			// 鉱石の種：ネザー
			blockOreSeedNether.setRegistryName(ModMirageFairy2019.MODID, "ore_seed_nether");
			blockOreSeedNether.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockOreSeedNether);

		});
		erMod.hookDecorator.register(() -> {

			// 地形生成
			MinecraftForge.ORE_GEN_BUS.register(new Object() {
				private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(blockOreSeed, blockState -> {
					if (blockState == null) return false;
					if (blockState.getBlock() != Blocks.STONE) return false;
					if (!blockState.getValue(BlockStone.VARIANT).isNatural()) return false;
					return true;
				});

				@SubscribeEvent
				public void accept(OreGenEvent.Post event)
				{
					if (event.getWorld().provider.isSurfaceWorld()) {
						worldGenCompound.accept(event.getWorld(), event.getRand(), event.getPos());
					}
				}
			});

			// 地形生成
			MinecraftForge.EVENT_BUS.register(new Object() {
				private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(blockOreSeedNether, blockState -> {
					if (blockState == null) return false;
					if (blockState.getBlock() != Blocks.NETHERRACK) return false;
					return true;
				});

				@SubscribeEvent
				public void accept(DecorateBiomeEvent.Post event)
				{
					if (BiomeDictionary.hasType(event.getWorld().getBiome(event.getChunkPos().getBlock(8, 0, 8)), BiomeDictionary.Type.NETHER)) {
						worldGenCompound.accept(event.getWorld(), event.getRand(), event.getChunkPos().getBlock(0, 0, 0));
					}
				}
			});

		});
	}

}
