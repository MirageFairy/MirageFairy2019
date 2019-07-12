package miragefairy2019.mod.modules.mirageflower;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.lib.BiomeDecoratorFlowers;
import miragefairy2019.mod.lib.WorldGenBush;
import mirrg.boron.util.UtilsLambda;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleMirageFlower
{

	public static BlockMirageFlower blockMirageFlower;
	public static ItemMirageFlowerSeeds itemMirageFlowerSeeds;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 種
			ApiMirageFlower.itemMirageFlowerSeeds = itemMirageFlowerSeeds = new ItemMirageFlowerSeeds();
			itemMirageFlowerSeeds.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower_seeds");
			itemMirageFlowerSeeds.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.ITEMS.register(itemMirageFlowerSeeds);

		});
		erMod.registerBlock.register(b -> {

			// ブロック
			ApiMirageFlower.blockMirageFlower = blockMirageFlower = new BlockMirageFlower();
			blockMirageFlower.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower");
			blockMirageFlower.setCreativeTab(ApiMain.creativeTab);
			ForgeRegistries.BLOCKS.register(blockMirageFlower);

		});
		erMod.createItemStack.register(b -> {

			// 種
			ApiMirageFlower.itemStackMirageFlowerSeeds = new ItemStack(itemMirageFlowerSeeds);

		});
		erMod.hookDecorator.register(() -> {

			// 地形生成

			BiomeDecoratorFlowers decoratorDreamyFlower = new BiomeDecoratorFlowers(
				UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), worldGenerator1 -> {
					worldGenerator1.blockCountMin = 1;
					worldGenerator1.blockCountMax = 3;
				}),
				0.025);
			BiomeDecoratorFlowers decoratorDreamyFlowerMountain = new BiomeDecoratorFlowers(
				UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), worldGenerator2 -> {
					worldGenerator2.blockCountMin = 1;
					worldGenerator2.blockCountMax = 5;
				}),
				0.25) {
				@Override
				protected boolean canGenerate(Biome biome)
				{
					return super.canGenerate(biome) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN);
				}
			};

			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void accept(DecorateBiomeEvent.Post event)
				{
					decoratorDreamyFlower.decorate(event);
					decoratorDreamyFlowerMountain.decorate(event);
				}
			});

		});
	}

}
