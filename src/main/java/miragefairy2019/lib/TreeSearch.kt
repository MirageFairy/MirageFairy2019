package miragefairy2019.lib

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

sealed class TreeSearchException : Exception()

class TooLargeTreeSearchException : TreeSearchException()

class PartiallyUnloadedTreeSearchException : TreeSearchException()

object NeighborhoodType {
    val SURFACE: (BlockPos) -> Iterator<BlockPos> = {
        iterator {
            yield(it.add(-1, 0, 0))
            yield(it.add(1, 0, 0))
            yield(it.add(0, -1, 0))
            yield(it.add(0, 1, 0))
            yield(it.add(0, 0, -1))
            yield(it.add(0, 0, 1))
        }
    }
    val VERTEX: (BlockPos) -> Iterator<BlockPos> = {
        iterator {
            yield(it.add(-1, -1, -1))
            yield(it.add(-1, -1, 0))
            yield(it.add(-1, -1, 1))
            yield(it.add(-1, 0, -1))
            yield(it.add(-1, 0, 0))
            yield(it.add(-1, 0, 1))
            yield(it.add(-1, 1, -1))
            yield(it.add(-1, 1, 0))
            yield(it.add(-1, 1, 1))
            yield(it.add(0, -1, -1))
            yield(it.add(0, -1, 0))
            yield(it.add(0, -1, 1))
            yield(it.add(0, 0, -1))

            yield(it.add(0, 0, 1))
            yield(it.add(0, 1, -1))
            yield(it.add(0, 1, 0))
            yield(it.add(0, 1, 1))
            yield(it.add(1, -1, -1))
            yield(it.add(1, -1, 0))
            yield(it.add(1, -1, 1))
            yield(it.add(1, 0, -1))
            yield(it.add(1, 0, 0))
            yield(it.add(1, 0, 1))
            yield(it.add(1, 1, -1))
            yield(it.add(1, 1, 0))
            yield(it.add(1, 1, 1))
        }
    }
}

enum class TooLargeBehaviour { IGNORE, EXCEPTION }

enum class UnloadedPositionBehaviour { LOAD, IGNORE, EXCEPTION }

class SearchResultEntry<out T>(val blockPos: BlockPos, val distance: Int, val tag: T)

val List<SearchResultEntry<*>>.blockPoses get() = this.map { it.blockPos }

/**
 * @param includeZero 真の場合、開始座標にもステップ判定を行います。偽の場合、開始座標は確定済みとして起点にします。
 * @throws TreeSearchException
 */
fun <T : Any> treeSearch(
    world: World,
    startBlockPoses: List<BlockPos>,
    visitedBlockPoses: MutableSet<BlockPos>,
    maxDistance: Int? = null,
    maxSize: Int? = null,
    startDistance: Int = 0,
    includeZero: Boolean = false,
    neighborhoodType: (BlockPos) -> Iterator<BlockPos> = NeighborhoodType.SURFACE,
    tooLargeBehaviour: TooLargeBehaviour = TooLargeBehaviour.EXCEPTION,
    unloadedPositionBehaviour: UnloadedPositionBehaviour = UnloadedPositionBehaviour.LOAD,
    validator: (blockPos: BlockPos, distance: Int) -> T?
): List<SearchResultEntry<T>> {

    val resultEntries = mutableListOf<SearchResultEntry<T>>()
    var nextBlockPoses = mutableListOf<BlockPos>()
    var distance = startDistance

    fun step(blockPos: BlockPos): Boolean {
        if (blockPos in visitedBlockPoses) return false
        visitedBlockPoses += blockPos

        if (!world.isBlockLoaded(blockPos)) { // 一部がチャンクロード範囲外にある
            when (unloadedPositionBehaviour) {
                UnloadedPositionBehaviour.LOAD -> Unit // 強制的に踏む
                UnloadedPositionBehaviour.IGNORE -> return false // 踏まずに飛ばす
                UnloadedPositionBehaviour.EXCEPTION -> throw PartiallyUnloadedTreeSearchException() // 例外にする
            }
        }

        // 判定
        val tag = validator(blockPos, distance) ?: return false

        // 成功

        if (maxDistance != null && distance > maxDistance) { // 距離の上限を超えているブロックが見つかった
            when (tooLargeBehaviour) {
                TooLargeBehaviour.IGNORE -> return true
                TooLargeBehaviour.EXCEPTION -> throw TooLargeTreeSearchException()
            }
        }
        if (maxSize != null && resultEntries.size >= maxSize) { // 個数の上限に達しているにもかかわらずブロックが見つかった
            when (tooLargeBehaviour) {
                TooLargeBehaviour.IGNORE -> return true
                TooLargeBehaviour.EXCEPTION -> throw TooLargeTreeSearchException()
            }
        }

        resultEntries += SearchResultEntry(blockPos, distance, tag)
        nextBlockPoses.add(blockPos)

        return false
    }

    var lastBlockPoses: List<BlockPos>

    if (includeZero) {

        startBlockPoses.forEach { blockPos ->
            step(blockPos)
        }

        distance++
        lastBlockPoses = nextBlockPoses

    } else {

        visitedBlockPoses += startBlockPoses // どちらにしろゼロ座標は再び判定しない

        distance++
        lastBlockPoses = startBlockPoses

    }

    run finish@{
        while (true) {

            if (lastBlockPoses.isEmpty()) return@finish

            nextBlockPoses = mutableListOf()
            lastBlockPoses.forEach { blockPos ->
                neighborhoodType(blockPos).forEach {
                    if (step(it)) return@finish
                }
            }

            //

            distance++
            lastBlockPoses = nextBlockPoses

        }
    }

    return resultEntries
}
