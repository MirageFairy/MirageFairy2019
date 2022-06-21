package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Mana
import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.region
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod.fairyweapon.deprecated.negative
import miragefairy2019.mod.fairyweapon.deprecated.positive
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.skill.EnumMastery
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

class ItemRyugyoDrill(
    additionalBaseStatus: Double
) : ItemMiragiumToolBase(
    Mana.GAIA,
    EnumMastery.mining,
    additionalBaseStatus
) {
    val maxHardness = status("maxHardness", { 2.0 + !strength.magicStatus * 0.02 }) { float2.positive }.setRange(2.0..20.0).setVisibility(EnumVisibility.DETAIL)
    val range = status("range", { (1 + !extent.magicStatus * 0.01).toInt() }) { integer.positive }.setRange(1..5).setVisibility(EnumVisibility.DETAIL)
    val coolTime = status("coolTime", { cost * 2.0 }) { duration.negative }.setVisibility(EnumVisibility.DETAIL)

    init {
        setHarvestLevel("pickaxe", 3)
        setHarvestLevel("shovel", 3)
        destroySpeed = 8.0f
    }

    override fun iterateTargets(magicScope: MagicScope, blockPosBase: BlockPos) = iterator {
        magicScope.run {
            blockPosBase.region.grow(!range, !range, !range).positions.sortedByDistance(blockPosBase).forEach { blockPos ->
                if (canBreak(magicScope, blockPos)) yield(blockPos)
            }
        }
    }

    override fun isEffective(itemStack: ItemStack, blockState: IBlockState) = super.isEffective(itemStack, blockState) || when {
        blockState.block === Blocks.SNOW_LAYER -> true
        blockState.material === Material.IRON -> true
        blockState.material === Material.ANVIL -> true
        blockState.material === Material.ROCK -> true
        blockState.material === Material.SNOW -> true
        else -> false
    }

    override fun canBreak(magicScope: MagicScope, blockPos: BlockPos) = super.canBreak(magicScope, blockPos)
        && magicScope.run { world.getBlockState(blockPos).getBlockHardness(world, blockPos) <= !maxHardness } // 硬すぎてはいけない

    override fun getCoolTime(magicScope: MagicScope) = magicScope.run { !coolTime }
}
