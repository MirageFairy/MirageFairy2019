package miragefairy2019.mod.formula4

import miragefairy2019.libkt.green
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.formula4.api.Formula
import miragefairy2019.mod.formula4.api.FormulaArguments
import miragefairy2019.mod.formula4.api.FormulaRenderer
import mirrg.kotlin.formatAs
import net.minecraft.util.text.ITextComponent

class FormulaRendererScope<T>(val formulaArguments: FormulaArguments, val formula: Formula<T>) {
    val value by lazy { formula.calculate(formulaArguments) }
}

@Suppress("unused")
private fun <T> FormulaRendererSelector<T>.createSimpleFormulaRenderer(function: FormulaRendererScope<T>.() -> ITextComponent): FormulaRenderer<T> = object : FormulaRenderer<T> {
    override fun render(formulaArguments: FormulaArguments, formula: Formula<T>) = FormulaRendererScope(formulaArguments, formula).function()
}

private fun <T> FormulaRenderer<T>.map(function: FormulaRendererScope<T>.(ITextComponent) -> ITextComponent): FormulaRenderer<T> = object : FormulaRenderer<T> {
    override fun render(formulaArguments: FormulaArguments, formula: Formula<T>) = FormulaRendererScope(formulaArguments, formula).function(this@map.render(formulaArguments, formula))
}

val FormulaRendererSelector<Int>.integer get() = createSimpleFormulaRenderer { textComponent { !"$value" } }
val FormulaRendererSelector<Double>.float0 get() = createSimpleFormulaRenderer { textComponent { !(value formatAs "%.0f") } }
val FormulaRendererSelector<Double>.float1 get() = createSimpleFormulaRenderer { textComponent { !(value formatAs "%.1f") } }
val FormulaRendererSelector<Double>.float2 get() = createSimpleFormulaRenderer { textComponent { !(value formatAs "%.2f") } }
val FormulaRendererSelector<Double>.float3 get() = createSimpleFormulaRenderer { textComponent { !(value formatAs "%.3f") } }
val FormulaRendererSelector<Double>.percent0 get() = createSimpleFormulaRenderer { textComponent { !(value * 100.0 formatAs "%.0f%%") } }
val FormulaRendererSelector<Double>.percent1 get() = createSimpleFormulaRenderer { textComponent { !(value * 100.0 formatAs "%.1f%%") } }
val FormulaRendererSelector<Double>.percent2 get() = createSimpleFormulaRenderer { textComponent { !(value * 100.0 formatAs "%.2f%%") } }
val FormulaRendererSelector<Double>.percent3 get() = createSimpleFormulaRenderer { textComponent { !(value * 100.0 formatAs "%.3f%%") } }
val FormulaRendererSelector<Boolean>.boolean get() = createSimpleFormulaRenderer { textComponent { if (value) !"Yes" else !"No" } }

val FormulaRenderer<Boolean>.positive get() = map { textComponent { if (value) (!it).green else (!it).red } }
val FormulaRenderer<Boolean>.negative get() = map { textComponent { if (value) (!it).red else (!it).green } }
