package miragefairy2019.libkt

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import kotlin.math.abs


// BlockPos

operator fun BlockPos.plus(other: Vec3i): BlockPos = add(other)
operator fun BlockPos.minus(other: Vec3i): BlockPos = subtract(other)
val BlockPos.region get() = BlockRegion(this, this)
infix fun BlockPos.norm1(other: BlockPos) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)


// Iterable<BlockPos>

fun Iterable<BlockPos>.sortedByDistance(from: Vec3i) = sortedBy { it.distanceSq(from) }


// BlockRegion

data class BlockRegion(val from: BlockPos, val to: BlockPos)

inline fun BlockRegion.forEach(block: (x: Int, y: Int, z: Int) -> Unit) {
    (from.x..to.x).forEach { x ->
        (from.y..to.y).forEach { y ->
            (from.z..to.z).forEach { z ->
                block(x, y, z)
            }
        }
    }
}

val BlockRegion.positions get() = (from.x..to.x).flatMap { x -> (from.y..to.y).flatMap { y -> (from.z..to.z).map { z -> BlockPos(x, y, z) } } }
fun BlockRegion.grow(x: Int, y: Int, z: Int) = BlockRegion(from.add(-x, -y, -z), to.add(x, y, z))
fun BlockRegion.grow(amount: Int) = grow(amount, amount, amount)
fun BlockRegion.grow(vec: Vec3i) = grow(vec.x, vec.y, vec.z)
fun BlockRegion.shrink(x: Int, y: Int, z: Int) = grow(-x, -y, -z)
fun BlockRegion.shrink(amount: Int) = grow(-amount)
fun BlockRegion.shrink(vec: Vec3i) = grow(-vec.x, -vec.y, -vec.z)
