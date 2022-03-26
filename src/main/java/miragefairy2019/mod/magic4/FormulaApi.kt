package miragefairy2019.mod.magic4

import miragefairy2019.api.Mana
import miragefairy2019.api.Erg
import miragefairy2019.mod3.skill.api.IMastery
import net.minecraft.util.text.ITextComponent

interface FormulaArguments {
    val hasPartnerFairy: Boolean
    fun getRawMana(mana: Mana): Double
    fun getRawErg(ergType: Erg): Double
    val cost: Double
    val color: Int
    fun getSkillLevel(mastery: IMastery): Int
}

interface Formula<T> {
    fun calculate(formulaArguments: FormulaArguments): T
}

interface FormulaRenderer<T> {
    fun render(formulaArguments: FormulaArguments, formula: Formula<T>): ITextComponent
}

class MagicStatus<T>(val name: String, val formula: Formula<T>, val renderer: FormulaRenderer<T>) : (FormulaArguments) -> T {
    override fun invoke(arguments: FormulaArguments) = formula.calculate(arguments)
}
