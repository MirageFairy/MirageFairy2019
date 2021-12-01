package miragefairy2019.mod.modules.oreseed

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import java.util.Random

abstract class WorldGenOreSeedBase(private val blockState: IBlockState, private val isReplaceable: (IBlockState) -> Boolean) : WorldGenerator() {
    protected fun replace(world: World, random: Random, pos: BlockPos) {
        val state = world.getBlockState(pos)
        if (state.block.isReplaceableOreGen(state, world, pos) { isReplaceable(it!!) }) world.setBlockState(pos, blockState, 2)
    }
}

class WorldGenOreSeedString(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        var pos2 = pos
        val posMin = pos2.add(0, 0, 0)
        val posMax = pos2.add(15, 0, 15)
        pos2 = pos2.add(8, 0, 8)

        val poses = mutableSetOf<BlockPos>()
        var variant = random.nextInt(6)

        repeat(100) {
            if (poses.size >= 32) return true

            if (pos2.x < posMin.x) return true
            if (pos2.z < posMin.z) return true
            if (pos2.x > posMax.x) return true
            if (pos2.z > posMax.z) return true

            replace(world, random, pos2)
            poses += pos2

            if (random.nextInt(2) == 0) variant = random.nextInt(6)

            when (variant) {
                0 -> pos2 = pos2.up()
                1 -> pos2 = pos2.down()
                2 -> pos2 = pos2.west()
                3 -> pos2 = pos2.east()
                4 -> pos2 = pos2.north()
                5 -> pos2 = pos2.south()
            }
        }
        return true
    }
}

class WorldGenOreSeedHorizontal(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        val pos2 = pos.add(8, 0, 8)
        val variant = random.nextInt(2)
        if (variant == 0) {
            replace(world, random, pos2.west(4))
            replace(world, random, pos2.west(3))
            replace(world, random, pos2.west(2))
            replace(world, random, pos2.west(1))
            replace(world, random, pos2)
            replace(world, random, pos2.east(1))
            replace(world, random, pos2.east(2))
            replace(world, random, pos2.east(3))
            replace(world, random, pos2.east(4))
        } else {
            replace(world, random, pos2.north(4))
            replace(world, random, pos2.north(3))
            replace(world, random, pos2.north(2))
            replace(world, random, pos2.north(1))
            replace(world, random, pos2)
            replace(world, random, pos2.south(1))
            replace(world, random, pos2.south(2))
            replace(world, random, pos2.south(3))
            replace(world, random, pos2.south(4))
        }
        return true
    }
}

class WorldGenOreSeedVertical(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        val pos2 = pos.add(8, 0, 8)
        replace(world, random, pos2.up(0))
        replace(world, random, pos2.up(1))
        replace(world, random, pos2.up(2))
        replace(world, random, pos2.up(3))
        replace(world, random, pos2.up(4))
        replace(world, random, pos2.up(5))
        replace(world, random, pos2.up(6))
        replace(world, random, pos2.up(7))
        return true
    }
}

class WorldGenOreSeedPoint(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        val pos2 = pos.add(8, 0, 8)
        replace(world, random, pos2)
        return true
    }
}

class WorldGenOreSeedStar(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        val pos2 = pos.add(8, 0, 8)
        replace(world, random, pos2)
        replace(world, random, pos2.up(1))
        replace(world, random, pos2.up(2))
        replace(world, random, pos2.down(1))
        replace(world, random, pos2.down(2))
        replace(world, random, pos2.west(1))
        replace(world, random, pos2.west(2))
        replace(world, random, pos2.east(1))
        replace(world, random, pos2.east(2))
        replace(world, random, pos2.north(1))
        replace(world, random, pos2.north(2))
        replace(world, random, pos2.south(1))
        replace(world, random, pos2.south(2))
        return true
    }
}

class WorldGenOreSeedRing(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        val pos2 = pos.add(8, 0, 8)
        replace(world, random, pos2.add(-1, 0, -1))
        replace(world, random, pos2.add(-1, 0, 0))
        replace(world, random, pos2.add(-1, 0, 1))
        replace(world, random, pos2.add(0, 0, -1))
        replace(world, random, pos2.add(0, 0, 1))
        replace(world, random, pos2.add(1, 0, -1))
        replace(world, random, pos2.add(1, 0, 0))
        replace(world, random, pos2.add(1, 0, 1))
        return true
    }
}

class WorldGenOreSeedPyramid(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        val pos2 = pos.add(8, 0, 8)
        replace(world, random, pos2)
        replace(world, random, pos2.up())
        replace(world, random, pos2.down())
        replace(world, random, pos2.west())
        replace(world, random, pos2.east())
        replace(world, random, pos2.north())
        replace(world, random, pos2.south())
        return true
    }
}

class WorldGenOreSeedCube(blockState: IBlockState, isReplaceable: (IBlockState) -> Boolean) : WorldGenOreSeedBase(blockState, isReplaceable) {
    override fun generate(world: World, random: Random, pos: BlockPos): Boolean {
        val pos2 = pos.add(8, 0, 8)
        replace(world, random, pos2.add(0, 0, 0))
        replace(world, random, pos2.add(0, 0, 1))
        replace(world, random, pos2.add(0, 1, 0))
        replace(world, random, pos2.add(0, 1, 1))
        replace(world, random, pos2.add(1, 0, 0))
        replace(world, random, pos2.add(1, 0, 1))
        replace(world, random, pos2.add(1, 1, 0))
        replace(world, random, pos2.add(1, 1, 1))
        return true
    }
}
