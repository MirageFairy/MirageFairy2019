package miragefairy2019.libkt

import net.minecraft.block.BlockBush
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import java.util.Random

class WorldGenBush(private val block: BlockBush, private val state: IBlockState) : WorldGenerator() {
    var blockCountMin = 1
    var blockCountMax = 64
    var generateTryCount = 64
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        var count = 0
        val countLimit = random.nextInt(blockCountMin..blockCountMax)
        run limited@{
            repeat(generateTryCount) nextChance@{
                val pos2 = pos.add(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8)
                )

                if (!world.isAirBlock(pos2)) return@nextChance // 空気しか置換できない
                if (world.provider.isNether && pos2.y >= world.height - 1) return@nextChance // ネザー上Voidには置けない
                if (!block.canBlockStay(world, pos2, state)) return@nextChance // その場所に置けなければならない

                world.setBlockState(pos2, state, 2)
                count++
                if (count >= countLimit) return@limited
            }
        }
        return true
    }
}
