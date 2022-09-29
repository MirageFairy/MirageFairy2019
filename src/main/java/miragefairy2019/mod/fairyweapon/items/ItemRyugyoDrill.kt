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
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.floor

class ItemRyugyoDrill(baseBreakStonesPerTick: Double) : ItemMiragiumToolBase() {
    override val maxHardness = status("maxHardness", { 2.0 + !Mana.GAIA / 50.0 + !Erg.DESTROY / 25.0 + !Mastery.mining / 25.0 atMost 20.0 }, { float2 })
    val range = status("range", { floor(1.0 + !Mana.WIND / 50.0 + !Erg.LEVITATE / 25.0).toInt() atMost 5 }, { integer })
    val breakSpeed = status("breakSpeed", { baseBreakStonesPerTick * 20.0/* tick/sec */ * 2.0/* hardness */ * costFactor }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.mining / 100.0 }, { boost })
    val fortune = status("fortune", { !Mana.SHINE / 50.0 + !Mana.DARK / 100.0 + !Erg.THUNDER / 50.0 }, { float2 })
    val fortuneBoost = status("fortuneBoost", { 1.0 + !Mastery.mining / 100.0 }, { boost })
    override val actualFortune: FormulaArguments.() -> Double get() = { fortune(this) * fortuneBoost(this) }

    val wear = status("wear", { 0.04 / (1.0 + !Mana.FIRE / 50.0 + !Erg.LIFE / 25.0) * costFactor }, { percent2 })
    override fun getDurabilityCost(formulaArguments: FormulaArguments, world: World, blockPos: BlockPos, blockState: IBlockState) = wear(formulaArguments)

    val silkTouch = status("silkTouch", { !Erg.WATER >= 10.0 }, { boolean.positive })
    override fun isSilkTouch(formulaArguments: FormulaArguments) = silkTouch(formulaArguments)

    val collection = status("collection", { !Erg.WARP >= 10.0 }, { boolean.positive })
    override fun doCollection(formulaArguments: FormulaArguments) = collection(formulaArguments)

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

    override fun getActualCoolTimePerBlock(magicArguments: MagicArguments) = magicArguments.run { 20.0 / (breakSpeed() * speedBoost()) }
}
