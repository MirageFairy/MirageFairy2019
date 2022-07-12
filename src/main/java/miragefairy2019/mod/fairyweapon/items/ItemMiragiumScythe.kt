package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.region
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

class ItemMiragiumScythe(private val additionalBaseStatus: Double, override var destroySpeed: Float) : ItemMiragiumToolBase() {
    val maxHardness = status("maxHardness", { ((additionalBaseStatus + !Erg.SLASH + !Mastery.harvest * 0.5) * costFactor + !Mana.GAIA) * 0.01 atMost 10.0 }, { float2 })
    val range = status("range", { (2 + ((additionalBaseStatus + !Erg.SHOOT) * costFactor + !Mana.WIND * 2) * 0.02).toInt() atMost 5 }, { integer })
    val coolTime = status("coolTime", { 15.0 * costFactor }, { duration })
    override val fortune = status("fortune", { ((additionalBaseStatus + !Erg.HARVEST) * costFactor + !Mana.SHINE + !Mana.DARK) * 0.03 }, { float2 })
    override val wear = status("wear", { 1 / (25.0 + ((additionalBaseStatus + !Erg.SENSE) * costFactor + !Mana.FIRE + !Mana.AQUA) * 0.25) }, { percent2 })

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
