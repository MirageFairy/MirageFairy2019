package miragefairy2019.libkt

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class Complex(val re: Float, val im: Float) {
    companion object {
        fun ofPolar(abs: Float, arg: Float) = Complex(abs * cos(arg), abs * sin(arg))
        fun ofPolar(abs: Double, arg: Double) = Complex(abs * cos(arg), abs * sin(arg))
    }

    constructor(re: Double, im: Double) : this(re.toFloat(), im.toFloat())

    val rei = re.roundToInt()
    val imi = im.roundToInt()
    val red = re.toDouble()
    val imd = im.toDouble()
}

operator fun Complex.plus(o: Complex) = Complex(re + o.re, im + o.im)
operator fun Complex.minus(o: Complex) = Complex(re - o.re, im - o.im)
operator fun Complex.times(o: Complex) = Complex(re * o.re - im * o.im, im * o.re + re * o.im)
operator fun Complex.div(o: Complex): Complex {
    val e = o.re * o.re + o.im * o.im
    return Complex((re * o.re + im * o.im) / e, (im * o.re - re * o.im) / e)
}

// operator fun Complex.plus(o: Float) = Complex(re + o, im)
// operator fun Complex.plus(o: Int) = Complex(re + o, im)
// fun Complex.plus(re2: Float, im2: Float) = Complex(re + re2, im + im2)
// fun Complex.plus(re2: Int, im2: Int) = Complex(re + re2, im + im2)
// operator fun Complex.minus(o: Float) = Complex(re - o, im)
// operator fun Complex.minus(o: Int) = Complex(re - o, im)
// operator fun Complex.times(o: Float) = Complex(re * o, im * o)
// operator fun Complex.times(o: Int) = Complex(re * o, im * o)
