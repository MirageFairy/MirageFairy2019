package miragefairy2019.mod.fairyweapon.magic4

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.mod.skill.IMastery
import net.minecraft.util.text.ITextComponent

interface FormulaArguments {
    val hasPartnerFairy: Boolean
    fun getOldMana(mana: Mana): Double = getRawMana(mana) * (cost / 50.0) // TODO remove
    fun getRawMana(mana: Mana): Double
    fun getRawErg(erg: Erg): Double
    val cost: Double
    val color: Int
    fun getSkillLevel(mastery: IMastery): Int
}

interface Formula<out T> {
    fun calculate(formulaArguments: FormulaArguments): T
}

fun <T> Formula(block: (formulaArguments: FormulaArguments) -> T) = object : Formula<T> {
    override fun calculate(formulaArguments: FormulaArguments) = block(formulaArguments)
}

interface FormulaRenderer<in T> {
    fun render(formulaArguments: FormulaArguments, formula: Formula<T>): ITextComponent
}

fun <T> FormulaRenderer(block: (formulaArguments: FormulaArguments, formula: Formula<T>) -> ITextComponent) = object : FormulaRenderer<T> {
    override fun render(formulaArguments: FormulaArguments, formula: Formula<T>) = block(formulaArguments, formula)
}

enum class EnumVisibility { ALWAYS, DETAIL, NEVER }

class MagicStatus<T>(
    val name: String,
    val formula: Formula<T>,
    val renderer: FormulaRenderer<T>,
    val visibility: EnumVisibility
) : (FormulaArguments) -> T {
    override fun invoke(arguments: FormulaArguments) = formula.calculate(arguments)
}
