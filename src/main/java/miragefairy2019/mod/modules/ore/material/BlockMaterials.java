package miragefairy2019.mod.modules.ore.material;

import miragefairy2019.mod.modules.ore.IOreVariantList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMaterials<V extends IBlockVariantMaterials> extends Block
{

	public final IOreVariantList<V> variantList;

	public BlockMaterials(IOreVariantList<V> variantList)
	{
		super(Material.IRON);
		this.variantList = variantList;

		// meta
		setDefaultState(blockState.getBaseState()
			.withProperty(VARIANT, variantList.getDefaultMetadata()));

		// style
		setSoundType(SoundType.STONE);

		// 挙動
		setHardness(3.0F);
		setResistance(5.0F);

		for (V variant : variantList) {
			setHarvestLevel(variant.getHarvestTool(), variant.getHarvestLevel(), getState(variant));
		}

	}

	//

	public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT);
	}

	public IBlockState getState(V variant)
	{
		return getDefaultState().withProperty(VARIANT, variant.getMetadata());
	}

	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return getDefaultState().withProperty(VARIANT, metadata);
	}

	@Override
	public int getMetaFromState(IBlockState blockState)
	{
		return blockState.getValue(VARIANT);
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> itemStacks)
	{
		for (V variant : variantList) {
			itemStacks.add(new ItemStack(this, 1, variant.getMetadata()));
		}
	}

	//

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(state));
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.add(new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(state)));
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}

}
