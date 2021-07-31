package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenOreSeedPyramid extends WorldGenOreSeedBase {

    public WorldGenOreSeedPyramid(IBlockState blockState, Predicate<IBlockState> pReplaceable) {
        super(blockState, pReplaceable);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        pos = pos.add(8, 0, 8);

        replace(world, random, pos);
        replace(world, random, pos.up());
        replace(world, random, pos.down());
        replace(world, random, pos.west());
        replace(world, random, pos.east());
        replace(world, random, pos.north());
        replace(world, random, pos.south());
        return true;
    }

}
