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

class ItemMiragiumScythe(private val baseFortune: Double, override var destroySpeed: Float) : ItemMiragiumToolBase() {
    override fun focusSurface() = true

    override val maxHardness = status("maxHardness", { 0.0 + !Mana.GAIA / 50.0 + !Erg.SLASH / 25.0 + !Mastery.agriculture / 100.0 / 2.0 atMost 10.0 }, { float2 })
    override fun isEffective(itemStack: ItemStack, blockState: IBlockState) = when {
        super.isEffective(itemStack, blockState) -> true
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

    override fun canBreak(a: MagicArguments, blockPos: BlockPos) = when {
        !super.canBreak(a, blockPos) -> false
        a.world.getBlockState(blockPos).isNormalCube -> false // 普通のキューブであってはならない
        else -> true
    }

    val range = status("range", { floor(2.0 + !Mana.WIND / 20.0 + !Erg.HARVEST / 20.0).toInt() atMost 5 }, { integer })
    override fun getTargetBlockPoses(a: MagicArguments, baseBlockPos: BlockPos) = iterator {
        baseBlockPos.region.grow(range(a), 1, range(a)).positions.sortedByDistance(baseBlockPos).forEach { blockPos ->
            yield(blockPos)
        }
    }

    val wear = status("wear", { 0.1 / (1.0 + !Mana.FIRE / 20.0 + !Erg.SENSE / 10.0) * costFactor }, { percent2 })
    override fun getDurabilityCost(a: FormulaArguments, world: World, blockPos: BlockPos, blockState: IBlockState) = wear(a)

    val breakSpeed = status("breakSpeed", { 10.0 * costFactor }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.agriculture / 100.0 }, { boost })
    override fun getCoolTimePerHardness(a: MagicArguments) = 20.0 / (breakSpeed(a) * speedBoost(a))

    val fortune = status("fortune", { baseFortune + !Mana.AQUA / 100.0 + !Erg.LIFE / 50.0 }, { float2 })
    val fortuneBoost = status("fortuneBoost", { 1.0 + !Mastery.agriculture / 100.0 }, { boost })
    override fun getFortune(a: FormulaArguments) = fortune(a) * fortuneBoost(a)

    val shearing = status("shearing", { !Erg.HARVEST >= 10.0 }, { boolean.positive })
    override fun isShearing(a: FormulaArguments) = shearing(a)

    val collection = status("collection", { !Erg.WARP >= 10.0 }, { boolean.positive })
    override fun doCollection(a: FormulaArguments) = collection(a)
}
