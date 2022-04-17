package miragefairy2019.lib

import miragefairy2019.libkt.copy
import miragefairy2019.libkt.equalsItemDamageTag
import mirrg.kotlin.atMost
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList

// Merge

/**
 * スロットAからスロットBにアイテムを移す
 * @return アイテムが完全にマージされたかどうか
 */
fun merge(srcInventory: IInventory, srcSlotIndex: Int, destInventory: IInventory, destSlotIndex: Int): Boolean {
    when {
        srcInventory[srcSlotIndex].isEmpty -> Unit // 元が空の場合、何もする必要はない
        destInventory[destSlotIndex].isEmpty || srcInventory[srcSlotIndex] equalsItemDamageTag destInventory[destSlotIndex] -> {
            // 先が空もしくは元と同じ種類のアイテムが入っている場合、マージ
            val count = srcInventory[srcSlotIndex].count + destInventory[destSlotIndex].count
            val destCount = count atMost destInventory.inventoryStackLimit atMost srcInventory[srcSlotIndex].maxStackSize
            destInventory[destSlotIndex] = srcInventory[srcSlotIndex].copy(destCount)
            srcInventory[srcSlotIndex] = srcInventory[srcSlotIndex].copy(count - destCount)
        }
        else -> Unit // 先に別のアイテムが入っている場合、何もできない
    }
    return !srcInventory[srcSlotIndex].isEmpty
}

/**
 * スロットAのアイテムをインベントリBに移す
 * @return アイテムが完全にマージされたかどうか
 */
fun merge(srcInventory: IInventory, srcSlotIndex: Int, destInventory: IInventory, destSlotIndices: Iterable<Int>): Boolean {
    destSlotIndices.forEach { destSlotIndex ->
        if (merge(srcInventory, srcSlotIndex, destInventory, destSlotIndex)) return true
    }
    return false
}

/**
 * インベントリAのアイテムをインベントリBに移す
 * @return すべてのアイテムが完全にマージされたかどうか
 */
fun merge(srcInventory: IInventory, srcSlotIndices: Iterable<Int>, destInventory: IInventory, destSlotIndices: Iterable<Int>): Boolean {
    return srcSlotIndices.all { srcSlotIndex -> merge(srcInventory, srcSlotIndex, destInventory, destSlotIndices) }
}

fun merge(srcInventory: IInventory, destInventory: IInventory): Boolean {
    return merge(srcInventory, srcInventory.indices, destInventory, destInventory.indices)
}


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
