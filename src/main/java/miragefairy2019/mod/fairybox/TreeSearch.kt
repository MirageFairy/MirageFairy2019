package miragefairy2019.mod.fairybox

import miragefairy2019.libkt.darkRed
import miragefairy2019.libkt.textComponent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


sealed class TreeSearchException : Exception()

class TooLargeTreeSearchException : TreeSearchException()

class PartiallyUnloadedTreeSearchException : TreeSearchException()

enum class NeighborhoodType { SURFACE, VERTEX }

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
    neighborhoodType: NeighborhoodType = NeighborhoodType.SURFACE,
    tooLargeBehaviour: TooLargeBehaviour = TooLargeBehaviour.EXCEPTION,
    unloadedPositionBehaviour: UnloadedPositionBehaviour = UnloadedPositionBehaviour.LOAD,
    predicate: (blockPos: BlockPos, distance: Int) -> T?
): List<SearchResultEntry<T>> {

    val resultEntries = mutableListOf<SearchResultEntry<T>>()
    var nextBlockPoses = mutableListOf<BlockPos>()
    var distance = startDistance

    fun step(blockPos: BlockPos) {
        if (blockPos in visitedBlockPoses) return
        visitedBlockPoses += blockPos

        // 一部がチャンクロード範囲外にある場合
        when (unloadedPositionBehaviour) {
            UnloadedPositionBehaviour.LOAD -> Unit // 強制的に踏む
            UnloadedPositionBehaviour.IGNORE -> {
                if (!world.isBlockLoaded(blockPos)) return  // 踏まずに飛ばす
            }
            UnloadedPositionBehaviour.EXCEPTION -> {
                if (!world.isBlockLoaded(blockPos)) throw PartiallyUnloadedTreeSearchException() // 例外にする
            }
        }

        // 判定
        val tag = predicate(blockPos, distance) ?: return

        // 成功

        resultEntries += SearchResultEntry(blockPos, distance, tag)
        nextBlockPoses.add(blockPos)

        if (tooLargeBehaviour == TooLargeBehaviour.EXCEPTION) {
            if (maxDistance != null && distance > maxDistance) throw TooLargeTreeSearchException() // 距離の上限を超えている場合は例外
        }
        if (maxSize != null && resultEntries.size > maxSize) throw TooLargeTreeSearchException() // 大きすぎる場合は例外

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

    while (true) {

        if (tooLargeBehaviour == TooLargeBehaviour.IGNORE) {
            if (maxDistance != null && distance > maxDistance) break // 距離の上限を超えている場合は終了
        }

        if (lastBlockPoses.isEmpty()) break

        nextBlockPoses = mutableListOf()
        lastBlockPoses.forEach { blockPos ->
            when (neighborhoodType) {
                NeighborhoodType.SURFACE -> {
                    step(blockPos.add(-1, 0, 0))
                    step(blockPos.add(1, 0, 0))
                    step(blockPos.add(0, -1, 0))
                    step(blockPos.add(0, 1, 0))
                    step(blockPos.add(0, 0, -1))
                    step(blockPos.add(0, 0, 1))
                }
                NeighborhoodType.VERTEX -> {
                    step(blockPos.add(-1, -1, -1))
                    step(blockPos.add(-1, -1, 0))
                    step(blockPos.add(-1, -1, 1))
                    step(blockPos.add(-1, 0, -1))
                    step(blockPos.add(-1, 0, 0))
                    step(blockPos.add(-1, 0, 1))
                    step(blockPos.add(-1, 1, -1))
                    step(blockPos.add(-1, 1, 0))
                    step(blockPos.add(-1, 1, 1))
                    step(blockPos.add(0, -1, -1))
                    step(blockPos.add(0, -1, 0))
                    step(blockPos.add(0, -1, 1))
                    step(blockPos.add(0, 0, -1))

                    step(blockPos.add(0, 0, 1))
                    step(blockPos.add(0, 1, -1))
                    step(blockPos.add(0, 1, 0))
                    step(blockPos.add(0, 1, 1))
                    step(blockPos.add(1, -1, -1))
                    step(blockPos.add(1, -1, 0))
                    step(blockPos.add(1, -1, 1))
                    step(blockPos.add(1, 0, -1))
                    step(blockPos.add(1, 0, 0))
                    step(blockPos.add(1, 0, 1))
                    step(blockPos.add(1, 1, -1))
                    step(blockPos.add(1, 1, 0))
                    step(blockPos.add(1, 1, 1))
                }
            }
        }

        //

        distance++
        lastBlockPoses = nextBlockPoses

    }

    return resultEntries
}


class PartiallyUnloadedException : TreeCompileException(textComponent { "構造物の一部がロード範囲外にあります"().darkRed }) // TRANSLATE
class TooLargeTreeException : TreeCompileException(textComponent { "構造物が大きすぎます"().darkRed }) // TRANSLATE

/**
 * @return [startBlockPosList]に含まれる要素を含まない、探索された返却要素のリスト
 */
@Throws(TreeCompileException::class)
fun <R : Any> treeSearch(world: World, startBlockPosList: List<BlockPos>, maxSize: Int = 10000, matcher: (world: World, blockPos: BlockPos, distance: Int) -> R?): List<R> {
    val result = mutableSetOf<R>() // 受理されたブロックの返却オブジェクトのリスト
    val known = mutableSetOf<BlockPos>() // waitingへの追加を飛ばしてよいブロック
    var waiting = mutableSetOf<BlockPos>() // 次のループでマッチ判定を行うブロック

    fun step(blockPos2: BlockPos) {
        if (blockPos2 in known) return // その座標が確認済みなら判定をしない
        known += blockPos2 // その座標を確認済みに指定
        waiting.add(blockPos2) // そのブロックを処理待ちにする
    }

    // 開始座標は再び判定を行わない
    known.addAll(startBlockPosList)

    // 開始座標の処理
    startBlockPosList.forEach a@{ blockPos ->
        step(blockPos.up())
        step(blockPos.down())
        step(blockPos.north())
        step(blockPos.south())
        step(blockPos.west())
        step(blockPos.east())
    }

    var distance = 1
    while (waiting.isNotEmpty()) {

        // waitingの中のすべての要素を掬ってprocessingに入れる
        val processing = waiting
        waiting = mutableSetOf()

        processing.forEach a@{ blockPos ->

            // 一部がチャンクロード範囲外にある場合はコンパイルエラー
            if (!world.isBlockLoaded(blockPos)) throw PartiallyUnloadedException()

            // そのブロックがマッチしないなら何もしない
            val r = matcher(world, blockPos, distance) ?: return@a

            // マッチした

            // 大きすぎる探索はコンパイルエラー
            if (result.size + 1 > maxSize) throw TooLargeTreeException()

            result += r // その返却アイテムを出力リストに指定する

            step(blockPos.up())
            step(blockPos.down())
            step(blockPos.north())
            step(blockPos.south())
            step(blockPos.west())
            step(blockPos.east())

        }

        distance++
    }

    return result.toList()
}
