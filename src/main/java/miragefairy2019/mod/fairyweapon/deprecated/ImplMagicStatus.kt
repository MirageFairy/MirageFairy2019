package miragefairy2019.mod.fairyweapon.deprecated

import miragefairy2019.libkt.bold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer
import miragefairy2019.mod.fairyweapon.magic4.MagicStatus

fun <T : Comparable<T>> MagicStatus<T>.ranged(min: T, max: T) = MagicStatus(
    name,
    Formula { formula.calculate(it).coerceIn(min, max) },
    FormulaRenderer { arguments, function ->
        val value = function.calculate(arguments)
        val displayValue = renderer.render(arguments, function)
        when (value) {
            min -> textComponent { displayValue().bold }
            max -> textComponent { displayValue().bold }
            else -> displayValue
        }
    },
    visibility
)
