package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.darkRed
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod3.artifacts.treecompile.TreeCompileException
import miragefairy2019.mod3.artifacts.treecompile.treeSearch
import net.minecraft.block.BlockLeaves
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import java.util.Random
import kotlin.math.floor
import kotlin.math.log

// Compile

class MultipleFairyBoxException : TreeCompileException(textComponent { (!"妖精の木が接触しています").darkRed }) // TODO translate

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

@Throws(TreeCompileException::class)
fun compileFairyTree(world: World, originBlockPos: BlockPos): Leaves {

    // 基点
    val originResult = listOf(Pair(originBlockPos, world.getBlockState(originBlockPos).block))

    // 基点幹
    val originalStemResult = originResult + treeSearch(world, originResult.map { it.first }, maxSize = 100) { world2, blockPos, _ ->
        when (val block = world2.getBlockState(blockPos).block) {
            is BlockFairyWoodLog -> Pair(blockPos, block)
            is BlockFairyBoxBase -> Pair(blockPos, block)
            else -> null
        }
    }

    // 基点幹の葉
    val leavesResult = treeSearch(world, originalStemResult.map { it.first }, maxSize = 1000) { world2, blockPos, distance ->
        when (world2.getBlockState(blockPos).block) {
            is BlockLeaves -> Pair(blockPos, (5 - distance).coerceAtLeast(0))
            else -> null
        }
    }

    // 基点木
    val originalTreeResult = originalStemResult + leavesResult

    // 基点木に隣接している幹
    val extraStemResult = treeSearch(world, originalTreeResult.map { it.first }, maxSize = 100) { world2, blockPos, _ ->
        when (val block = world2.getBlockState(blockPos).block) {
            is BlockFairyWoodLog -> Pair(blockPos, block)
            is BlockFairyBoxBase -> Pair(blockPos, block)
            else -> null
        }
    }

    // 木が隣接している場合はコンパイルエラー
    if ((originalStemResult + extraStemResult).filter { it.second is BlockFairyBoxBase }.size > 1) throw MultipleFairyBoxException()

    return Leaves(leavesResult.map { (0 until it.second).map { _ -> it.first } }.flatten())
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


// TileEntity

abstract class TileEntityFairyBoxBase : TileEntity(), ITickable {
    private var tick = -1
    override fun update() {
        if (world.isRemote) return // サーバーワールドのみ

        // 平均して1分に1回行動する
        val interval = 20 * 60
        if (tick < 0) tick = randomSkipTicks(world.rand, 1 / interval.toDouble())
        if (tick != 0) {
            tick--
            return
        } else {
            tick = randomSkipTicks(world.rand, 1 / interval.toDouble())
        }

        executor?.onUpdateTick()
    }


    abstract val executor: TileEntityExecutor?
}

open class TileEntityExecutor {
    open fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = false
    open fun onUpdateTick() = Unit
}

/** @return 0以上の値 */
fun randomSkipTicks(random: Random, rate: Double) = floor(log(random.nextDouble(), 1 - rate)).toInt()
