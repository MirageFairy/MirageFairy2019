package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.region
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.boolean
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.positive
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import kotlin.math.floor

class ItemMiragiumScythe(private val baseFortune: Double, override var destroySpeed: Float) : ItemMiragiumToolBase() {
    override val maxHardness = status("maxHardness", { 0.0 + !Mana.GAIA / 50.0 + !Erg.SLASH / 25.0 + !Mastery.agriculture / 100.0 / 2.0 atMost 10.0 }, { float2 })
    val range = status("range", { floor(2.0 + !Mana.WIND / 20.0 + !Erg.HARVEST / 20.0).toInt() atMost 5 }, { integer })
    val breakSpeed = status("breakSpeed", { 10.0 * costFactor }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.agriculture / 100.0 }, { boost })
    val fortune = status("fortune", { baseFortune + !Mana.AQUA / 100.0 + !Erg.LIFE / 50.0 }, { float2 })
    val fortuneBoost = status("fortuneBoost", { 1.0 + !Mastery.agriculture / 100.0 }, { boost })
    override val actualFortune: FormulaArguments.() -> Double get() = { fortune(this) * fortuneBoost(this) }
    override val wear = status("wear", { 0.1 / (1.0 + !Mana.FIRE / 20.0 + !Erg.SENSE / 10.0) * costFactor }, { percent2 })
    override val collection = status("collection", { !Erg.WARP >= 10.0 }, { boolean.positive })
    override val shearing = status("shearing", { !Erg.HARVEST >= 10.0 }, { boolean.positive })

    override fun iterateTargets(magicArguments: MagicArguments, blockPosBase: BlockPos) = iterator {
        magicArguments.run {
            blockPosBase.region.grow(range(), 1, range()).positions.sortedByDistance(blockPosBase).forEach { blockPos ->
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

    override fun getActualCoolTimePerBlock(magicArguments: MagicArguments) = magicArguments.run { 20.0 / (breakSpeed() * speedBoost()) }

    override fun canBreak(magicArguments: MagicArguments, blockPos: BlockPos) = super.canBreak(magicArguments, blockPos)
        && !magicArguments.world.getBlockState(blockPos).isNormalCube // 普通のキューブであってはならない
}
