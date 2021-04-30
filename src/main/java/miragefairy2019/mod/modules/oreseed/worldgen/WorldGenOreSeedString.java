package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenOreSeedString extends WorldGenOreSeedBase
{

	public WorldGenOreSeedString(IBlockState blockState, Predicate<IBlockState> pReplaceable)
	{
		super(blockState, pReplaceable);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		BlockPos posMin = pos.add(0, 0, 0);
		BlockPos posMax = pos.add(15, 0, 15);

		pos = pos.add(8, 0, 8);

		Set<BlockPos> poses = new HashSet<>();
		int variant = random.nextInt(6);

		for (int i = 0; i < 100; i++) {
			if (poses.size() >= 32) break;

			if (pos.getX() < posMin.getX()) break;
			if (pos.getZ() < posMin.getZ()) break;
			if (pos.getX() > posMax.getX()) break;
			if (pos.getZ() > posMax.getZ()) break;

			replace(world, random, pos);
			poses.add(pos);

			if (random.nextInt(2) == 0) {
				variant = random.nextInt(6);
			}

			if (variant == 0) {
				pos = pos.up();
			} else if (variant == 1) {
				pos = pos.down();
			} else if (variant == 2) {
				pos = pos.west();
			} else if (variant == 3) {
				pos = pos.east();
			} else if (variant == 4) {
				pos = pos.north();
			} else if (variant == 5) {
				pos = pos.south();
			}

		}
		return true;
	}

}
