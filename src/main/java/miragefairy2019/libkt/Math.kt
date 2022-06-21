package miragefairy2019.libkt

import java.util.Random
import kotlin.math.floor

fun Int.sq() = this * this
fun Long.sq() = this * this
fun Float.sq() = this * this
fun Double.sq() = this * this

/** 期待値がdになるように整数の乱数を生成します。 */
fun Random.randomInt(d: Double): Int {
    val i = floor(d).toInt()
    val mod = d - i
    return if (this.nextDouble() < mod) i + 1 else i
}
