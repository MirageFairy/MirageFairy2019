package miragefairy2019.mod.modules.ore.material;

import java.util.Random;

import javax.annotation.Nullable;

import miragefairy2019.mod.lib.multi.BlockMulti;
import miragefairy2019.mod.lib.multi.IListBlockVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Override
	public Material getMaterial(IBlockState state)
	{
		return getVariant(state).getMaterial();
	}

	@Deprecated
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return getVariant(blockState).getBlockHardness();
	}

	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		return getVariant(world.getBlockState(pos)).getBlockHardness() * 5;
	}
	//

	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
	{
		return getVariant(state).getSoundType();
	}

	//

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}

	//

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!getVariant(state).isFallable()) return;
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!getVariant(state).isFallable()) return;
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!getVariant(state).isFallable()) return;
		if (!worldIn.isRemote) checkFallable(worldIn, pos);
	}

	private void checkFallable(World worldIn, BlockPos pos)
	{
		if ((worldIn.isAirBlock(pos.down()) || BlockFalling.canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {

			if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
				if (!worldIn.isRemote) {
					EntityFallingBlock entityFallingBlock = new EntityFallingBlock(
						worldIn,
						pos.getX() + 0.5,
						pos.getY(),
						pos.getZ() + 0.5,
						worldIn.getBlockState(pos));
					worldIn.spawnEntity(entityFallingBlock);
				}
			} else {
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockToAir(pos);

				BlockPos blockpos = pos.down();
				while ((worldIn.isAirBlock(blockpos) || BlockFalling.canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0) {
					blockpos = blockpos.down();
				}

				if (blockpos.getY() > 0) {
					worldIn.setBlockState(blockpos.up(), state);
				}
			}
		}
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (!getVariant(stateIn).isFallable()) return;

		if (rand.nextInt(16) == 0) {
			BlockPos blockPos = pos.down();
			if (BlockFalling.canFallThrough(worldIn.getBlockState(blockPos))) {
				double x = pos.getX() + rand.nextFloat();
				double y = pos.getY() - 0.05;
				double z = pos.getZ() + rand.nextFloat();
				worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0, 0, 0, Block.getStateId(stateIn));
			}
		}
	}

}
