package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.api.composite.ApiComposite.*;
import static miragefairy2019.mod.api.composite.Components.*;
import static miragefairy2019.mod.lib.Configurator.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import miragefairy2019.mod.modules.ore.oreseed.BlockOreSeed;
import miragefairy2019.mod.modules.ore.oreseed.EnumVariantOreSeed;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleOre
{

	public static BlockOreSeed blockOreSeed;
	public static BlockOre<EnumVariantOre1> blockOre1;
	public static BlockMaterials<EnumVariantMaterials1> blockMaterials1;

	public static ItemBlockOre<EnumVariantOre1> itemBlockOre1;
	public static ItemBlockMaterials<EnumVariantMaterials1> itemBlockMaterials1;

	public static void init(EventRegistryMod erMod)
	{

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

			// 鉱石の種
			blockOreSeed = new BlockOreSeed();
			blockOreSeed.setRegistryName(ModMirageFairy2019.MODID, "ore_seed");
			blockOreSeed.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockOreSeed);

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
				for (IOreVariant variant : blockOre1.variantList) {
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
		erMod.hookDecorator.register(() -> {

			// 地形生成
			MinecraftForge.ORE_GEN_BUS.register(new Object() {

				private WorldGenerator worldGenOreSeed1 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.TINY), 5);
				private WorldGenerator worldGenOreSeed2 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.LAPIS), 7);
				private WorldGenerator worldGenOreSeed3 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.DIAMOND), 8);
				private WorldGenerator worldGenOreSeed4 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.IRON), 9);
				private WorldGenerator worldGenOreSeed5 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.MEDIUM), 12);
				private WorldGenerator worldGenOreSeed6 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.LARGE), 15);
				private WorldGenerator worldGenOreSeed7 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.COAL), 17);
				private WorldGenerator worldGenOreSeed8 = new WorldGenMinable(blockOreSeed.getState(EnumVariantOreSeed.HUGE), 20);

				private WorldGenerator worldGenOreSeed15 = new WorldGenOreSeedString(blockOreSeed.getState(EnumVariantOreSeed.STRING));
				private WorldGenerator worldGenOreSeed16 = new WorldGenOreSeedHorizontal(blockOreSeed.getState(EnumVariantOreSeed.HORIZONTAL));
				private WorldGenerator worldGenOreSeed12 = new WorldGenOreSeedVertical(blockOreSeed.getState(EnumVariantOreSeed.VERTICAL));
				private WorldGenerator worldGenOreSeed9 = new WorldGenOreSeedPoint(blockOreSeed.getState(EnumVariantOreSeed.POINT));
				private WorldGenerator worldGenOreSeed14 = new WorldGenOreSeedStar(blockOreSeed.getState(EnumVariantOreSeed.STAR));
				private WorldGenerator worldGenOreSeed11 = new WorldGenOreSeedRing(blockOreSeed.getState(EnumVariantOreSeed.RING));
				private WorldGenerator worldGenOreSeed13 = new WorldGenOreSeedPyramid(blockOreSeed.getState(EnumVariantOreSeed.PYRAMID));
				private WorldGenerator worldGenOreSeed10 = new WorldGenOreSeedCube(blockOreSeed.getState(EnumVariantOreSeed.CUBE));

				@SubscribeEvent
				public void accept(OreGenEvent.Post event)
				{
					genStandardOre(event, 325 * 7 / 8 / 2 / 4, worldGenOreSeed15, 0, 255);
					genStandardOre(event, 263 * 7 / 9 / 2, worldGenOreSeed16, 0, 255);
					genStandardOre(event, 263 * 7 / 8 / 2, worldGenOreSeed12, 0, 255);
					genStandardOre(event, 263 * 7 / 1 / 2, worldGenOreSeed9, 0, 255);
					genStandardOre(event, 263 * 7 / 13 / 2, worldGenOreSeed14, 0, 255);
					genStandardOre(event, 263 * 7 / 8 / 2, worldGenOreSeed11, 0, 255);
					genStandardOre(event, 263 * 7 / 7 / 2, worldGenOreSeed13, 0, 255);
					genStandardOre(event, 263 * 7 / 8 / 2, worldGenOreSeed10, 0, 255);

					genStandardOre(event, 474 / 2, worldGenOreSeed1, 0, 255);
					genStandardOre(event, 293 / 2, worldGenOreSeed2, 0, 255);
					genStandardOre(event, 272 / 2, worldGenOreSeed3, 0, 255);
					genStandardOre(event, 263 / 2, worldGenOreSeed4, 0, 255);
					genStandardOre(event, 144 / 2, worldGenOreSeed5, 0, 255);
					genStandardOre(event, 120 / 2, worldGenOreSeed6, 0, 255);
					genStandardOre(event, 90 / 2, worldGenOreSeed7, 0, 255);
					genStandardOre(event, 45 / 2, worldGenOreSeed8, 0, 255);

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

	public static abstract class WorldGenOreSeedBase extends WorldGenerator
	{

		protected final IBlockState blockState;

		public WorldGenOreSeedBase(IBlockState blockState)
		{
			this.blockState = blockState;
		}

		protected void replace(World world, Random random, BlockPos pos)
		{
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock().isReplaceableOreGen(state, world, pos, bs -> {
				if (bs == null || bs.getBlock() != Blocks.STONE) return false;
				return bs.getValue(BlockStone.VARIANT).isNatural();
			})) {
				world.setBlockState(pos, blockState, 2);
			}
		}

	}

	public static class WorldGenOreSeedPoint extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedPoint(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			pos = pos.add(8, 0, 8);

			replace(world, random, pos);
			return true;
		}

	}

	public static class WorldGenOreSeedCube extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedCube(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			pos = pos.add(8, 0, 8);

			replace(world, random, pos.add(0, 0, 0));
			replace(world, random, pos.add(0, 0, 1));
			replace(world, random, pos.add(0, 1, 0));
			replace(world, random, pos.add(0, 1, 1));
			replace(world, random, pos.add(1, 0, 0));
			replace(world, random, pos.add(1, 0, 1));
			replace(world, random, pos.add(1, 1, 0));
			replace(world, random, pos.add(1, 1, 1));
			return true;
		}

	}

	public static class WorldGenOreSeedRing extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedRing(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			pos = pos.add(8, 0, 8);

			replace(world, random, pos.add(-1, 0, -1));
			replace(world, random, pos.add(-1, 0, 0));
			replace(world, random, pos.add(-1, 0, 1));
			replace(world, random, pos.add(0, 0, -1));
			replace(world, random, pos.add(0, 0, 1));
			replace(world, random, pos.add(1, 0, -1));
			replace(world, random, pos.add(1, 0, 0));
			replace(world, random, pos.add(1, 0, 1));
			return true;
		}

	}

	public static class WorldGenOreSeedVertical extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedVertical(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			pos = pos.add(8, 0, 8);

			replace(world, random, pos.up(0));
			replace(world, random, pos.up(1));
			replace(world, random, pos.up(2));
			replace(world, random, pos.up(3));
			replace(world, random, pos.up(4));
			replace(world, random, pos.up(5));
			replace(world, random, pos.up(6));
			replace(world, random, pos.up(7));
			return true;
		}

	}

	public static class WorldGenOreSeedPyramid extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedPyramid(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			pos = pos.add(8, 0, 8);

			replace(world, random, pos);
			replace(world, random, pos.up());
			replace(world, random, pos.down());
			replace(world, random, pos.west());
			replace(world, random, pos.east());
			replace(world, random, pos.north());
			replace(world, random, pos.south());
			return true;
		}

	}

	public static class WorldGenOreSeedStar extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedStar(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			pos = pos.add(8, 0, 8);

			replace(world, random, pos);
			replace(world, random, pos.up(1));
			replace(world, random, pos.up(2));
			replace(world, random, pos.down(1));
			replace(world, random, pos.down(2));
			replace(world, random, pos.west(1));
			replace(world, random, pos.west(2));
			replace(world, random, pos.east(1));
			replace(world, random, pos.east(2));
			replace(world, random, pos.north(1));
			replace(world, random, pos.north(2));
			replace(world, random, pos.south(1));
			replace(world, random, pos.south(2));
			return true;
		}

	}

	public static class WorldGenOreSeedString extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedString(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			BlockPos posMin = pos.add(0, 0, 0);
			BlockPos posMax = pos.add(15, 0, 15);

			pos = pos.add(8, 0, 8);

			Set<BlockPos> poses = new HashSet<>();
			int variant = random.nextInt(6);

			for (int i = 0; i < 100; i++) {
				if (poses.size() >= 32) break;

				if (pos.getX() < posMin.getX()) break;
				if (pos.getZ() < posMin.getZ()) break;
				if (pos.getX() > posMax.getX()) break;
				if (pos.getZ() > posMax.getZ()) break;

				replace(world, random, pos);
				poses.add(pos);

				if (random.nextInt(2) == 0) {
					variant = random.nextInt(6);
				}

				if (variant == 0) {
					pos = pos.up();
				} else if (variant == 1) {
					pos = pos.down();
				} else if (variant == 2) {
					pos = pos.west();
				} else if (variant == 3) {
					pos = pos.east();
				} else if (variant == 4) {
					pos = pos.north();
				} else if (variant == 5) {
					pos = pos.south();
				}

			}
			return true;
		}

	}

	public static class WorldGenOreSeedHorizontal extends WorldGenOreSeedBase
	{

		public WorldGenOreSeedHorizontal(IBlockState blockState)
		{
			super(blockState);
		}

		@Override
		public boolean generate(World world, Random random, BlockPos pos)
		{
			pos = pos.add(8, 0, 8);

			int variant = random.nextInt(2);
			if (variant == 0) {
				replace(world, random, pos.west(4));
				replace(world, random, pos.west(3));
				replace(world, random, pos.west(2));
				replace(world, random, pos.west(1));
				replace(world, random, pos);
				replace(world, random, pos.east(1));
				replace(world, random, pos.east(2));
				replace(world, random, pos.east(3));
				replace(world, random, pos.east(4));
			} else {
				replace(world, random, pos.north(4));
				replace(world, random, pos.north(3));
				replace(world, random, pos.north(2));
				replace(world, random, pos.north(1));
				replace(world, random, pos);
				replace(world, random, pos.south(1));
				replace(world, random, pos.south(2));
				replace(world, random, pos.south(3));
				replace(world, random, pos.south(4));
			}
			return true;
		}

	}

}
