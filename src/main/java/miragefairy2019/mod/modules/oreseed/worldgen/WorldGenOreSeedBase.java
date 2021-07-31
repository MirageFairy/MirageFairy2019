package miragefairy2019.mod.modules.oreseed.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenOreSeedBase extends WorldGenerator {

    private final IBlockState blockState;
    private final Predicate<IBlockState> pReplaceable;

    public WorldGenOreSeedBase(IBlockState blockState, Predicate<IBlockState> pReplaceable) {
        this.blockState = blockState;
        this.pReplaceable = pReplaceable;
    }

    protected void replace(World world, Random random, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().isReplaceableOreGen(state, world, pos, blockState -> pReplaceable.test(blockState))) {
            world.setBlockState(pos, blockState, 2);
        }
    }

}
