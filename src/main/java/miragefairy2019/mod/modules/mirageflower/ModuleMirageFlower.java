package miragefairy2019.mod.modules.mirageflower;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.lib.BiomeDecoratorFlowers;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.WorldGenBush;
import mirrg.boron.util.UtilsLambda;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleMirageFlower
{

	public static BlockMirageFlower blockMirageFlower;
	public static BlockFairyLog blockFairyLog;
	public static ItemMirageFlowerSeeds itemMirageFlowerSeeds;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 種
			ApiMirageFlower.itemMirageFlowerSeeds = itemMirageFlowerSeeds = new ItemMirageFlowerSeeds();
			itemMirageFlowerSeeds.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower_seeds");
			itemMirageFlowerSeeds.setUnlocalizedName("mirageFlowerSeeds");
			itemMirageFlowerSeeds.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.ITEMS.register(itemMirageFlowerSeeds);
			if (ApiMain.side.isClient()) {
				ModelLoader.setCustomModelResourceLocation(itemMirageFlowerSeeds, 0, new ModelResourceLocation(itemMirageFlowerSeeds.getRegistryName(), null));
			}

		});
		erMod.registerBlock.register(b -> {

			// ブロック
			ApiMirageFlower.blockMirageFlower = blockMirageFlower = new BlockMirageFlower();
			blockMirageFlower.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower");
			blockMirageFlower.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.BLOCKS.register(blockMirageFlower);

			// ブロック
			ApiMirageFlower.blockFairyLog = blockFairyLog = new BlockFairyLog();
			blockFairyLog.setRegistryName(ModMirageFairy2019.MODID, "fairy_log");
			blockFairyLog.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.BLOCKS.register(blockFairyLog);

		});
		erMod.createItemStack.register(b -> {

			// 種
			ApiMirageFlower.itemStackMirageFlowerSeeds = new ItemStack(itemMirageFlowerSeeds);

		});
		erMod.hookDecorator.register(() -> {

			// 地形生成
			List<BiomeDecoratorFlowers> biomeDecorators = new ArrayList<>();

			biomeDecorators.add(new BiomeDecoratorFlowers(
				UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), wg -> {
					wg.blockCountMin = 1;
					wg.blockCountMax = 3;
				}),
				0.01));

			biomeDecorators.add(new BiomeDecoratorFlowers(
				UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), wg -> {
					wg.blockCountMin = 1;
					wg.blockCountMax = 10;
				}),
				0.1) {
				@Override
				protected boolean canGenerate(Biome biome)
				{
					return super.canGenerate(biome) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN);
				}
			});

			biomeDecorators.add(new BiomeDecoratorFlowers(
				UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), wg -> {
					wg.blockCountMin = 1;
					wg.blockCountMax = 10;
				}),
				0.5) {
				@Override
				protected boolean canGenerate(Biome biome)
				{
					return super.canGenerate(biome) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST);
				}
			});

			// ミラージュの花
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void accept(DecorateBiomeEvent.Post event)
				{
					for (BiomeDecoratorFlowers biomeDecorator : biomeDecorators) {
						biomeDecorator.decorate(event);
					}
				}
			});

			// 妖精の洞穴
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void init(DecorateBiomeEvent.Post event)
				{
					int count = Utils.randomInt(event.getRand(), (event.getWorld().getHeight() / 16.0) * 50); // TODO
					for (int i = 0; i < count; i++) {
						EnumFacing facing = EnumFacing.HORIZONTALS[event.getRand().nextInt(4)];
						BlockPos posLog = event.getChunkPos().getBlock(
							event.getRand().nextInt(16) + 8,
							event.getRand().nextInt(event.getWorld().getHeight()),
							event.getRand().nextInt(16) + 8);
						BlockPos posAir = posLog.offset(facing);
						IBlockState blockStateLog = event.getWorld().getBlockState(posLog);
						IBlockState blockStateAir = event.getWorld().getBlockState(posAir);
						IBlockState blockStateFairyLog = blockFairyLog.getState(facing);
						if (!blockStateAir.isSideSolid(event.getWorld(), posAir, facing.getOpposite())) {
							if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK))) {
								event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
							} else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE))) {
								event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
							} else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH))) {
								event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
							} else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE))) {
								event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
							} else if (blockStateLog.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA))) {
								event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
							} else if (blockStateLog.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK))) {
								event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
							}
						}
					}
				}
			});

		});
	}

}
