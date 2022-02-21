package miragefairy2019.mod3.manualrepair

import miragefairy2019.libkt.Module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.manualrepair.api.IManualRepairableItem
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemStack.EMPTY
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

object ManualRepair {
    val module: Module = {
        onAddRecipe {
            GameRegistry.findRegistry(IRecipe::class.java).register(RecipeManualRepair())
        }
    }
}

class RecipeManualRepair : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        registryName = ResourceLocation(ModMirageFairy2019.MODID, "manual_repair")
    }

    private class MatchResult(val itemTarget: IManualRepairableItem, val itemStackTarget: ItemStack, val slotIndexTarget: Int)

    private fun match(inventoryCrafting: InventoryCrafting): MatchResult? {
        val used = BooleanArray(inventoryCrafting.sizeInventory)

        fun <T : Any> pull(predicate: (index: Int, ItemStack) -> T?): T? = run a@{
            (0 until inventoryCrafting.sizeInventory).forEach { i ->
                if (!used[i]) {
                    val itemStack: ItemStack = inventoryCrafting.getStackInSlot(i)
                    val result = predicate(i, itemStack)
                    if (result != null) {
                        used[i] = true
                        return@a result
                    }
                }
            }
            return null
        }

        // 妖精武器探索
        val matchResult = pull { i, itemStack ->
            val item: Item = itemStack.item
            if (item is IManualRepairableItem && item.canManualRepair(itemStack)) MatchResult(item, itemStack, i) else null
        } ?: return null

        // スフィア探索
        val ingredientsSubstitutes = matchResult.itemTarget.getManualRepairIngredients(matchResult.itemStackTarget)
        if (ingredientsSubstitutes.size == 0) return null
        ingredientsSubstitutes.forEach { ingredient -> pull { _, it -> if (ingredient.test(it)) true else null } ?: return null }

        // 余りがあってはならない
        (0 until inventoryCrafting.sizeInventory).forEach {
            if (!used[it] && !inventoryCrafting.getStackInSlot(it).isEmpty) return null
        }

        return matchResult
    }

    override fun matches(inventory: InventoryCrafting, world: World) = match(inventory) != null
    override fun getRecipeOutput(): ItemStack = EMPTY
    override fun isDynamic() = true
    override fun canFit(width: Int, height: Int) = width * height >= 1
    override fun getCraftingResult(inventory: InventoryCrafting): ItemStack = match(inventory)?.let { it.itemTarget.getManualRepairedItem(it.itemStackTarget) } ?: EMPTY
    override fun getRemainingItems(inventory: InventoryCrafting): NonNullList<ItemStack> = match(inventory)?.let { result ->
        (0 until inventory.sizeInventory)
            .map { i -> if (i == result.slotIndexTarget) EMPTY else ForgeHooks.getContainerItem(inventory.getStackInSlot(i)) }
            .toCollection(NonNullList.create())
    } ?: NonNullList.create()
}
