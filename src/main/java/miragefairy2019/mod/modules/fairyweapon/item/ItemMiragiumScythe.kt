package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.range
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.MagicScope
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.skill.EnumMastery
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemMiragiumScythe(
    additionalBaseStatus: Double,
    breakSpeed: Float
) : ItemMiragiumToolBase(
    EnumManaType.GAIA,
    EnumMastery.harvest,
    additionalBaseStatus,
    breakSpeed
) {
    override fun MagicScope.getTargets(world: World, blockPosBase: BlockPos) = blockPosBase.range.grow(!range, 0, !range).positions
        .filter { blockPos ->
            val blockState = world.getBlockState(blockPos)
            when {
                !isEffective(blockState) -> false
                blockState.getBlockHardness(world, blockPos) > !maxHardness -> false
                blockState.isNormalCube -> false
                else -> true
            }
        }
        .sortedByDistance(blockPosBase)

    override fun isEffective(state: IBlockState) = when {
        state.block === Blocks.WEB -> true
        state.material === Material.PLANTS -> true
        state.material === Material.VINE -> true
        state.material === Material.CORAL -> true
        state.material === Material.LEAVES -> true
        state.material === Material.GOURD -> true
        state.material === Material.GRASS -> true
        state.material === Material.CACTUS -> true
        else -> false
    }
}
