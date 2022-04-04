package miragefairy2019.libkt

import java.util.Random

class WeightedItem<T : Any>(val item: T, val weight: Double)

fun <T : Any> List<WeightedItem<T>>.getRandomItem(random: Random) = getItem(random.nextDouble())

/** @param d 0以上1未満の値 */
fun <T : Any> List<WeightedItem<T>>.getItem(d: Double): T? {
    if (isEmpty()) return null

    var w = d * totalWeight // 0 <= w < totalWeight
    this.forEach { item ->
        w -= item.weight
        if (w < 0) return item.item
    }
    return this.last().item
}

val <T : Any> List<WeightedItem<T>>.totalWeight get() = sumByDouble { it.weight }

/** 同一キーのエントリの重みを加算することによってキーをユニークにします。 */
fun <T : Any> List<WeightedItem<T>>.unique(equals: (T, T) -> Boolean): List<WeightedItem<T>> {
    class Slot(val item: T) {
        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (this === other) return true // 相手が自分自身なら一致
            if (other == null) return false // 相手が無なら不一致

            // 型チェック
            if (javaClass != other.javaClass) return false
            @Suppress("UNCHECKED_CAST")
            other as Slot

            return equals(item, other.item)
        }
    }

    val map = mutableMapOf<Slot, Double>()
    this.forEach { item ->
        map[Slot(item.item)] = (map[Slot(item.item)] ?: 0.0) + item.weight
    }
    return map.entries.map { WeightedItem(it.key.item, it.value) }
}
