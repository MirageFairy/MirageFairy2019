// TODO change
package miragefairy2019.mod.lib

import java.util.Random
import java.util.function.BiPredicate

fun <T : Any> List<WeightedRandom.Item<T>>.getRandomItem(random: Random) = getItem(random.nextDouble())

/** @param d 0以上1未満の値 */
fun <T : Any> List<WeightedRandom.Item<T>>.getItem(d: Double): T? {
    if (isEmpty()) return null

    var w = d * WeightedRandom.getTotalWeight(this) // 0 <= w < totalWeight
    this.forEach { item ->
        w -= item.weight
        if (w < 0) return item.item
    }
    return this.last().item
}

// TODO flat
object WeightedRandom {

    // TODO receiver
    fun getTotalWeight(items: List<Item<*>>) = items.sumByDouble { it.weight }

    // TODO -> Pair
    class Item<T : Any>(val item: T, val weight: Double)

    // TODO receiver
    fun <T : Any> unique(dropTable: List<Item<T>>, equals: BiPredicate<T, T>): List<Item<T>> {
        class Slot(val item: T) {
            override fun hashCode() = 0

            override fun equals(other: Any?): Boolean {
                if (this === other) return true // 相手が自分自身なら一致
                if (other == null) return false // 相手が無なら不一致

                // 型チェック
                if (javaClass != other.javaClass) return false
                @Suppress("UNCHECKED_CAST")
                other as Slot

                return equals.test(item, other.item)
            }
        }

        val map = mutableMapOf<Slot, Double>()
        dropTable.forEach { item ->
            map[Slot(item.item)] = (map[Slot(item.item)] ?: 0.0) + item.weight
        }
        return map.entries.map { Item<T>(it.key.item, it.value) }
    }
}
