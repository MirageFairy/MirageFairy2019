package miragefairy2019.colormaker

import java.awt.Color

class ColorEvaluator {
    private val variables = mutableMapOf<String, () -> Color>()
    fun registerVariable(name: String, colorGetter: () -> Color) = run { variables[name] = colorGetter }
    fun evaluate(colorExpression: ColorExpression) = variables[colorExpression.expression]?.invoke() ?: Color.decode(colorExpression.expression)!!
}

class ColorExpression(val expression: String)
