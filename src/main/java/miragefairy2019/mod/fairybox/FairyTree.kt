package miragefairy2019.mod.fairybox

import miragefairy2019.libkt.darkRed
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.artifacts.BlockFairyWoodLog
import mirrg.kotlin.hydrogen.atLeast
import net.minecraft.block.BlockLeaves
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

// Compile

class MultipleFairyBoxException : TreeCompileException(textComponent { "栄養の取り合いになっています"().darkRed }) // TRANSLATE

data class Leaves(
    /**
     * 葉ブロックの座標が各葉において最寄りの原木とのマンハッタン距離から算出される重みに従った個数だけ多重に含まれるリストです。
     *
     * 個数は原木との距離が1のときに4で、距離が1離れるごとに減り、距離5で0になります。
     *
     * lightLevelに係数をかけるよりもこの重み付きリストを使った方がオーラ吸収量のばらつきを抑えられます。
     */
    val blockPosList: List<BlockPos>
)

/**
 * @throws TreeCompileException
 * @throws TreeSearchException
 */
fun compileFairyTree(world: World, originBlockPos: BlockPos): Leaves {
    fun checkLog(blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockFairyWoodLog -> false
        is BlockFairyBoxBase -> true
        else -> null
    }

    fun checkLeaves(blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockLeaves -> Unit
        else -> null
    }

    // 起点座標を基点とした26隣接のメイン幹
    val mainStemResult = treeSearch(
        world,
        listOf(originBlockPos),
        mutableSetOf(),
        maxSize = 100,
        includeZero = true,
        neighborhoodType = NeighborhoodType.VERTEX,
        unloadedPositionBehaviour = UnloadedPositionBehaviour.EXCEPTION
    ) { blockPos, _ -> checkLog(blockPos) }

    // メイン幹に6隣接する葉ブロック
    val originalLeavesResult = treeSearch(
        world,
        mainStemResult.blockPoses,
        mainStemResult.blockPoses.toMutableSet(),
        maxDistance = 1,
        maxSize = 2000,
        includeZero = false,
        neighborhoodType = NeighborhoodType.SURFACE,
        tooLargeBehaviour = TooLargeBehaviour.IGNORE,
        unloadedPositionBehaviour = UnloadedPositionBehaviour.EXCEPTION
    ) { blockPos, _ -> checkLeaves(blockPos) }

    // メイン幹に所属するすべての葉ブロック
    val leavesResult = treeSearch(
        world,
        originalLeavesResult.blockPoses,
        mainStemResult.blockPoses.toMutableSet(),
        maxSize = 2000,
        startDistance = 1,
        includeZero = true,
        neighborhoodType = NeighborhoodType.SURFACE,
        unloadedPositionBehaviour = UnloadedPositionBehaviour.EXCEPTION
    ) { blockPos, _ -> checkLeaves(blockPos) }

    // メイン幹の葉ブロックに6隣接する外部の幹
    val originalExternalStemsResult = treeSearch(
        world,
        leavesResult.blockPoses,
        (mainStemResult + leavesResult).blockPoses.toMutableSet(),
        maxSize = 100,
        includeZero = false,
        neighborhoodType = NeighborhoodType.SURFACE,
        unloadedPositionBehaviour = UnloadedPositionBehaviour.EXCEPTION
    ) { blockPos, _ -> checkLog(blockPos) }

    // 外部幹の起点から26隣接で繋がる幹ブロック
    val externalStemsResult = treeSearch(
        world,
        originalExternalStemsResult.blockPoses,
        (mainStemResult + leavesResult).blockPoses.toMutableSet(),
        maxSize = 100,
        includeZero = true,
        neighborhoodType = NeighborhoodType.VERTEX,
        unloadedPositionBehaviour = UnloadedPositionBehaviour.EXCEPTION
    ) { blockPos, _ -> checkLog(blockPos) }

    // 木が隣接している場合はコンパイルエラー
    if ((mainStemResult + externalStemsResult).sumBy { if (it.tag) 1 else 0 } > 1) throw MultipleFairyBoxException()

    return Leaves(leavesResult.flatMap { entry ->
        val rate = (5 - entry.distance) atLeast 0
        (0 until rate).map { entry.blockPos }
    })
}


// Aura Collection Speed

// オーラ吸収量のばらつきを抑えるために入射方向自体を重み付きランダムで抽選するようにする
private val directions = listOf(
    (0 until 1).map { Vec3i(-1, 1, -1) },
    (0 until 4).map { Vec3i(-1, 1, 0) },
    (0 until 1).map { Vec3i(-1, 1, 1) },
    (0 until 2).map { Vec3i(0, 1, -1) },
    (0 until 8).map { Vec3i(0, 1, 0) },
    (0 until 2).map { Vec3i(0, 1, 1) },
    (0 until 1).map { Vec3i(1, 1, -1) },
    (0 until 4).map { Vec3i(1, 1, 0) },
    (0 until 1).map { Vec3i(1, 1, 1) }
).flatten()

private fun getLightLevel(world: World, blockPosLeaves: BlockPos, direction: Vec3i, lightLevelCapacity: Int): Int {
    var blockPos = blockPosLeaves + direction
    var lightLevel = lightLevelCapacity
    while (true) {
        lightLevel -= when {
            blockPos.y >= world.height -> return lightLevel // その座標が天空ならば、ループを抜ける
            !world.isBlockLoaded(blockPos) -> 0 // その座標がロードされていないならば、遮蔽度は0
            else -> world.getBlockLightOpacity(blockPos) // そうでない場合、遮蔽度はそのブロック固有の値
        }
        if (lightLevel <= 0) return 0
        blockPos += direction
    }
}

fun getAuraCollectionSpeed(world: World, leaves: Leaves, times: Int) = (0 until times).map {
    if (leaves.blockPosList.isEmpty()) return 0.0
    val blockPosLeaf = leaves.blockPosList[world.rand.nextInt(leaves.blockPosList.size)]
    val direction = directions[world.rand.nextInt(directions.size)]
    getLightLevel(world, blockPosLeaf, direction, 4) // 4 .. 0
}.average() * leaves.blockPosList.size / 4.0 /* lightLevelは4で100%の効率になるため */ / 4.0 /* leaves.blockPosListは効率100%のときに4重に登録されるため */

/** 葉の数が`24+24+9+9`であるバニラの小さい木が持つオーラ吸収速度 */
const val smallTreeAuraCollectionSpeed = 30.0
