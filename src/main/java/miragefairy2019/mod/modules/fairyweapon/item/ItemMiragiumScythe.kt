package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.range
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.MagicScope
import miragefairy2019.mod3.magic.negative
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.skill.EnumMastery
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemMiragiumScythe(
    additionalBaseStatus: Double,
    override var destroySpeed: Float
) : ItemMiragiumToolBase(
    EnumManaType.GAIA,
    EnumMastery.harvest,
    additionalBaseStatus
) {
    val maxHardness = "maxHardness"({ double2.positive }) { !strength * 0.01 }.setRange(0.0..10.0).setVisibility(Companion.EnumVisibility.DETAIL)
    val range = "range"({ int.positive }) { (2 + !extent * 0.02).toInt() }.setRange(2..5).setVisibility(Companion.EnumVisibility.DETAIL)
    val coolTime = "coolTime"({ tick.negative }) { cost * 0.3 }.setVisibility(Companion.EnumVisibility.DETAIL)

    override fun MagicScope.getTargets(itemStack: ItemStack, world: World, blockPosBase: BlockPos) = blockPosBase.range.grow(!range, 0, !range).positions
        .filter { blockPos ->
            val blockState = world.getBlockState(blockPos)
            when {
                blockState.getBlockHardness(world, blockPos) > !maxHardness -> false
                blockState.isNormalCube -> false
                else -> true
            }
        }
        .sortedByDistance(blockPosBase)

    override fun isEffective(itemStack: ItemStack, blockState: IBlockState) = super.isEffective(itemStack, blockState) || when {
        blockState.block === Blocks.WEB -> true
        blockState.material === Material.PLANTS -> true
        blockState.material === Material.VINE -> true
        blockState.material === Material.CORAL -> true
        blockState.material === Material.LEAVES -> true
        blockState.material === Material.GOURD -> true
        blockState.material === Material.GRASS -> true
        blockState.material === Material.CACTUS -> true
        else -> false
    }

    override fun MagicScope.getCoolTime() = !coolTime
}
