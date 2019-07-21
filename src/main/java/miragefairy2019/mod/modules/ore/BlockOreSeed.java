package miragefairy2019.mod.modules.ore;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

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
import net.minecraft.util.IStringSerializable;
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
			.withProperty(VARIANT, EnumVariant.TINY));

		// style
		setSoundType(SoundType.STONE);

		// 挙動
		setHardness(1.5f);
		setResistance(10.0f);
		setTickRandomly(true);

	}

	//

	public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", EnumVariant.class);

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT);
	}

	public IBlockState getState(EnumVariant variant)
	{
		return getDefaultState().withProperty(VARIANT, variant);
	}

	public EnumVariant getVariant(IBlockState blockState)
	{
		return blockState.getValue(VARIANT);
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANT, EnumVariant.byMetadata(meta));
	}

	public int getMetaFromState(IBlockState blockState)
	{
		return blockState.getValue(VARIANT).metadata;
	}

	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> itemStacks)
	{
		for (EnumVariant variant : EnumVariant.values()) {
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

	protected void update(World world, BlockPos pos, IBlockState state)
	{
		if (canMutate(world, pos)) {
			Random random = new Random(pos.getX() * 15946848L + pos.getY() * 29135678L + pos.getZ() * 65726816L);
			EnumVariant variant = getVariant(state);
			double s = random.nextDouble();

			IBlockState blockStateAfter;
			if (variant == EnumVariant.MEDIUM) {
				if (s < 0.10) {
					blockStateAfter = ModuleOre.blockOre1.getState(EnumVariantOre1.APATITE_ORE);
				} else {
					blockStateAfter = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
				}
			} else if (variant == EnumVariant.LAPIS) {
				if (s < 0.10) {
					blockStateAfter = ModuleOre.blockOre1.getState(EnumVariantOre1.FLUORITE_ORE);
				} else {
					blockStateAfter = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
				}
			} else if (variant == EnumVariant.TINY) {
				if (s < 0.10) {
					blockStateAfter = ModuleOre.blockOre1.getState(EnumVariantOre1.SULFUR_ORE);
				} else {
					blockStateAfter = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
				}
			} else {
				blockStateAfter = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
			}
			//blockStateAfter = ((BlockColored) Blocks.WOOL).getStateFromMeta(getMetaFromState(state));

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

	//

	public static enum EnumVariant implements IStringSerializable
	{
		TINY(0, "tiny", "tiny"),
		LAPIS(1, "lapis", "lapis"),
		DIAMOND(2, "diamond", "diamond"),
		IRON(3, "iron", "iron"),
		MEDIUM(4, "medium", "medium"),
		LARGE(5, "large", "large"),
		COAL(6, "coal", "coal"),
		HUGE(7, "huge", "huge"),
		;

		//

		private static final EnumVariant[] META_LOOKUP;
		static {
			META_LOOKUP = new EnumVariant[EnumVariant.values().length];
			EnumVariant[] types = EnumVariant.values();
			for (int i = 0; i < types.length; i++) {
				EnumVariant.META_LOOKUP[types[i].metadata] = types[i];
			}
		}

		public static EnumVariant byMetadata(int metadata)
		{
			if (metadata < 0 || metadata >= META_LOOKUP.length) metadata = 0;
			return META_LOOKUP[metadata];
		}

		//

		public final int metadata;
		public final String resourceName;
		public final String unlocalizedName;

		private EnumVariant(int metadata, String resourceName, String unlocalizedName)
		{
			this.metadata = metadata;
			this.resourceName = resourceName;
			this.unlocalizedName = unlocalizedName;
		}

		public String toString()
		{
			return this.resourceName;
		}

		public String getName()
		{
			return this.resourceName;
		}

	}

}
