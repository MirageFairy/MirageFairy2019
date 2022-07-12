package miragefairy2019.mod.fairyweapon.magic4

import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.green
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.withColor
import miragefairy2019.mod.fairyweapon.CriticalRate
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.util.text.ITextComponent
import kotlin.math.log

class FormulaRendererScope<out T>(val formulaArguments: FormulaArguments, val formula: Formula<T>) {
    val value by lazy { formula.calculate(formulaArguments) }
}

@Suppress("unused")
private fun <T> FormulaRendererSelector<T>.createSimpleFormulaRenderer(function: FormulaRendererScope<T>.() -> ITextComponent) = FormulaRenderer<T> { formulaArguments, formula ->
    FormulaRendererScope(formulaArguments, formula).function()
}

val FormulaRendererSelector<String>.string get() = createSimpleFormulaRenderer { textComponent { value() } }
val FormulaRendererSelector<Int>.integer get() = createSimpleFormulaRenderer { textComponent { "$value"() } }
val FormulaRendererSelector<Double>.duration get() = createSimpleFormulaRenderer { textComponent { (value / 20.0 formatAs "%.2f 秒")() } } // TODO translate
val FormulaRendererSelector<Double>.pitch get() = createSimpleFormulaRenderer { textComponent { (log(value, 2.0) * 12 formatAs "%.2f")() } } // TODO translate
val FormulaRendererSelector<Double>.float0 get() = createSimpleFormulaRenderer { textComponent { (value formatAs "%.0f")() } }
val FormulaRendererSelector<Double>.float1 get() = createSimpleFormulaRenderer { textComponent { (value formatAs "%.1f")() } }
val FormulaRendererSelector<Double>.float2 get() = createSimpleFormulaRenderer { textComponent { (value formatAs "%.2f")() } }
val FormulaRendererSelector<Double>.float3 get() = createSimpleFormulaRenderer { textComponent { (value formatAs "%.3f")() } }
val FormulaRendererSelector<Double>.percent0 get() = createSimpleFormulaRenderer { textComponent { (value * 100.0 formatAs "%.0f%%")() } }
val FormulaRendererSelector<Double>.percent1 get() = createSimpleFormulaRenderer { textComponent { (value * 100.0 formatAs "%.1f%%")() } }
val FormulaRendererSelector<Double>.percent2 get() = createSimpleFormulaRenderer { textComponent { (value * 100.0 formatAs "%.2f%%")() } }
val FormulaRendererSelector<Double>.percent3 get() = createSimpleFormulaRenderer { textComponent { (value * 100.0 formatAs "%.3f%%")() } }
val FormulaRendererSelector<Double>.boost get() = createSimpleFormulaRenderer { textComponent { ((value - 1) * 100.0 formatAs "%+.0f%%")() } }
val FormulaRendererSelector<Boolean>.boolean get() = createSimpleFormulaRenderer { textComponent { if (value) "Yes"() else "No"() } }
val FormulaRendererSelector<CriticalRate>.criticalRate get() = createSimpleFormulaRenderer { textComponent { value.bar.map { "|"().withColor(it.color) }.flatten() + (value.mean formatAs " (%.2f)")() } }

fun <T> FormulaRenderer<T>.map(function: FormulaRendererScope<T>.(ITextComponent) -> ITextComponent): FormulaRenderer<T> = FormulaRenderer { formulaArguments, formula ->
    FormulaRendererScope(formulaArguments, formula).function(this@map.render(formulaArguments, formula))
}

fun <T> FormulaRenderer<T>.prefix(string: String) = map { textComponent { string() + it() } }
fun <T> FormulaRenderer<T>.suffix(string: String) = map { textComponent { it() + string() } }

val FormulaRenderer<Boolean>.positive get() = map { textComponent { if (value) it().green else it().red } }
val FormulaRenderer<Boolean>.negative get() = map { textComponent { if (value) it().red else it().green } }
