package miragefairy2019.mod.lib.multi;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockMulti<V extends IBlockVariant> extends Block
{

	public final IListBlockVariant<V> variantList;

	public BlockMulti(Material material, IListBlockVariant<V> variantList)
	{
		super(material);
		this.variantList = variantList;

		// meta
		setDefaultState(blockState.getBaseState()
			.withProperty(VARIANT, variantList.getDefaultMetadata()));

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

}
