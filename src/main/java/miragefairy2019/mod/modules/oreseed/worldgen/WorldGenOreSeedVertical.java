package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenOreSeedVertical extends WorldGenOreSeedBase
{

	public WorldGenOreSeedVertical(IBlockState blockState, Predicate<IBlockState> pReplaceable)
	{
		super(blockState, pReplaceable);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = pos.add(8, 0, 8);

		replace(world, random, pos.up(0));
		replace(world, random, pos.up(1));
		replace(world, random, pos.up(2));
		replace(world, random, pos.up(3));
		replace(world, random, pos.up(4));
		replace(world, random, pos.up(5));
		replace(world, random, pos.up(6));
		replace(world, random, pos.up(7));
		return true;
	}

}
