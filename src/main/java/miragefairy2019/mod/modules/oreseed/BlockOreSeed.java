package miragefairy2019.mod.modules.oreseed;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod.modules.ore.ModuleOre;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOreSeed extends Block
{

	public BlockOreSeed()
	{
		super(Material.ROCK);

		// meta
		setDefaultState(blockState.getBaseState()
			.withProperty(VARIANT, EnumVariantOreSeed.TINY));

		// style
		setSoundType(SoundType.STONE);

		// 挙動
		setHardness(1.5f);
		setResistance(10.0f);
		setTickRandomly(true);

	}

	//

	public static final PropertyEnum<EnumVariantOreSeed> VARIANT = PropertyEnum.create("variant", EnumVariantOreSeed.class);

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT);
	}

	public IBlockState getState(EnumVariantOreSeed variant)
	{
		return getDefaultState().withProperty(VARIANT, variant);
	}

	public EnumVariantOreSeed getVariant(IBlockState blockState)
	{
		return blockState.getValue(VARIANT);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANT, EnumVariantOreSeed.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState blockState)
	{
		return blockState.getValue(VARIANT).metadata;
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> itemStacks)
	{
		for (EnumVariantOreSeed variant : EnumVariantOreSeed.values()) {
			itemStacks.add(new ItemStack(this, 1, variant.metadata));
		}
	}

	//

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(Blocks.STONE));
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.add(new ItemStack(Item.getItemFromBlock(Blocks.STONE)));
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	//

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote) {
			update(world, pos, state);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		worldIn.scheduleUpdate(pos, this, 2);
	}

	//

	private static Map<EnumVariantOreSeed, List<Function<Tuple<World, BlockPos>, Optional<WeightedRandom.Item<Supplier<IBlockState>>>>>> registry = new HashMap<>();

	static {
		register(EnumVariantOreSeed.LARGE, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.APATITE_ORE));
		register(EnumVariantOreSeed.LARGE, 0.08, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SMITHSONITE_ORE), minY(30));
		register(EnumVariantOreSeed.PYRAMID, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.FLUORITE_ORE));
		register(EnumVariantOreSeed.STAR, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SULFUR_ORE), maxY(15));
		register(EnumVariantOreSeed.POINT, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.CINNABAR_ORE), maxY(15));
		register(EnumVariantOreSeed.POINT, 0.05, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.PYROPE_ORE), maxY(50));
		register(EnumVariantOreSeed.COAL, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MAGNETITE_ORE));
		register(EnumVariantOreSeed.TINY, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MOONSTONE_ORE), minY(40), maxY(50));
	}

	private static interface IGenerationCondition extends Predicate<Tuple<World, BlockPos>>
	{

	}

	private static IGenerationCondition minY(int minY)
	{
		return t -> t.y.getY() >= minY;
	}

	private static IGenerationCondition maxY(int maxY)
	{
		return t -> t.y.getY() <= maxY;
	}

	private static void register(EnumVariantOreSeed variant, double weight, Supplier<IBlockState> block, IGenerationCondition... generationConditions)
	{
		registry.compute(variant, (v, l) -> {
			if (l == null) l = new ArrayList<>();
			l.add(t -> {
				for (IGenerationCondition generationCondition : generationConditions) {
					if (!generationCondition.test(t)) return Optional.empty();
				}
				return Optional.of(new WeightedRandom.Item<>(block, weight));
			});
			return l;
		});
	}

	public static List<WeightedRandom.Item<Supplier<IBlockState>>> getList(World world, BlockPos pos, EnumVariantOreSeed variant)
	{
		List<Function<Tuple<World, BlockPos>, Optional<WeightedRandom.Item<Supplier<IBlockState>>>>> list = registry.get(variant);
		if (list == null) return new ArrayList<>();
		List<WeightedRandom.Item<Supplier<IBlockState>>> list2 = ISuppliterator.ofIterable(list)
			.mapIfPresent(f -> f.apply(Tuple.of(world, pos)))
			.toList();
		return list2;
	}

	public static Optional<IBlockState> get(World world, BlockPos pos, EnumVariantOreSeed variant, Random random)
	{
		List<WeightedRandom.Item<Supplier<IBlockState>>> list2 = getList(world, pos, variant);
		if (random.nextDouble() < Math.max(1 - WeightedRandom.getTotalWeight(list2), 0)) return Optional.empty();
		return WeightedRandom.getRandomItem(
			random,
			list2).map(s -> s.get());
	}

	protected void update(World world, BlockPos pos, IBlockState state)
	{
		if (canMutate(world, pos)) {
			Random random = new Random(pos.getX() * 15946848L + pos.getY() * 29135678L + pos.getZ() * 65726816L);
			IBlockState blockStateAfter = get(
				world,
				pos,
				getVariant(state),
				random).orElseGet(() -> Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE));

			Deque<BlockPos> poses = new ArrayDeque<>();
			poses.addLast(pos);
			int t = 4096;
			while (!poses.isEmpty()) {
				BlockPos pos2 = poses.removeFirst();

				world.setBlockState(pos2, blockStateAfter, 2);
				if (world.getBlockState(pos2.up()).equals(state)) poses.addLast(pos2.up());
				if (world.getBlockState(pos2.down()).equals(state)) poses.addLast(pos2.down());
				if (world.getBlockState(pos2.west()).equals(state)) poses.addLast(pos2.west());
				if (world.getBlockState(pos2.east()).equals(state)) poses.addLast(pos2.east());
				if (world.getBlockState(pos2.north()).equals(state)) poses.addLast(pos2.north());
				if (world.getBlockState(pos2.south()).equals(state)) poses.addLast(pos2.south());

				t--;
				if (t <= 0) break;
			}

		}
	}

	protected boolean canMutate(IBlockAccess world, BlockPos pos)
	{
		if (!world.getBlockState(pos.up()).isSideSolid(world, pos.up(), EnumFacing.DOWN)) return true;
		if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) return true;
		if (!world.getBlockState(pos.west()).isSideSolid(world, pos.west(), EnumFacing.EAST)) return true;
		if (!world.getBlockState(pos.east()).isSideSolid(world, pos.east(), EnumFacing.WEST)) return true;
		if (!world.getBlockState(pos.north()).isSideSolid(world, pos.north(), EnumFacing.SOUTH)) return true;
		if (!world.getBlockState(pos.south()).isSideSolid(world, pos.south(), EnumFacing.NORTH)) return true;
		return false;
	}

}
