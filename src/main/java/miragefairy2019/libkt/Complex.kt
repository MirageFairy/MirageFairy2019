package miragefairy2019.libkt

data class Complex(val re: Float, val im: Float)

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
