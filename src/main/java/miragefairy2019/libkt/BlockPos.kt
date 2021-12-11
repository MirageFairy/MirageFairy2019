package miragefairy2019.libkt

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i


// BlockPos

operator fun BlockPos.plus(other: Vec3i): BlockPos = add(other)
operator fun BlockPos.minus(other: Vec3i): BlockPos = subtract(other)
val BlockPos.range get() = BlockRange(this, this)


// Iterable<BlockPos>

fun Iterable<BlockPos>.sortedByDistance(from: Vec3i) = sortedBy { it.distanceSq(from) }


// BlockRange

data class BlockRange(val from: BlockPos, val to: BlockPos)

val BlockRange.positions get() = (from.x..to.x).flatMap { x -> (from.y..to.y).flatMap { y -> (from.z..to.z).map { z -> BlockPos(x, y, z) } } }
fun BlockRange.grow(x: Int, y: Int, z: Int) = BlockRange(from.add(-x, -y, -z), to.add(x, y, z))
fun BlockRange.grow(amount: Int) = grow(amount, amount, amount)
fun BlockRange.grow(vec: Vec3i) = grow(vec.x, vec.y, vec.z)
fun BlockRange.shrink(x: Int, y: Int, z: Int) = grow(-x, -y, -z)
fun BlockRange.shrink(amount: Int) = grow(-amount)
fun BlockRange.shrink(vec: Vec3i) = grow(-vec.x, -vec.y, -vec.z)
