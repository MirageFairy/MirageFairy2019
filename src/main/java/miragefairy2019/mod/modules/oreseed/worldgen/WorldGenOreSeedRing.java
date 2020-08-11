package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenOreSeedRing extends WorldGenOreSeedBase
{

	public WorldGenOreSeedRing(IBlockState blockState)
	{
		super(blockState);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = pos.add(8, 0, 8);

		replace(world, random, pos.add(-1, 0, -1));
		replace(world, random, pos.add(-1, 0, 0));
		replace(world, random, pos.add(-1, 0, 1));
		replace(world, random, pos.add(0, 0, -1));
		replace(world, random, pos.add(0, 0, 1));
		replace(world, random, pos.add(1, 0, -1));
		replace(world, random, pos.add(1, 0, 0));
		replace(world, random, pos.add(1, 0, 1));
		return true;
	}

}
