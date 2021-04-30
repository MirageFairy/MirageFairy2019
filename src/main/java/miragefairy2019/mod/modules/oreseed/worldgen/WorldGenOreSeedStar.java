package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenOreSeedStar extends WorldGenOreSeedBase
{

	public WorldGenOreSeedStar(IBlockState blockState, Predicate<IBlockState> pReplaceable)
	{
		super(blockState, pReplaceable);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = pos.add(8, 0, 8);

		replace(world, random, pos);
		replace(world, random, pos.up(1));
		replace(world, random, pos.up(2));
		replace(world, random, pos.down(1));
		replace(world, random, pos.down(2));
		replace(world, random, pos.west(1));
		replace(world, random, pos.west(2));
		replace(world, random, pos.east(1));
		replace(world, random, pos.east(2));
		replace(world, random, pos.north(1));
		replace(world, random, pos.north(2));
		replace(world, random, pos.south(1));
		replace(world, random, pos.south(2));
		return true;
	}

}
