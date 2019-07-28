package miragefairy2019.mod.modules.mirageflower;

import java.util.Random;

import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.api.ApiOre;
import miragefairy2019.mod.lib.Utils;
import mirrg.boron.util.UtilsMath;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMirageFlower extends BlockBush implements IGrowable
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

	// 判定

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

	protected boolean isMaxAge(IBlockState state)
	{
		return state.getValue(AGE) == 3;
	}

	protected void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!isMaxAge(state)) {
			if (rand.nextInt(20) == 0) {
				worldIn.setBlockState(pos, getDefaultState().withProperty(AGE, state.getValue(AGE) + 1), 2);
			}
		}
	}

	/**
	 * UpdateTickごとにAgeが1ずつ最大3まで増える。
	 */
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		if (!worldIn.isAreaLoaded(pos, 1)) return;
		grow(worldIn, pos, state, rand);
	}

	/**
	 * 骨粉をやると低確率で成長する。
	 */
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		grow(worldIn, pos, state, rand);
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return !isMaxAge(state);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return worldIn.rand.nextFloat() < 0.05;
	}

	// ドロップ

	/**
	 * クリエイティブピックでの取得アイテム。
	 */
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return ApiMirageFlower.itemStackMirageFlowerSeeds.copy();
	}

	/**
	 * Ageが最大のとき、種を1個ドロップする。
	 * 幸運Lv1につき種のドロップ数が1%増える。
	 * 地面破壊ドロップでも適用される。
	 */
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		Random random = world instanceof World ? ((World) world).rand : new Random();

		// 種1個は確定でドロップ
		drops.add(ApiMirageFlower.itemStackMirageFlowerSeeds.copy());

		// 追加の種
		{
			int count = Utils.randomInt(random, isMaxAge(state) ? fortune * 0.01 : 0);
			for (int i = 0; i < count; i++) {
				drops.add(ApiMirageFlower.itemStackMirageFlowerSeeds.copy());
			}
		}

		// クリスタル
		{
			int count = Utils.randomInt(random, isMaxAge(state) ? 1 + fortune * 0.5 : 0);
			for (int i = 0; i < count; i++) {
				drops.add(ApiFairy.itemStackFairyCrystal.copy());
			}
		}

		// ミラジウム
		{
			int count = Utils.randomInt(random, isMaxAge(state) ? 1 + fortune * 0.5 : 0);
			for (int i = 0; i < count; i++) {
				drops.add(ApiOre.itemStackDustTinyMiragium.copy());
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

}
