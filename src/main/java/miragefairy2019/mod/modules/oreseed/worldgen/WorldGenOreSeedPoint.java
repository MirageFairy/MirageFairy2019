package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenOreSeedPoint extends WorldGenOreSeedBase
{

	public WorldGenOreSeedPoint(IBlockState blockState)
	{
		super(blockState);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = pos.add(8, 0, 8);

		replace(world, random, pos);
		return true;
	}

}
