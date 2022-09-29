package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
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
import mirrg.kotlin.hydrogen.floorToInt
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemMiragiumAxe(private val baseSpeed: Double, baseRange: Double, private val baseFortune: Double) : ItemAxeBase(baseRange) {
    init {
        setHarvestLevel("axe", 2) // 鉄相当
        destroySpeed = 8.0f * baseSpeed.toFloat() // ダイヤ相当
    }

    val additionalReach = status("additionalReach", { 0.0 + !Mana.WIND / 20.0 + !Erg.LEVITATE / 10.0 atMost 30.0 }, { float2 })
    override fun getAdditionalReach(a: MagicArguments) = additionalReach(a)

    val range = status("range", { (baseRange + !Mana.GAIA / 10.0 + !Erg.DESTROY / 5.0).floorToInt() atMost 50 }, { integer })
    override fun getRange(a: FormulaArguments) = range(a)

    val wear = status("wear", { 0.1 / (1.0 + !Mana.FIRE / 20.0 + !Erg.SLASH / 10.0) * costFactor }, { percent2 })
    override fun getDurabilityCost(a: FormulaArguments, world: World, blockPos: BlockPos, blockState: IBlockState) = wear(a)

    val breakSpeed = status("breakSpeed", { baseSpeed * 2.0/* 原木の硬度 */ * 1.0/* 基準秒間破壊個数 */ * (1.0 + !Mana.AQUA / 20.0 + !Erg.HARVEST / 10.0) * costFactor }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.lumbering / 100.0 }, { boost })
    override fun getBreakSpeed(a: MagicArguments) = breakSpeed(a) * speedBoost(a)

    val fortune = status("fortune", { baseFortune + !Mana.SHINE / 100.0 + !Erg.LIFE / 50.0 }, { float2 })
    val fortuneBoost = status("fortuneBoost", { 1.0 + !Mastery.lumbering / 100.0 }, { boost })
    override fun getFortune(a: FormulaArguments) = fortune(a) * fortuneBoost(a)

    val silkTouch = status("silkTouch", { !Erg.WATER >= 10.0 }, { boolean.positive })
    override fun isSilkTouch(a: FormulaArguments) = silkTouch(a)

    val collection = status("collection", { !Erg.WARP >= 10.0 }, { boolean.positive })
    override fun doCollection(a: FormulaArguments) = collection(a)
}
