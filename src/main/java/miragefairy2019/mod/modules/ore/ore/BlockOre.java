package miragefairy2019.mod.modules.ore.ore;

import java.util.Random;

import miragefairy2019.mod.lib.multi.BlockMulti;
import miragefairy2019.mod.lib.multi.IListBlockVariant;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOre<V extends IBlockVariantOre> extends BlockMulti<V>
{

	public BlockOre(IListBlockVariant<V> variantList)
	{
		super(Material.ROCK, variantList);

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
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return getVariant(blockState).getHardness();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return getVariant(world.getBlockState(pos)).getResistance();
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
		Random random = world instanceof World ? ((World) world).rand : RANDOM;
		getVariant(state).getDrops(drops, random, this, getMetaFromState(state), fortune);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		Random random = world instanceof World ? ((World) world).rand : new Random();
		return getVariant(state).getExpDrop(random, fortune);
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

}
