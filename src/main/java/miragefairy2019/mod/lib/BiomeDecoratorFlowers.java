package miragefairy2019.mod.lib;

import kotlin.jvm.functions.Function1;
import mirrg.boron.util.UtilsMath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

public final class BiomeDecoratorFlowers {

    private WorldGenerator worldGenerator;
    private double trialsPerChunk;
    private Function1<Biome, Boolean> canGenerate;

    public BiomeDecoratorFlowers(WorldGenerator worldGenerator, double trialsPerChunk, Function1<Biome, Boolean> canGenerate) {
        this.worldGenerator = worldGenerator;
        this.trialsPerChunk = trialsPerChunk;
        this.canGenerate = canGenerate;
    }

    public void decorate(DecorateBiomeEvent.Post event) {
        int trialCount = UtilsMath.randomInt(event.getRand(), trialsPerChunk);

        for (int i = 0; i < trialCount; ++i) {
            int offsetX = event.getRand().nextInt(16);
            int offsetZ = event.getRand().nextInt(16);
            BlockPos pos1 = event.getChunkPos().getBlock(8, 0, 8);
            BlockPos pos2 = pos1.add(offsetX, 0, offsetZ);
            int yMax = event.getWorld().getHeight(pos2).getY() + 32;
            if (yMax > 0) {

                BlockPos pos3 = pos2.add(0, event.getRand().nextInt(yMax), 0);
                if (canGenerate.invoke(event.getWorld().getBiome(pos3))) {
                    worldGenerator.generate(event.getWorld(), event.getRand(), pos3);
                }

            }
        }
    }

}
