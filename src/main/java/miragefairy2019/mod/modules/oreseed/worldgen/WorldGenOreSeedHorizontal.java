package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenOreSeedHorizontal extends WorldGenOreSeedBase
{

	public WorldGenOreSeedHorizontal(IBlockState blockState, Predicate<IBlockState> pReplaceable)
	{
		super(blockState, pReplaceable);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = pos.add(8, 0, 8);

		int variant = random.nextInt(2);
		if (variant == 0) {
			replace(world, random, pos.west(4));
			replace(world, random, pos.west(3));
			replace(world, random, pos.west(2));
			replace(world, random, pos.west(1));
			replace(world, random, pos);
			replace(world, random, pos.east(1));
			replace(world, random, pos.east(2));
			replace(world, random, pos.east(3));
			replace(world, random, pos.east(4));
		} else {
			replace(world, random, pos.north(4));
			replace(world, random, pos.north(3));
			replace(world, random, pos.north(2));
			replace(world, random, pos.north(1));
			replace(world, random, pos);
			replace(world, random, pos.south(1));
			replace(world, random, pos.south(2));
			replace(world, random, pos.south(3));
			replace(world, random, pos.south(4));
		}
		return true;
	}

}
