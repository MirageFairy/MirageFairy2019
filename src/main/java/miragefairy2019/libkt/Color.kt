package miragefairy2019.libkt

import kotlin.math.roundToInt

interface IRgb {
    val rgb: Int
    val r: Int
    val g: Int
    val b: Int
    val rf: Float
    val gf: Float
    val bf: Float
}

interface IArgb {
    val argb: Int
    val a: Int
    val r: Int
    val g: Int
    val b: Int
    val af: Float
    val rf: Float
    val gf: Float
    val bf: Float
}

fun rgb(rgb: Int) = object : IRgb {
    override val rgb = rgb
    override val r by lazy { rgb shr 16 and 0xFF }
    override val g by lazy { rgb shr 8 and 0xFF }
    override val b by lazy { rgb and 0xFF }
    override val rf by lazy { r / 255f }
    override val gf by lazy { g / 255f }
    override val bf by lazy { b / 255f }
}

fun rgb(rgb: Long) = rgb(rgb.toInt())

fun argb(argb: Int) = object : IArgb {
    override val argb = argb
    override val a by lazy { argb shr 24 and 0xFF }
    override val r by lazy { argb shr 16 and 0xFF }
    override val g by lazy { argb shr 8 and 0xFF }
    override val b by lazy { argb and 0xFF }
    override val af by lazy { a / 255f }
    override val rf by lazy { r / 255f }
    override val gf by lazy { g / 255f }
    override val bf by lazy { b / 255f }
}

fun argb(argb: Long) = argb(argb.toInt())

fun rgb(r: Int, g: Int, b: Int) = object : IRgb {
    override val rgb by lazy { (r.coerceIn(0, 255) shl 16) or (g.coerceIn(0, 255) shl 8) or b.coerceIn(0, 255) }
    override val r = r
    override val g = g
    override val b = b
    override val rf by lazy { r / 255f }
    override val gf by lazy { g / 255f }
    override val bf by lazy { b / 255f }
}

fun argb(a: Int, r: Int, g: Int, b: Int) = object : IArgb {
    override val argb by lazy { (a.coerceIn(0, 255) shl 24) or (r.coerceIn(0, 255) shl 16) or (g.coerceIn(0, 255) shl 8) or b.coerceIn(0, 255) }
    override val a = a
    override val r = r
    override val g = g
    override val b = b
    override val af by lazy { a / 255f }
    override val rf by lazy { r / 255f }
    override val gf by lazy { g / 255f }
    override val bf by lazy { b / 255f }
}

fun rgb(rf: Float, gf: Float, bf: Float) = object : IRgb {
    override val r by lazy { (rf * 255f).roundToInt() }
    override val g by lazy { (gf * 255f).roundToInt() }
    override val b by lazy { (bf * 255f).roundToInt() }
    override val rgb by lazy { (r.coerceIn(0, 255) shl 16) or (g.coerceIn(0, 255) shl 8) or b.coerceIn(0, 255) }
    override val rf = rf
    override val gf = gf
    override val bf = bf
}

fun argb(af: Float, rf: Float, gf: Float, bf: Float) = object : IArgb {
    override val a by lazy { (af * 255f).roundToInt() }
    override val r by lazy { (rf * 255f).roundToInt() }
    override val g by lazy { (gf * 255f).roundToInt() }
    override val b by lazy { (bf * 255f).roundToInt() }
    override val argb by lazy { (a.coerceIn(0, 255) shl 24) or (r.coerceIn(0, 255) shl 16) or (g.coerceIn(0, 255) shl 8) or b.coerceIn(0, 255) }
    override val af = af
    override val rf = rf
    override val gf = gf
    override val bf = bf
}


fun Int.toRgb() = rgb(this)
fun Int.toArgb() = argb(this)
fun Long.toRgb() = rgb(this)
fun Long.toArgb() = argb(this)
fun IArgb.toRgb() = rgb(argb)
fun IRgb.toArgb(a: Int) = argb(a, r, g, b)
operator fun IRgb.times(x: Float) = rgb(rf * x, gf * x, bf * x)
operator fun IArgb.times(x: Float) = argb(af, rf * x, gf * x, bf * x)
