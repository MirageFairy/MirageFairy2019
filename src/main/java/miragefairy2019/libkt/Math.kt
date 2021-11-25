package miragefairy2019.libkt

import mirrg.boron.util.UtilsMath
import java.util.Random

fun Int.squared() = this * this
fun Double.squared() = this * this
fun Random.randomInt(d: Double) = UtilsMath.randomInt(this, d)
