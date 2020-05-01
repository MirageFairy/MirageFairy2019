package miragefairy2019.mod.modules.mirageflower;

import java.util.Random;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.modules.fairy.FairyRegistry;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import miragefairy2019.mod.modules.ore.EnumVariantMaterials1;
import miragefairy2019.mod.modules.ore.ModuleOre;
import mirrg.boron.util.UtilsMath;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

public class BlockMirageFlower extends BlockBush implements IGrowable
{

	public static double getGrowRateInFloor(VariantFairy fairy)
	{
		double costWeight = fairy.type.cost / 50.0;
		return (fairy.type.manaSet.shine / costWeight) * fairy.type.abilitySet.get(EnumAbilityType.crystal) / 100.0 * 3;
	}

	//

	public BlockMirageFlower()
	{
		super(Material.PLANTS); // Solidでないマテリアルでなければ耕土の上に置けない

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

	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return state.isFullBlock() || state.getBlock() == Blocks.FARMLAND;
	}

	public boolean isMaxAge(IBlockState state)
	{
		return state.getValue(AGE) == 3;
	}

	protected void grow(World worldIn, BlockPos pos, IBlockState state, Random rand, double rate)
	{
		int t = Utils.randomInt(rand, rate);
		for (int i = 0; i < t; i++) {
			if (!isMaxAge(state)) {
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

		grow(worldIn, pos, state, rand, getGrowRate(worldIn, pos));
	}

	@SuppressWarnings("deprecation")
	public double getGrowRate(World world, BlockPos blockPos)
	{
		double rate = 0.04;

		// 人工光が当たっているなら加点
		if (world.getLightFor(EnumSkyBlock.BLOCK, blockPos) >= 13) rate *= 1.2;
		else if (world.getLightFor(EnumSkyBlock.BLOCK, blockPos) >= 9) rate *= 1.1;

		// 太陽光が当たっているなら加点
		if (world.getLightFor(EnumSkyBlock.SKY, blockPos) >= 15) rate *= 1.1;
		else if (world.getLightFor(EnumSkyBlock.SKY, blockPos) >= 9) rate *= 1.05;

		// 空が見えるなら加点
		if (world.canSeeSky(blockPos)) rate *= 1.1;

		// 地面加点
		{
			double bonus = 0.5;

			if (world.getBlockState(blockPos.down()).getBlock() == Blocks.GRASS) bonus = Math.max(bonus, 1);

			if (world.getBlockState(blockPos.down()).getBlock() == Blocks.DIRT) bonus = Math.max(bonus, 1.1);

			if (world.getBlockState(blockPos.down()).getBlock() == Blocks.FARMLAND) {
				bonus = Math.max(bonus, 1.2);

				// 耕土が湿っているなら加点
				if (world.getBlockState(blockPos.down()).getValue(BlockFarmland.MOISTURE) > 0) bonus = Math.max(bonus, 1.3);

			}

			// 妖精による判定
			{
				IBlockState blockState = world.getBlockState(blockPos.down());
				ItemStack itemStack = blockState.getBlock().getItem(world, blockPos, blockState);
				Double value = FairyRegistry.getFairies(itemStack)
					.map(f -> getGrowRateInFloor(f))
					.max(Double::compare).orElse(null);
				if (value != null) {
					bonus = Math.max(bonus, value);
				}
			}

			if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.APATITE_BLOCK)) bonus = Math.max(bonus, 1.5);
			if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.FLUORITE_BLOCK)) bonus = Math.max(bonus, 2);
			if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.SULFUR_BLOCK)) bonus = Math.max(bonus, 1.5);
			if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.CINNABAR_BLOCK)) bonus = Math.max(bonus, 2);
			if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MOONSTONE_BLOCK)) bonus = Math.max(bonus, 3);
			if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MAGNETITE_BLOCK)) bonus = Math.max(bonus, 1.2);

			rate *= bonus;
		}

		// バイオーム加点
		{
			double bonus = 1;

			if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.PLAINS)) bonus = Math.max(bonus, 1.1);
			if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.SWAMP)) bonus = Math.max(bonus, 1.1);
			if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.MOUNTAIN)) bonus = Math.max(bonus, 1.2);
			if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.JUNGLE)) bonus = Math.max(bonus, 1.2);
			if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.FOREST)) bonus = Math.max(bonus, 1.3);
			if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.MAGICAL)) bonus = Math.max(bonus, 1.3);

			rate *= bonus;
		}

		return rate;
	}

	/**
	 * 骨粉をやると低確率で成長する。
	 */
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		grow(worldIn, pos, state, rand, getGrowRate(worldIn, pos));
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
		return new ItemStack(ModuleMirageFlower.itemMirageFlowerSeeds);
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
		drops.add(new ItemStack(ModuleMirageFlower.itemMirageFlowerSeeds));

		// 追加の種
		{
			int count = Utils.randomInt(random, isMaxAge(state) ? fortune * 0.01 : 0);
			for (int i = 0; i < count; i++) {
				drops.add(new ItemStack(ModuleMirageFlower.itemMirageFlowerSeeds));
			}
		}

		// クリスタル
		{
			int count = Utils.randomInt(random, isMaxAge(state) ? 1 + fortune * 0.5 : 0);
			for (int i = 0; i < count; i++) {
				//drops.add(ModuleFairyCrystal.variantFairyCrystal.createItemStack());
				drops.add(ModuleFairyCrystal.variantFairyCrystalChristmas.createItemStack());
			}
		}

		// ミラジウム
		{
			int count = Utils.randomInt(random, isMaxAge(state) ? 1 + fortune * 0.5 : 0);
			for (int i = 0; i < count; i++) {
				drops.add(UtilsMinecraft.getItemStack("dustTinyMiragium").copy());
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
