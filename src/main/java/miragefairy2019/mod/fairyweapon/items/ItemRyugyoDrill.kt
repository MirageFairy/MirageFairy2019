package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.region
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float0
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

class ItemRyugyoDrill(private val additionalBaseStatus: Double) : ItemMiragiumToolBase() {
    override val strength = status("strength", { (additionalBaseStatus + !Erg.SLASH + !Mastery.mining * 0.5) * costFactor + !Mana.GAIA }, { float0 })
    override val extent = status("extent", { (additionalBaseStatus + !Erg.SHOOT) * costFactor + !Mana.WIND * 2 }, { float0 })
    override val endurance = status("endurance", { (additionalBaseStatus + !Erg.SENSE) * costFactor + !Mana.FIRE + !Mana.AQUA }, { float0 })
    override val production = status("production", { (additionalBaseStatus + !Erg.HARVEST) * costFactor + !Mana.SHINE + !Mana.DARK }, { float0 })
    val maxHardness = status("maxHardness", { 2.0 + !strength * 0.02 }, { float2 }) { setRange(2.0..20.0).setVisibility(EnumVisibility.DETAIL) }
    val range = status("range", { (1 + !extent * 0.01).toInt() }, { integer }) { setRange(1..5).setVisibility(EnumVisibility.DETAIL) }
    val coolTime = status("coolTime", { 100.0 * costFactor }, { duration }) { setVisibility(EnumVisibility.DETAIL) }

    init {
        setHarvestLevel("pickaxe", 3)
        setHarvestLevel("shovel", 3)
        destroySpeed = 8.0f
    }

    override fun iterateTargets(magicArguments: MagicArguments, blockPosBase: BlockPos) = iterator {
        magicArguments.run {
            blockPosBase.region.grow(range(), range(), range()).positions.sortedByDistance(blockPosBase).forEach { blockPos ->
                if (canBreak(magicArguments, blockPos)) yield(blockPos)
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

    override fun canBreak(magicArguments: MagicArguments, blockPos: BlockPos) = super.canBreak(magicArguments, blockPos)
        && magicArguments.run { world.getBlockState(blockPos).getBlockHardness(world, blockPos) <= maxHardness() } // 硬すぎてはいけない

    override fun getCoolTime(magicArguments: MagicArguments) = magicArguments.run { coolTime() }
}
