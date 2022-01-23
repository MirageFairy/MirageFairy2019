package miragefairy2019.mod.lib;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenBush extends WorldGenerator {

    private BlockBush block;
    private IBlockState state;

    public int blockCountMin = 1;
    public int blockCountMax = 64;
    public int generateTryCount = 64;

    public WorldGenBush(BlockBush block, IBlockState state) {
        this.block = block;
        this.state = state;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        int count = 0;
        int countLimit = blockCountMin + random.nextInt(blockCountMax - blockCountMin + 1);

        for (int i = 0; i < generateTryCount; i++) {
            BlockPos pos2 = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(pos2)
                && (!world.provider.isNether() || pos2.getY() < 255)
                && block.canBlockStay(world, pos2, state)) {

                world.setBlockState(pos2, state, 2);

                count++;
                if (count >= countLimit) break;

            }

        }

        return true;
    }

}
