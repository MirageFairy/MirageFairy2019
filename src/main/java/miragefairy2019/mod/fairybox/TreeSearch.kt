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
        val tag = predicate(blockPos, distance) ?: return false

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
                when (neighborhoodType) {
                    NeighborhoodType.SURFACE -> {
                        if (step(blockPos.add(-1, 0, 0))) return@finish
                        if (step(blockPos.add(1, 0, 0))) return@finish
                        if (step(blockPos.add(0, -1, 0))) return@finish
                        if (step(blockPos.add(0, 1, 0))) return@finish
                        if (step(blockPos.add(0, 0, -1))) return@finish
                        if (step(blockPos.add(0, 0, 1))) return@finish
                    }
                    NeighborhoodType.VERTEX -> {
                        if (step(blockPos.add(-1, -1, -1))) return@finish
                        if (step(blockPos.add(-1, -1, 0))) return@finish
                        if (step(blockPos.add(-1, -1, 1))) return@finish
                        if (step(blockPos.add(-1, 0, -1))) return@finish
                        if (step(blockPos.add(-1, 0, 0))) return@finish
                        if (step(blockPos.add(-1, 0, 1))) return@finish
                        if (step(blockPos.add(-1, 1, -1))) return@finish
                        if (step(blockPos.add(-1, 1, 0))) return@finish
                        if (step(blockPos.add(-1, 1, 1))) return@finish
                        if (step(blockPos.add(0, -1, -1))) return@finish
                        if (step(blockPos.add(0, -1, 0))) return@finish
                        if (step(blockPos.add(0, -1, 1))) return@finish
                        if (step(blockPos.add(0, 0, -1))) return@finish

                        if (step(blockPos.add(0, 0, 1))) return@finish
                        if (step(blockPos.add(0, 1, -1))) return@finish
                        if (step(blockPos.add(0, 1, 0))) return@finish
                        if (step(blockPos.add(0, 1, 1))) return@finish
                        if (step(blockPos.add(1, -1, -1))) return@finish
                        if (step(blockPos.add(1, -1, 0))) return@finish
                        if (step(blockPos.add(1, -1, 1))) return@finish
                        if (step(blockPos.add(1, 0, -1))) return@finish
                        if (step(blockPos.add(1, 0, 0))) return@finish
                        if (step(blockPos.add(1, 0, 1))) return@finish
                        if (step(blockPos.add(1, 1, -1))) return@finish
                        if (step(blockPos.add(1, 1, 0))) return@finish
                        if (step(blockPos.add(1, 1, 1))) return@finish
                    }
                }
            }

            //

            distance++
            lastBlockPoses = nextBlockPoses

        }
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
