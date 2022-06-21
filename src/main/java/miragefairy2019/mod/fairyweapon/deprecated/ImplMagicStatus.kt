package miragefairy2019.mod.fairyweapon.deprecated

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyType
import miragefairy2019.api.Mana
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.displayName
import miragefairy2019.lib.get
import miragefairy2019.libkt.bold
import miragefairy2019.libkt.darkPurple
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.withColor
import miragefairy2019.mod.skill.IMastery
import miragefairy2019.mod.skill.displayName
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TextFormatting.GREEN
import net.minecraft.util.text.TextFormatting.RED

class MagicStatus<T>(
    private val name: String,
    private val function: Formula<T>,
    private val formatter: FormulaRenderer<T>
) : IMagicStatus<T> {
    override fun getName() = name
    override fun getFormula() = function
    override fun getRenderer() = formatter
}

data class MagicStatusFunctionArguments(
    private val getSkillLevel: (IMastery) -> Int,
    private val fairyType: IFairyType
) : FormulaArguments {
    override fun getSkillLevel(mastery: IMastery) = getSkillLevel.invoke(mastery)
    override fun getCost() = fairyType.cost
    override fun getManaValue(mana: Mana) = fairyType.manaSet[mana]
    override fun getErgValue(erg: Erg) = fairyType.ergSet[erg]
}


val <T> IMagicStatus<T>.displayName get() = textComponent { translate("mirageFairy2019.magic.status.$name.name") }

fun <T> IMagicStatus<T>.getDisplayValue(arguments: FormulaArguments): ITextComponent = renderer.render(arguments, formula)

val <T> Formula<T>.defaultValue: T get() = calculate(MagicStatusFunctionArguments({ 0 }, EMPTY_FAIRY))

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

            override fun getCost() = add(textComponent { translate("mirageFairy2019.formula.source.cost.name").darkPurple }) // TODO 色変更
            override fun getManaValue(mana: Mana) = add(mana.displayName)
            override fun getErgValue(erg: Erg) = add(erg.displayName)
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

fun <T : Comparable<T>> IMagicStatus<T>.ranged(min: T, max: T) = object : IMagicStatus<T> {
    override fun getName() = this@ranged.name
    override fun getFormula() = Formula { this@ranged.formula.calculate(it).coerceIn(min, max) }
    override fun getRenderer() = FormulaRenderer<T> { arguments, function ->
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
}
