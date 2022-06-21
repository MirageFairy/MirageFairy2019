package miragefairy2019.mod.fairyweapon.deprecated

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyType
import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.PlayerProxy
import miragefairy2019.lib.div
import miragefairy2019.lib.erg
import miragefairy2019.lib.get
import miragefairy2019.lib.playerAuraHandler
import miragefairy2019.lib.plus
import miragefairy2019.lib.skillContainer
import miragefairy2019.lib.times
import miragefairy2019.libkt.bold
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.withColor
import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer
import miragefairy2019.mod.fairyweapon.magic4.MagicStatus
import miragefairy2019.mod.skill.EnumMastery
import miragefairy2019.mod.skill.IMastery
import miragefairy2019.mod.skill.getSkillLevel
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TextFormatting.GREEN
import net.minecraft.util.text.TextFormatting.RED

data class MagicStatusFunctionArguments(
    private val playerProxy: PlayerProxy?,
    private val getSkillLevel: (IMastery) -> Int,
    private val fairyType: IFairyType
) : FormulaArguments {
    override val hasPartnerFairy: Boolean get() = !fairyType.isEmpty
    override fun getSkillLevel(mastery: IMastery) = getSkillLevel.invoke(mastery)
    override val cost get() = fairyType.cost
    override val color get() = fairyType.color
    override fun getOldMana(mana: Mana) = fairyType.manaSet[mana]
    override fun getRawMana(mana: Mana): Double {
        val a = fairyType.manaSet / (cost / 50.0) // パートナー妖精のマナ
        val b = a + (playerProxy?.playerAuraHandler?.playerAura ?: ManaSet.ZERO) // プレイヤーオーラの加算
        val c = b * (1.0 + 0.005 * (playerProxy?.skillContainer?.getSkillLevel(EnumMastery.root) ?: 0)) // スキルレベル補正：妖精マスタリ1につき1%増加
        return c[mana]
    }

    override fun getRawErg(erg: Erg) = fairyType.erg(erg)
}

fun <T : Comparable<T>> MagicStatus<T>.ranged(min: T, max: T) = MagicStatus(
    name = this@ranged.name,
    formula = Formula { this@ranged.formula.calculate(it).coerceIn(min, max) },
    renderer = FormulaRenderer<T> { arguments, function ->
        val value = function.calculate(arguments)
        val displayValue = this@ranged.renderer.render(arguments, function)
        when (value) {
            min -> textComponent { displayValue().bold }
            max -> textComponent { displayValue().bold }
            else -> displayValue
        }
    }
)
