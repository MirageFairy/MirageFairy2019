package miragefairy2019.mod.fairyweapon.deprecated

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyType
import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.PlayerProxy
import miragefairy2019.lib.displayName
import miragefairy2019.lib.div
import miragefairy2019.lib.erg
import miragefairy2019.lib.get
import miragefairy2019.lib.playerAuraHandler
import miragefairy2019.lib.plus
import miragefairy2019.lib.skillContainer
import miragefairy2019.lib.times
import miragefairy2019.libkt.bold
import miragefairy2019.libkt.darkPurple
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.withColor
import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer
import miragefairy2019.mod.fairyweapon.magic4.MagicStatus
import miragefairy2019.mod.skill.EnumMastery
import miragefairy2019.mod.skill.IMastery
import miragefairy2019.mod.skill.displayName
import miragefairy2019.mod.skill.getSkillLevel
import net.minecraft.util.text.ITextComponent
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


val <T> MagicStatus<T>.displayName get() = textComponent { translate("mirageFairy2019.magic.status.$name.name") }

fun <T> MagicStatus<T>.getDisplayValue(arguments: FormulaArguments): ITextComponent = renderer.render(arguments, formula)

val <T> Formula<T>.defaultValue: T get() = calculate(MagicStatusFunctionArguments(null, { 0 }, EMPTY_FAIRY))

val <T> Formula<T>.factors
    get(): Iterable<ITextComponent> {
        val factors = mutableListOf<ITextComponent>()
        calculate(object : FormulaArguments {
            override fun getSkillLevel(mastery: IMastery): Int {
                factors.add(textComponent { mastery.displayName().gold })
                return 0
            }

            fun add(textComponent: ITextComponent): Double {
                factors.add(textComponent)
                return 0.0
            }

            override val hasPartnerFairy get() = true
            override val cost get() = add(textComponent { translate("mirageFairy2019.formula.source.cost.name").darkPurple }) // TODO 色変更
            override val color get() = 0x000000
            override fun getRawMana(mana: Mana) = add(mana.displayName)
            override fun getRawErg(erg: Erg) = add(erg.displayName)
        })
        return factors
    }


fun <T : Comparable<T>> FormulaRenderer<T>.coloredBySign(colorPositive: TextFormatting, colorNegative: TextFormatting) = FormulaRenderer<T> { arguments, function ->
    val value = function.calculate(arguments)
    val defaultValue = function.defaultValue
    val displayValue = this@coloredBySign.render(arguments, function)
    when {
        value > defaultValue -> textComponent { displayValue().withColor(colorPositive) }
        value < defaultValue -> textComponent { displayValue().withColor(colorNegative) }
        else -> displayValue
    }
}

val <T : Comparable<T>> FormulaRenderer<T>.positive get() = coloredBySign(GREEN, RED)
val <T : Comparable<T>> FormulaRenderer<T>.negative get() = coloredBySign(RED, GREEN)

fun FormulaRenderer<Boolean>.boolean(isPositive: Boolean) = FormulaRenderer<Boolean> { arguments, function ->
    val value = function.calculate(arguments)
    val displayValue = this@boolean.render(arguments, function)
    textComponent { displayValue().withColor(if (value xor !isPositive) GREEN else RED) }
}

val FormulaRenderer<Boolean>.positiveBoolean get() = boolean(true)
val FormulaRenderer<Boolean>.negativeBoolean get() = boolean(false)

fun <T : Comparable<T>> MagicStatus<T>.ranged(min: T, max: T) = MagicStatus(
    name = this@ranged.name,
    formula = Formula { this@ranged.formula.calculate(it).coerceIn(min, max) },
    renderer = FormulaRenderer<T> { arguments, function ->
        val value = function.calculate(arguments)
        val defaultValue = function.defaultValue
        val displayValue = this@ranged.renderer.render(arguments, function)
        when (value) {
            defaultValue -> displayValue
            min -> textComponent { displayValue().bold }
            max -> textComponent { displayValue().bold }
            else -> displayValue
        }
    }
)
