package miragefairy2019.mod3.manualrepair.api

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient

interface IManualRepairableItem {
    fun canManualRepair(itemStack: ItemStack): Boolean
    fun getManualRepairIngredients(itemStack: ItemStack): List<Ingredient>
    fun getManualRepairedItem(itemStack: ItemStack): ItemStack
}
