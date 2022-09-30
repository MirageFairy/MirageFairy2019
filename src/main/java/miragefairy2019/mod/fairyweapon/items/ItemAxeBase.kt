package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.lib.NeighborhoodType
import miragefairy2019.lib.TooLargeBehaviour
import miragefairy2019.lib.blockPoses
import miragefairy2019.lib.treeSearch
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.world
import mirrg.kotlin.hydrogen.toUnitOrNull
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

open class ItemAxeBase(private val baseRange: Double) : ItemMiragiumToolBase() {
    init {
        setHarvestLevel("axe", 2) // 鉄相当
        destroySpeed = 6.0f // 鉄相当
    }

    override fun isEffective(itemStack: ItemStack, blockState: IBlockState) = when {
        super.isEffective(itemStack, blockState) -> true
        isLog(blockState) -> true
        isLeaves(blockState) -> true
        else -> false
    }

    private fun isLog(blockState: IBlockState) = blockState.isNormalCube && blockState.block.isToolEffective("axe", blockState)
    private fun isLeaves(blockState: IBlockState) = !blockState.isNormalCube && blockState.material === Material.LEAVES

    open fun getRange(a: FormulaArguments) = baseRange.toInt()

    override fun getTargetBlockPoses(a: MagicArguments, baseBlockPos: BlockPos) = iterator {

        // 幹を探索
        val stemResult = treeSearch(
            a.world,
            listOf(baseBlockPos),
            mutableSetOf(),
            maxDistance = getRange(a),
            maxSize = 1000,
            includeZero = true,
            neighborhoodType = NeighborhoodType.VERTEX,
            tooLargeBehaviour = TooLargeBehaviour.IGNORE
        ) { it, _ -> isLog(a.world.getBlockState(it)).toUnitOrNull() }

        // 幹に6隣接で8マスまで接続する葉を壊せる
        val leavesResult = treeSearch(
            a.world,
            stemResult.blockPoses,
            stemResult.blockPoses.toMutableSet(),
            maxDistance = 8,
            maxSize = 2000,
            tooLargeBehaviour = TooLargeBehaviour.IGNORE
        ) { it, _ -> isLeaves(a.world.getBlockState(it)).toUnitOrNull() }

        (leavesResult.reversed() + stemResult.reversed()).blockPoses.forEach { blockPos ->
            yield(blockPos)
        }

    }

    override fun getCoolTimeCategories() = listOf("axe")
}
