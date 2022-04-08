package miragefairy2019.mod.fairyweapon.magic4

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.mod.skill.IMastery
import net.minecraft.util.text.ITextComponent

interface FormulaArguments {
    val hasPartnerFairy: Boolean
    fun getRawMana(mana: Mana): Double
    fun getRawErg(erg: Erg): Double
    val cost: Double
    val color: Int
    fun getSkillLevel(mastery: IMastery): Int
}

interface Formula<out T> {
    fun calculate(formulaArguments: FormulaArguments): T
}

interface FormulaRenderer<in T> {
    fun render(formulaArguments: FormulaArguments, formula: Formula<T>): ITextComponent
}

class MagicStatus<T>(val name: String, val formula: Formula<T>, val renderer: FormulaRenderer<T>) : (FormulaArguments) -> T {
    override fun invoke(arguments: FormulaArguments) = formula.calculate(arguments)
}
