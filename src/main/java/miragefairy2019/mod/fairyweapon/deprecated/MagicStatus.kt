package miragefairy2019.mod.fairyweapon.deprecated

import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer

interface IMagicStatus<T> {
    val name: String
    val formula: Formula<T>
    val renderer: FormulaRenderer<T>
}
