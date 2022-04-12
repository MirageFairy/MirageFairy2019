package miragefairy2019.libkt

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList

val IInventory.size get() = sizeInventory
operator fun IInventory.get(index: Int): ItemStack = getStackInSlot(index)
operator fun IInventory.set(index: Int, itemStack: ItemStack) = setInventorySlotContents(index, itemStack)

val IInventory.indices get() = 0 until size
val IInventory.itemStacks get() = (0 until size).map { this[it] }

fun IInventory.readFromNBT(nbt: NBTTagCompound) {
    val list = NonNullList.withSize(size, ItemStack.EMPTY)
    ItemStackHelper.loadAllItems(nbt, list)
    (0 until size).map { i -> this[i] = list[i] }
}

fun IInventory.writeToNBT(nbt: NBTTagCompound) {
    val list = NonNullList.withSize(size, ItemStack.EMPTY)
    (0 until size).map { i -> list[i] = this[i] }
    ItemStackHelper.saveAllItems(nbt, list)
}

class SmartSlot(inventory: IInventory, index: Int, xPosition: Int, yPosition: Int) : Slot(inventory, index, xPosition, yPosition) {
    override fun isItemValid(itemStack: ItemStack) = inventory.isItemValidForSlot(slotIndex, itemStack)
}
