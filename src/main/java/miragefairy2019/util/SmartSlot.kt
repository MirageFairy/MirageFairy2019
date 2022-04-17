package miragefairy2019.util

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class SmartSlot(inventory: IInventory, index: Int, xPosition: Int, yPosition: Int) : Slot(inventory, index, xPosition, yPosition) {
    override fun isItemValid(itemStack: ItemStack) = inventory.isItemValidForSlot(slotIndex, itemStack)
}
