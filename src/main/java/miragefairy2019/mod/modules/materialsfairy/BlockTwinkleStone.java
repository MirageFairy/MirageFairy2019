package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.lib.multi.BlockMulti;
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

public class BlockTwinkleStone extends BlockMulti<EnumVariantTwinkleStone>
{

	public BlockTwinkleStone()
	{
		super(Material.ROCK, EnumVariantTwinkleStone.variantList);

		// style
		setSoundType(SoundType.STONE);

		// 挙動
		setHardness(3.0F);
		setResistance(5.0F);

		for (EnumVariantTwinkleStone variant : variantList) {
			setHarvestLevel("pickaxe", 0, getState(variant));
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

	//

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return variantList.byMetadata(getMetaFromState(state)).lightValue;
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type)
	{
		return false;
	}

}
