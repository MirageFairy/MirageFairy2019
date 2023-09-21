package miragefairy2019.lib

import miragefairy2019.libkt.EMPTY_ITEM_STACK
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.registries.IForgeRegistryEntry

abstract class RecipeBase<R : Any>(registryName: ResourceLocation) : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        this.registryName = registryName
    }

    protected abstract fun match(matcher: RecipeMatcher): R?

    override fun isHidden() = true
    override fun matches(inventory: InventoryCrafting, world: World) = match(RecipeMatcher(inventory)) != null
    override fun getRecipeOutput() = EMPTY_ITEM_STACK

    abstract fun getCraftingResult(result: R): ItemStack
    override fun getCraftingResult(inventory: InventoryCrafting): ItemStack {
        val result = match(RecipeMatcher(inventory)) ?: return EMPTY_ITEM_STACK
        return getCraftingResult(result)
    }

    open fun getRemainingItem(result: R, index: Int, itemStack: ItemStack) = ForgeHooks.getContainerItem(itemStack)
    override fun getRemainingItems(inventory: InventoryCrafting): NonNullList<ItemStack> {
        val result = match(RecipeMatcher(inventory)) ?: return NonNullList.create()
        return inventory.itemStacks.mapIndexed { index, itemStack ->
            getRemainingItem(result, index, itemStack)
        }.toNonNullList()
    }
}

class RecipeInput<out T : Any>(val index: Int, val itemStack: ItemStack, val tag: T, private val consumer: () -> Unit) {
    fun consume() = consumer()
}

class RecipeMatcher(private val inventory: IInventory) {
    private val used = Array(inventory.size) { false }

    fun <T : Any> findIndexed(tagSupplier: (index: Int, itemStack: ItemStack) -> T?): RecipeInput<T>? {
        inventory.itemStacks.forEachIndexed next@{ index, itemStack ->
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
