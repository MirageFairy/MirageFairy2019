package miragefairy2019.libkt

import miragefairy2019.lib.get
import miragefairy2019.lib.int
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.tags
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList

// IInventory Integrations

val IInventory.size get() = sizeInventory
operator fun IInventory.get(index: Int): ItemStack = getStackInSlot(index)
operator fun IInventory.set(index: Int, itemStack: ItemStack) = setInventorySlotContents(index, itemStack)

val IInventory.indices get() = 0 until size
val IInventory.itemStacks get() = indices.map { this[it] }

fun IInventory.readFromNBT(nbt: NBTTagCompound) {
    val list = NonNullList.withSize(size, ItemStack.EMPTY)
    ItemStackHelper.loadAllItems(nbt, list)
    indices.map { i -> this[i] = list[i] }
}

fun IInventory.writeToNBT(nbt: NBTTagCompound) {
    val list = NonNullList.withSize(size, ItemStack.EMPTY)
    indices.map { i -> list[i] = this[i] }
    ItemStackHelper.saveAllItems(nbt, list)
}


// NBTTagCompound Integrations

val NBTTagCompound.inventorySlotCount: Int
    get() {
        val tags = this.nbtProvider["Items"].tags ?: return 0 // Itemsがリスト出ない場合は0
        val maxSlotIndex = tags.mapNotNull { it["Slot"].int }.max() ?: return 0 // Slotに数値を持つタグが1個もなかった場合は0
        return maxSlotIndex + 1
    }

fun <I : IInventory> NBTTagCompound.readInventory(inventoryCreator: (size: Int) -> I): I {
    val inventory = inventoryCreator(this.inventorySlotCount)
    inventory.readFromNBT(this)
    return inventory
}
