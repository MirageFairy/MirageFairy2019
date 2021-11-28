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

val moduleManualRepair: Module = {
    onAddRecipe {
        GameRegistry.findRegistry(IRecipe::class.java).register(RecipeManualRepair())
    }
}

class RecipeManualRepair : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        setRegistryName(ResourceLocation(ModMirageFairy2019.MODID, "manual_repair"))
    }

    private class MatchResult(val itemTarget: IManualRepairableItem, val itemStackTarget: ItemStack)

    private fun match(inventoryCrafting: InventoryCrafting): MatchResult? {
        val used = BooleanArray(inventoryCrafting.sizeInventory)

        fun <T : Any> pull(predicate: (ItemStack) -> T?): T? = run a@{
            (0 until inventoryCrafting.sizeInventory).forEach { i ->
                if (!used[i]) {
                    val itemStack: ItemStack = inventoryCrafting.getStackInSlot(i)
                    val result = predicate(itemStack)
                    if (result != null) {
                        used[i] = true
                        return@a result
                    }
                }
            }
            return null
        }

        // 妖精武器探索
        val (itemStackTarget, itemTarget) = pull { itemStack ->
            val item: Item = itemStack.item
            if (item is IManualRepairableItem && item.canManualRepair(itemStack)) Pair(itemStack, item as IManualRepairableItem) else null
        } ?: return null

        // スフィア探索
        val ingredientsSubstitutes = itemTarget.getManualRepairSubstitute(itemStackTarget)
        if (ingredientsSubstitutes.size == 0) return null
        ingredientsSubstitutes.forEach { ingredient -> pull { if (ingredient.test(it)) true else null } ?: return null }

        // 余りがあってはならない
        (0 until inventoryCrafting.sizeInventory).forEach {
            if (!used[it] && !inventoryCrafting.getStackInSlot(it).isEmpty) return null
        }

        return MatchResult(itemTarget, itemStackTarget)
    }

    override fun matches(inventory: InventoryCrafting, world: World) = match(inventory) != null
    override fun getRecipeOutput(): ItemStack = EMPTY
    override fun isDynamic() = true
    override fun canFit(width: Int, height: Int) = width * height >= 1
    override fun getCraftingResult(inventory: InventoryCrafting): ItemStack = match(inventory)?.let { it.itemTarget.getManualRepairedItem(it.itemStackTarget) } ?: EMPTY
    override fun getRemainingItems(inventory: InventoryCrafting): NonNullList<ItemStack> = match(inventory)?.let {
        (0 until inventory.sizeInventory)
            .map { ForgeHooks.getContainerItem(inventory.getStackInSlot(it)) }
            .toCollection(NonNullList.create())
    } ?: NonNullList.create()
}
