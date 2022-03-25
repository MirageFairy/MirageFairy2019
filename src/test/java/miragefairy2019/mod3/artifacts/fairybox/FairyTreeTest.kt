package miragefairy2019.mod3.artifacts.fairybox

import java.util.Random

fun main() {
    var min = 9999
    var max = 0
    var sum = 0L
    var count = 0

    repeat(10000000) {
        val interval = randomSkipTicks(Random(), 1 / 200.0) + 1
        if (interval < min) min = interval
        if (interval > max) max = interval
        sum += interval
        count++
    }

    println("$min <- 1であればいい")
    println("$max <- 3000くらいになるっぽい（いくらでもいい）")
    println("$sum <- 2000000000くらい")
    println("$count <- 10000000固定")
    println("${sum / count.toDouble()} <- 非常に200に近い")
}
