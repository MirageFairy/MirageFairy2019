package miragefairy2019.mod.modules.mirageflower;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFairyLog extends Block
{

	public BlockFairyLog()
	{
		super(Material.WOOD);

		// meta
		setDefaultState(blockState.getBaseState()
			.withProperty(VARIANT, BlockPlanks.EnumType.OAK)
			.withProperty(FACING, EnumFacing.NORTH));

		// style
		setSoundType(SoundType.WOOD);

		// 挙動
		setHardness(2.0F);
		setHarvestLevel("axe", 0);

	}

	// state

	public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);

	@Override
	public int getMetaFromState(IBlockState state)
	{
		if (state.getValue(FACING) == EnumFacing.NORTH) return 0;
		if (state.getValue(FACING) == EnumFacing.SOUTH) return 1;
		if (state.getValue(FACING) == EnumFacing.WEST) return 2;
		if (state.getValue(FACING) == EnumFacing.EAST) return 3;
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		if (meta == 0) return getDefaultState().withProperty(FACING, EnumFacing.NORTH);
		if (meta == 1) return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
		if (meta == 2) return getDefaultState().withProperty(FACING, EnumFacing.WEST);
		if (meta == 3) return getDefaultState().withProperty(FACING, EnumFacing.EAST);
		return getDefaultState();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT, FACING);
	}

	public IBlockState getState(EnumFacing facing)
	{
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public IBlockState getActualState(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		IBlockState blockState2;
		blockState2 = getActualState0(blockState, world, pos.up());
		if (blockState2 != null) return blockState2;
		blockState2 = getActualState0(blockState, world, pos.down());
		if (blockState2 != null) return blockState2;
		blockState2 = getActualState0(blockState, world, pos.west());
		if (blockState2 != null) return blockState2;
		blockState2 = getActualState0(blockState, world, pos.east());
		if (blockState2 != null) return blockState2;
		blockState2 = getActualState0(blockState, world, pos.north());
		if (blockState2 != null) return blockState2;
		blockState2 = getActualState0(blockState, world, pos.south());
		if (blockState2 != null) return blockState2;
		return blockState;
	}

	private IBlockState getActualState0(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		IBlockState blockState2 = world.getBlockState(pos);
		if (blockState2.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK))) {
			return blockState.withProperty(VARIANT, BlockPlanks.EnumType.OAK);
		} else if (blockState2.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE))) {
			return blockState.withProperty(VARIANT, BlockPlanks.EnumType.SPRUCE);
		} else if (blockState2.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH))) {
			return blockState.withProperty(VARIANT, BlockPlanks.EnumType.BIRCH);
		} else if (blockState2.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE))) {
			return blockState.withProperty(VARIANT, BlockPlanks.EnumType.JUNGLE);
		} else if (blockState2.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA))) {
			return blockState.withProperty(VARIANT, BlockPlanks.EnumType.ACACIA);
		} else if (blockState2.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK))) {
			return blockState.withProperty(VARIANT, BlockPlanks.EnumType.DARK_OAK);
		}
		return null;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}

	// 動作

	// ドロップ

	/**
	 * クリエイティブピックでの取得アイテム。
	 */
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(ModuleMirageFlower.itemBlockFairyLog);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune)
	{
		Random random = blockAccess instanceof World ? ((World) blockAccess).rand : new Random();

		if (blockAccess instanceof World) {
			World world = (World) blockAccess;

			for (int i = 0; i < 3 + fortune; i++) {

				// ドロップリスト作成
				List<WeightedRandom.Item<VariantFairy>> list = new ArrayList<>();
				{

					if (!BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.NETHER)) {
						if (!BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.END)) {

							list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.daytime[0], 0.1));
							list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.night[0], 0.1));
							list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.morning[0], 0.1));

							list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.fine[0], 0.1));
							if (world.getBiome(pos).canRain()) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.rain[0], 0.1));
							if (world.getBiome(pos).canRain()) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.thunder[0], 0.02));

						}
					}

					if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.PLAINS)) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.plains[0], 1));
					if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.FOREST)) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.forest[0], 1));
					if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.CONIFEROUS)) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.taiga[0], 1));
					if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.SANDY)) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.desert[0], 1));
					if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.MOUNTAIN)) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.mountain[0], 1));

				}

				// 抽選
				double totalWeight = WeightedRandom.getTotalWeight(list);
				if (totalWeight < 1) list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.air[0], 1 - totalWeight));
				Optional<VariantFairy> oVariantFairy = WeightedRandom.getRandomItem(random, list);

				// 排出
				if (oVariantFairy.isPresent()) drops.add(oVariantFairy.get().createItemStack());

			}
		}

	}

	/**
	 * シルクタッチ無効。
	 */
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

}
