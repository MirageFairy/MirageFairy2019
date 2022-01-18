// TODO change
package miragefairy2019.mod.lib

import java.util.Optional
import java.util.Random
import java.util.function.BiPredicate

// TODO Optional -> Nullable
// TODO flat
object WeightedRandom {
    // TODO receiver
    fun <T : Any> getRandomItem(random: Random, items: List<Item<T>>): Optional<T> = getItem(random.nextDouble(), items)

    /** @param d 0以上1未満の値 */
    // TODO receiver
    fun <T : Any> getItem(d: Double, items: List<Item<T>>): Optional<T> {
        if (items.isEmpty()) return Optional.empty()
        return Optional.of(getRandomItem(items, d * getTotalWeight(items)))
    }

    // TODO receiver
    fun getTotalWeight(items: List<Item<*>>) = items.sumByDouble { it.weight }

    // TODO receiver
    private fun <T : Any> getRandomItem(items: List<Item<T>>, w: Double): T {
        var w2 = w
        items.forEach { item ->
            w2 -= item.weight
            if (w2 < 0) return item.item
        }
        return items.last().item
    }

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
