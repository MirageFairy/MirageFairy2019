package miragefairy2019.mod.fairybox

import miragefairy2019.libkt.darkRed
import miragefairy2019.libkt.textComponent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

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
