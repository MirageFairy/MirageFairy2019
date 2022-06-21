package miragefairy2019.mod.fairyweapon.deprecated

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.mod.skill.IMastery
import net.minecraft.util.text.ITextComponent

interface Formula<T> {
    fun calculate(formulaArguments: FormulaArguments): T
}

fun <T> Formula(block: (formulaArguments: FormulaArguments) -> T) = object : Formula<T> {
    override fun calculate(formulaArguments: FormulaArguments) = block(formulaArguments)
}

interface FormulaArguments {
    fun getSkillLevel(mastery: IMastery): Int
    val cost: Double
    fun getManaValue(mana: Mana): Double
    fun getErgValue(erg: Erg): Double
}

interface FormulaRenderer<T> {
    fun render(formulaArguments: FormulaArguments, formula: Formula<T>): ITextComponent
}

fun <T> FormulaRenderer(block: (formulaArguments: FormulaArguments, formula: Formula<T>) -> ITextComponent) = object : FormulaRenderer<T> {
    override fun render(formulaArguments: FormulaArguments, formula: Formula<T>) = block(formulaArguments, formula)
}

interface IMagicStatus<T> {
    val name: String
    val formula: Formula<T>
    val renderer: FormulaRenderer<T>
}
