package miragefairy2019.lib

import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.size
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack

class RecipeInput<T : Any>(val index: Int, val itemStack: ItemStack, val tag: T, private val consumer: () -> Unit) {
    fun consume() = consumer()
}

class RecipeMatcher(private val inventoryCrafting: InventoryCrafting) {
    private val used = Array(inventoryCrafting.size) { false }

    fun <T : Any> findIndexed(tagSupplier: (index: Int, itemStack: ItemStack) -> T?): RecipeInput<T>? {
        inventoryCrafting.itemStacks.forEachIndexed next@{ index, itemStack ->
            if (used[index]) return@next // 使用済みのスロットならスルー
            val tag = tagSupplier(index, itemStack) ?: return@next // マッチしないならスルー
            return RecipeInput(index, itemStack, tag) { used[index] = true } // マッチしたのでそれを返す
        }
        return null // 1個もマッチしなかった
    }

    fun <T : Any> pullIndexed(tagSupplier: (index: Int, itemStack: ItemStack) -> T?): RecipeInput<T>? {
        val recipeInput = findIndexed(tagSupplier) ?: return null // マッチしなかったので何もしない
        recipeInput.consume() // マッチしたので消費
        return recipeInput
    }

    fun <T : Any> find(tagSupplier: (ItemStack) -> T?) = findIndexed { _, itemStack -> tagSupplier(itemStack) }
    fun <T : Any> pull(tagSupplier: (ItemStack) -> T?) = pullIndexed { _, itemStack -> tagSupplier(itemStack) }
    fun findMatched(predicate: (ItemStack) -> Boolean) = find { if (predicate(it)) Unit else null }
    fun pullMatched(predicate: (ItemStack) -> Boolean) = pull { if (predicate(it)) Unit else null }
    fun hasRemaining() = findMatched { !it.isEmpty } != null
}
