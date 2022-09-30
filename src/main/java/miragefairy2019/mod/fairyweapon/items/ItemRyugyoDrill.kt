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
import net.minecraft.world.World
import kotlin.math.floor

class ItemRyugyoDrill(baseBreakStonesPerTick: Double, private val baseMaxHardness: Double) : ItemMiragiumToolBase() {
    init {
        setHarvestLevel("pickaxe", 3)
        setHarvestLevel("shovel", 3)
        destroySpeed = 8.0f
    }

    override fun focusSurface() = true

    override fun isEffective(itemStack: ItemStack, blockState: IBlockState) = when {
        super.isEffective(itemStack, blockState) -> true
        blockState.block === Blocks.SNOW_LAYER -> true
        blockState.material === Material.IRON -> true
        blockState.material === Material.ANVIL -> true
        blockState.material === Material.ROCK -> true
        blockState.material === Material.SNOW -> true
        else -> false
    }

    val maxHardness = status("maxHardness", { baseMaxHardness }, { float2 })
    override fun canBreak(a: MagicArguments, blockPos: BlockPos) = when {
        !super.canBreak(a, blockPos) -> false
        a.world.getBlockState(blockPos).getBlockHardness(a.world, blockPos) > maxHardness(a) -> false // 硬すぎてはいけない
        else -> true
    }

    val range = status("range", { floor(1.0 + !Mana.WIND / 50.0 + !Erg.LEVITATE / 25.0).toInt() atMost 5 }, { integer })
    override fun getTargetBlockPoses(a: MagicArguments, baseBlockPos: BlockPos) = iterator {
        baseBlockPos.region.grow(range(a), range(a), range(a)).positions.sortedByDistance(baseBlockPos).forEach { blockPos ->
            yield(blockPos)
        }
    }

    val wear = status("wear", { 0.04 / (1.0 + !Mana.FIRE / 50.0 + !Erg.LIFE / 25.0) * costFactor }, { percent2 })
    override fun getDurabilityCost(a: FormulaArguments, world: World, blockPos: BlockPos, blockState: IBlockState) = wear(a)

    val breakSpeed = status("breakSpeed", { baseBreakStonesPerTick * 20.0/* tick/sec */ * 2.0/* hardness */ * costFactor }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.mining / 100.0 }, { boost })
    override fun getBreakSpeed(a: MagicArguments) = breakSpeed(a) * speedBoost(a)

    override fun getCoolTimeCategories() = listOf("pickaxe")

    val fortune = status("fortune", { !Mana.SHINE / 50.0 + !Mana.DARK / 100.0 + !Erg.THUNDER / 50.0 }, { float2 })
    val fortuneBoost = status("fortuneBoost", { 1.0 + !Mastery.mining / 100.0 }, { boost })
    override fun getFortune(a: FormulaArguments) = fortune(a) * fortuneBoost(a)

    val silkTouch = status("silkTouch", { !Erg.WATER >= 10.0 }, { boolean.positive })
    override fun isSilkTouch(a: FormulaArguments) = silkTouch(a)

    val collection = status("collection", { !Erg.WARP >= 10.0 }, { boolean.positive })
    override fun doCollection(a: FormulaArguments) = collection(a)
}
