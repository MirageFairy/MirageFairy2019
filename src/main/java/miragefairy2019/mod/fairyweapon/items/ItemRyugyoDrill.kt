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
import kotlin.math.floor

class ItemRyugyoDrill() : ItemMiragiumToolBase() {
    val maxHardness = status("maxHardness", { 2.0 + !Mana.GAIA / 50.0 + !Erg.DESTROY / 25.0 + !Mastery.mining / 25.0 atMost 20.0 }, { float2 })
    val range = status("range", { floor(1.0 + !Mana.WIND / 50.0 + !Erg.THUNDER / 25.0).toInt() atMost 5 }, { integer })
    val coolTime = status("coolTime", { 100.0 / costFactor }, { duration })
    override val fortune = status("fortune", { !Mana.SHINE / 15.0 + !Mana.DARK / 30.0 + !Erg.HARVEST / 15.0 }, { float2 })
    override val wear = status("wear", { 25.0 / (1.0 + !Mana.FIRE / 50.0 + !Erg.LEVITATE / 25.0) * costFactor }, { percent2 })

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
