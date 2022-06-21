package miragefairy2019.libkt

import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.WorldGenerator
import net.minecraftforge.event.terraingen.DecorateBiomeEvent

class BiomeDecoratorFlowers(
    private val worldGenerator: WorldGenerator,
    private val chancesPerChunk: Double,
    private val canGenerate: (Biome) -> Boolean
) {
    fun decorate(event: DecorateBiomeEvent.Post) {
        repeat(event.rand.randomInt(chancesPerChunk)) {
            val pos = event.chunkPos.getBlock(
                8 + event.rand.nextInt(16),
                0,
                8 + event.rand.nextInt(16)
            )
            val yMax = event.world.getHeight(pos).y + 32
            if (yMax > 0) {
                val pos2 = pos.add(0, event.rand.nextInt(yMax), 0)
                if (canGenerate(event.world.getBiome(pos2))) {
                    worldGenerator.generate(event.world, event.rand, pos2)
                }
            }
        }
    }
}
