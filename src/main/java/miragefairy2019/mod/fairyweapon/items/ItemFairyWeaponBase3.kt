package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.libkt.bold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer
import miragefairy2019.mod.fairyweapon.magic4.MagicStatusBuilder


// Magic Status

fun <T> MagicStatusBuilder<T>.setVisibility(visibility: EnumVisibility) = apply { this.visibility = visibility }

fun <T : Comparable<T>> MagicStatusBuilder<T>.setRange(range: ClosedRange<T>) = apply {
    val oldFormula = formula
    val oldRenderer = renderer
    formula = Formula { oldFormula.calculate(it).coerceIn(range.start, range.endInclusive) }
    renderer = FormulaRenderer { arguments, function ->
        val value = function.calculate(arguments)
        val displayValue = oldRenderer.render(arguments, function)
        when (value) {
            range.start -> textComponent { displayValue().bold }
            range.endInclusive -> textComponent { displayValue().bold }
            else -> displayValue
        }
    }
}
