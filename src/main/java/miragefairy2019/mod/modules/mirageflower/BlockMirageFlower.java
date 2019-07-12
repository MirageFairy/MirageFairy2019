package miragefairy2019.mod.modules.mirageflower;

import java.util.Random;

import miragefairy2019.mod.api.ApiMirageFlower;
import mirrg.boron.util.UtilsMath;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMirageFlower extends BlockBush
{

	public BlockMirageFlower()
	{
		super(Material.GLASS);

		// meta
		setDefaultState(blockState.getBaseState()
			.withProperty(AGE, 0));

		// style
		setSoundType(SoundType.GLASS);

	}

	// state

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return UtilsMath.trim(state.getValue(AGE), 0, 3);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(AGE, UtilsMath.trim(meta, 0, 3));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AGE);
	}

	public IBlockState getState(int age)
	{
		return getDefaultState().withProperty(AGE, age);
	}

	// style

	public static final AxisAlignedBB AABB_STAGE0 = new AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0);
	public static final AxisAlignedBB AABB_STAGE1 = new AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0);
	public static final AxisAlignedBB AABB_STAGE2 = new AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0);
	public static final AxisAlignedBB AABB_STAGE3 = new AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		int stage = state.getValue(AGE);
		if (stage == 0) return AABB_STAGE0;
		if (stage == 1) return AABB_STAGE1;
		if (stage == 2) return AABB_STAGE2;
		if (stage == 3) return AABB_STAGE3;
		return AABB_STAGE3;
	}

	// 動作

	/**
	 * UpdateTickごとにAgeが1ずつ最大3まで増える。
	 */
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		int age = state.getValue(AGE);
		if (age < 3) {
			worldIn.setBlockState(pos, getDefaultState().withProperty(AGE, age + 1), 2);
		}

	}

	/**
	 * 何もドロップしない。
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	/**
	 * 何もドロップしない。
	 */
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	/**
	 * 種をドロップする。
	 */
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return ApiMirageFlower.itemStackMirageFlowerSeeds.copy();
	}

	/**
	 * Ageが最大のとき、種を1個ドロップする。
	 * 幸運Lv1につき種のドロップ数が5%増える。
	 */
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		Random random = world instanceof World ? ((World) world).rand : new Random();
		int seedCount = 1;
		/*
		// TODO ドロップ
		int dropCount = 0;
		int additionalDropCount = 0;
		*/

		if (state.getValue(AGE) >= 3) {
			for (int i = 0; i <= fortune; i++) {

				if (random.nextDouble() < 0.05) seedCount++;

				/*
				if (random.nextDouble() < 0.5) dropCount++;
				if (random.nextDouble() < 0.5) dropCount++;
				if (random.nextDouble() < 0.5) dropCount++;
				if (random.nextDouble() < 0.5) dropCount++;

				if (random.nextDouble() < 0.05) additionalDropCount++;
				*/

			}
		}

		for (int i = 0; i < seedCount; i++) {
			drops.add(ApiMirageFlower.itemStackMirageFlowerSeeds.copy());
		}
		/*
		for (int i = 0; i < dropCount; i++) {
			drops.add(ModuleMaterial.dustTinyMiragium.createItemStack());
		}
		for (int i = 0; i < additionalDropCount; i++) {
			drops.add(ModuleFairy.mirageWisp.createItemStack());
		}
		*/

	}

}
