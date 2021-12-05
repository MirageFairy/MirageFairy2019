package miragefairy2019.libkt

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList

operator fun IInventory.get(index: Int): ItemStack = getStackInSlot(index)
operator fun IInventory.set(index: Int, itemStack: ItemStack) = setInventorySlotContents(index, itemStack)

val IInventory.itemStacks get() = (0 until sizeInventory).map { this[it] }

fun IInventory.readFromNBT(nbt: NBTTagCompound) {
    val list = NonNullList.withSize(sizeInventory, ItemStack.EMPTY)
    ItemStackHelper.loadAllItems(nbt, list)
    (0 until sizeInventory).map { i -> this[i] = list[i] }
}

fun IInventory.writeToNBT(nbt: NBTTagCompound) {
    val list = NonNullList.withSize(sizeInventory, ItemStack.EMPTY)
    (0 until sizeInventory).map { i -> list[i] = this[i] }
    ItemStackHelper.saveAllItems(nbt, list)
}