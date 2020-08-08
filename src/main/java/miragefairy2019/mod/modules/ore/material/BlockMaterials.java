package miragefairy2019.mod.modules.ore.material;

import miragefairy2019.mod.lib.multi.BlockMulti;
import miragefairy2019.mod.lib.multi.IListBlockVariant;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMaterials<V extends IBlockVariantMaterials> extends BlockMulti<V>
{

	public BlockMaterials(IListBlockVariant<V> variantList)
	{
		super(Material.IRON, variantList);

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
