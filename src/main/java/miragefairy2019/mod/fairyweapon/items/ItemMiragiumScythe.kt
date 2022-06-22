package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Mana
import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.region
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.skill.Mastery
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

class ItemMiragiumScythe(
    additionalBaseStatus: Double,
    override var destroySpeed: Float
) : ItemMiragiumToolBase(
    Mana.GAIA,
    Mastery.harvest,
    additionalBaseStatus
) {
    val maxHardness = status("maxHardness", { !strength * 0.01 }, { float2 }) { setRange(0.0..10.0).setVisibility(EnumVisibility.DETAIL) }
    val range = status("range", { (2 + !extent * 0.02).toInt() }, { integer }) { setRange(2..5).setVisibility(EnumVisibility.DETAIL) }
    val coolTime = status("coolTime", { cost * 0.3 }, { duration }) { setVisibility(EnumVisibility.DETAIL) }

    override fun iterateTargets(magicArguments: MagicArguments, blockPosBase: BlockPos) = iterator {
        magicArguments.run {
            blockPosBase.region.grow(range(), 0, range()).positions.sortedByDistance(blockPosBase).forEach { blockPos ->
                if (canBreak(magicArguments, blockPos)) yield(blockPos)
            }
        }
    }

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

    override fun canBreak(magicArguments: MagicArguments, blockPos: BlockPos) = super.canBreak(magicArguments, blockPos)
        && magicArguments.run { world.getBlockState(blockPos).getBlockHardness(world, blockPos) <= maxHardness() } // 硬すぎてはいけない
        && !magicArguments.world.getBlockState(blockPos).isNormalCube // 普通のキューブであってはならない

    override fun getCoolTime(magicArguments: MagicArguments) = magicArguments.run { coolTime() }
}
