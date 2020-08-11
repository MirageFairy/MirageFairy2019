package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenOreSeedBase extends WorldGenerator
{

	protected final IBlockState blockState;

	public WorldGenOreSeedBase(IBlockState blockState)
	{
		this.blockState = blockState;
	}

	protected void replace(World world, Random random, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isReplaceableOreGen(state, world, pos, bs -> {
			if (bs == null || bs.getBlock() != Blocks.STONE) return false;
			return bs.getValue(BlockStone.VARIANT).isNatural();
		})) {
			world.setBlockState(pos, blockState, 2);
		}
	}

}
