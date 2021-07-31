package miragefairy2019.mod.modules.oreseed;

import miragefairy2019.mod.modules.oreseed.worldgen.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;
import java.util.function.Predicate;

public class WorldGenCompoundOreSeed {

    private WorldGenerator worldGenOreSeed1;
    private WorldGenerator worldGenOreSeed2;
    private WorldGenerator worldGenOreSeed3;
    private WorldGenerator worldGenOreSeed4;
    private WorldGenerator worldGenOreSeed5;
    private WorldGenerator worldGenOreSeed6;
    private WorldGenerator worldGenOreSeed7;
    private WorldGenerator worldGenOreSeed8;

    private WorldGenerator worldGenOreSeed15;
    private WorldGenerator worldGenOreSeed16;
    private WorldGenerator worldGenOreSeed12;
    private WorldGenerator worldGenOreSeed9;
    private WorldGenerator worldGenOreSeed14;
    private WorldGenerator worldGenOreSeed11;
    private WorldGenerator worldGenOreSeed13;
    private WorldGenerator worldGenOreSeed10;

    public WorldGenCompoundOreSeed(BlockOreSeed block, Predicate<IBlockState> pReplaceable) {
        worldGenOreSeed1 = new WorldGenMinable(block.getState(EnumVariantOreSeed.TINY), 5, blockState -> pReplaceable.test(blockState));
        worldGenOreSeed2 = new WorldGenMinable(block.getState(EnumVariantOreSeed.LAPIS), 7, blockState -> pReplaceable.test(blockState));
        worldGenOreSeed3 = new WorldGenMinable(block.getState(EnumVariantOreSeed.DIAMOND), 8, blockState -> pReplaceable.test(blockState));
        worldGenOreSeed4 = new WorldGenMinable(block.getState(EnumVariantOreSeed.IRON), 9, blockState -> pReplaceable.test(blockState));
        worldGenOreSeed5 = new WorldGenMinable(block.getState(EnumVariantOreSeed.MEDIUM), 12, blockState -> pReplaceable.test(blockState));
        worldGenOreSeed6 = new WorldGenMinable(block.getState(EnumVariantOreSeed.LARGE), 15, blockState -> pReplaceable.test(blockState));
        worldGenOreSeed7 = new WorldGenMinable(block.getState(EnumVariantOreSeed.COAL), 17, blockState -> pReplaceable.test(blockState));
        worldGenOreSeed8 = new WorldGenMinable(block.getState(EnumVariantOreSeed.HUGE), 20, blockState -> pReplaceable.test(blockState));

        worldGenOreSeed15 = new WorldGenOreSeedString(block.getState(EnumVariantOreSeed.STRING), pReplaceable);
        worldGenOreSeed16 = new WorldGenOreSeedHorizontal(block.getState(EnumVariantOreSeed.HORIZONTAL), pReplaceable);
        worldGenOreSeed12 = new WorldGenOreSeedVertical(block.getState(EnumVariantOreSeed.VERTICAL), pReplaceable);
        worldGenOreSeed9 = new WorldGenOreSeedPoint(block.getState(EnumVariantOreSeed.POINT), pReplaceable);
        worldGenOreSeed14 = new WorldGenOreSeedStar(block.getState(EnumVariantOreSeed.STAR), pReplaceable);
        worldGenOreSeed11 = new WorldGenOreSeedRing(block.getState(EnumVariantOreSeed.RING), pReplaceable);
        worldGenOreSeed13 = new WorldGenOreSeedPyramid(block.getState(EnumVariantOreSeed.PYRAMID), pReplaceable);
        worldGenOreSeed10 = new WorldGenOreSeedCube(block.getState(EnumVariantOreSeed.CUBE), pReplaceable);
    }

    public void accept(World world, Random random, BlockPos pos) {
        genStandardOre(world, random, pos, 325 * 7 / 8 / 2 / 4, worldGenOreSeed15, 0, 255); // TODO world heightを取得
        genStandardOre(world, random, pos, 263 * 7 / 9 / 2, worldGenOreSeed16, 0, 255);
        genStandardOre(world, random, pos, 263 * 7 / 8 / 2, worldGenOreSeed12, 0, 255);
        genStandardOre(world, random, pos, 263 * 7 / 1 / 2, worldGenOreSeed9, 0, 255);
        genStandardOre(world, random, pos, 263 * 7 / 13 / 2, worldGenOreSeed14, 0, 255);
        genStandardOre(world, random, pos, 263 * 7 / 8 / 2, worldGenOreSeed11, 0, 255);
        genStandardOre(world, random, pos, 263 * 7 / 7 / 2, worldGenOreSeed13, 0, 255);
        genStandardOre(world, random, pos, 263 * 7 / 8 / 2, worldGenOreSeed10, 0, 255);

        genStandardOre(world, random, pos, 474 / 2, worldGenOreSeed1, 0, 255);
        genStandardOre(world, random, pos, 293 / 2, worldGenOreSeed2, 0, 255);
        genStandardOre(world, random, pos, 272 / 2, worldGenOreSeed3, 0, 255);
        genStandardOre(world, random, pos, 263 / 2, worldGenOreSeed4, 0, 255);
        genStandardOre(world, random, pos, 144 / 2, worldGenOreSeed5, 0, 255);
        genStandardOre(world, random, pos, 120 / 2, worldGenOreSeed6, 0, 255);
        genStandardOre(world, random, pos, 90 / 2, worldGenOreSeed7, 0, 255);
        genStandardOre(world, random, pos, 45 / 2, worldGenOreSeed8, 0, 255);
    }

    private void genStandardOre(World world, Random random, BlockPos pos, int count, WorldGenerator generator, int minHeightInclusive, int maxHeightExclusive) {
        for (int j = 0; j < count; ++j) {
            generator.generate(world, random, pos.add(
                    random.nextInt(16),
                    random.nextInt(maxHeightExclusive - minHeightInclusive) + minHeightInclusive,
                    random.nextInt(16)));
        }
    }

}
