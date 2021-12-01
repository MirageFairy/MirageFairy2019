package miragefairy2019.mod.modules.oreseed

import net.minecraft.block.state.IBlockState
import net.minecraft.world.gen.feature.WorldGenerator
import net.minecraft.world.World
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.feature.WorldGenMinable
import java.util.Random

class WorldGenCompoundOreSeed(block: BlockOreSeed, isReplaceable: (IBlockState) -> Boolean) {
    private val worldGenTiny = WorldGenMinable(block.getState(EnumVariantOreSeed.TINY), 5) { isReplaceable(it!!) }
    private val worldGenLapis = WorldGenMinable(block.getState(EnumVariantOreSeed.LAPIS), 7) { isReplaceable(it!!) }
    private val worldGenDiamond = WorldGenMinable(block.getState(EnumVariantOreSeed.DIAMOND), 8) { isReplaceable(it!!) }
    private val worldGenIron = WorldGenMinable(block.getState(EnumVariantOreSeed.IRON), 9) { isReplaceable(it!!) }
    private val worldGenMedium = WorldGenMinable(block.getState(EnumVariantOreSeed.MEDIUM), 12) { isReplaceable(it!!) }
    private val worldGenLarge = WorldGenMinable(block.getState(EnumVariantOreSeed.LARGE), 15) { isReplaceable(it!!) }
    private val worldGenCoal = WorldGenMinable(block.getState(EnumVariantOreSeed.COAL), 17) { isReplaceable(it!!) }
    private val worldGenHuge = WorldGenMinable(block.getState(EnumVariantOreSeed.HUGE), 20) { isReplaceable(it!!) }
    private val worldGenString = WorldGenOreSeedString(block.getState(EnumVariantOreSeed.STRING), isReplaceable)
    private val worldGenHorizontal = WorldGenOreSeedHorizontal(block.getState(EnumVariantOreSeed.HORIZONTAL), isReplaceable)
    private val worldGenVertical = WorldGenOreSeedVertical(block.getState(EnumVariantOreSeed.VERTICAL), isReplaceable)
    private val worldGenPoint = WorldGenOreSeedPoint(block.getState(EnumVariantOreSeed.POINT), isReplaceable)
    private val worldGenStar = WorldGenOreSeedStar(block.getState(EnumVariantOreSeed.STAR), isReplaceable)
    private val worldGenRing = WorldGenOreSeedRing(block.getState(EnumVariantOreSeed.RING), isReplaceable)
    private val worldGenPyramid = WorldGenOreSeedPyramid(block.getState(EnumVariantOreSeed.PYRAMID), isReplaceable)
    private val worldGenCube = WorldGenOreSeedCube(block.getState(EnumVariantOreSeed.CUBE), isReplaceable)
    fun accept(world: World, random: Random, pos: BlockPos) {
        // TODO world heightを取得
        fun genStandardOre(world: World, random: Random, pos: BlockPos, count: Int, generator: WorldGenerator) =
            repeat(count) { generator.generate(world, random, pos.add(random.nextInt(16), random.nextInt(255), random.nextInt(16))) }

        genStandardOre(world, random, pos, 325 * 7 / 8 / 2 / 4, worldGenTiny)
        genStandardOre(world, random, pos, 263 * 7 / 9 / 2, worldGenLapis)
        genStandardOre(world, random, pos, 263 * 7 / 8 / 2, worldGenDiamond)
        genStandardOre(world, random, pos, 263 * 7 / 1 / 2, worldGenIron)
        genStandardOre(world, random, pos, 263 * 7 / 13 / 2, worldGenMedium)
        genStandardOre(world, random, pos, 263 * 7 / 8 / 2, worldGenLarge)
        genStandardOre(world, random, pos, 263 * 7 / 7 / 2, worldGenCoal)
        genStandardOre(world, random, pos, 263 * 7 / 8 / 2, worldGenHuge)
        genStandardOre(world, random, pos, 474 / 2, worldGenString)
        genStandardOre(world, random, pos, 293 / 2, worldGenHorizontal)
        genStandardOre(world, random, pos, 272 / 2, worldGenVertical)
        genStandardOre(world, random, pos, 263 / 2, worldGenPoint)
        genStandardOre(world, random, pos, 144 / 2, worldGenStar)
        genStandardOre(world, random, pos, 120 / 2, worldGenRing)
        genStandardOre(world, random, pos, 90 / 2, worldGenPyramid)
        genStandardOre(world, random, pos, 45 / 2, worldGenCube)
    }
}
