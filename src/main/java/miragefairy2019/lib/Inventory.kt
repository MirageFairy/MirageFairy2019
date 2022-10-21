package miragefairy2019.lib

import miragefairy2019.libkt.copy
import miragefairy2019.libkt.equalsItemDamageTag
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraftforge.items.IItemHandlerModifiable

// Merge

/**
 * スロットAからスロットBにアイテムを移す
 * @return アイテムが完全にマージされたかどうか
 */
fun merge(srcInventory: IInventory, srcSlotIndex: Int, destInventory: IInventory, destSlotIndex: Int): Boolean {
    when {
        srcInventory[srcSlotIndex].isEmpty -> return true // 元から空なので何もする必要はない
        !destInventory.isItemValidForSlot(destSlotIndex, srcInventory[srcSlotIndex]) -> return false // 宛先にこの種類のアイテムが入らない
        destInventory[destSlotIndex].isEmpty || srcInventory[srcSlotIndex] equalsItemDamageTag destInventory[destSlotIndex] -> {
            // 先が空もしくは元と同じ種類のアイテムが入っている場合、マージ

            // 個数計算
            val count = srcInventory[srcSlotIndex].count + destInventory[destSlotIndex].count
            val destCount = count atMost destInventory.inventoryStackLimit atMost srcInventory[srcSlotIndex].maxStackSize

            // 移動処理
            destInventory[destSlotIndex] = srcInventory[srcSlotIndex].copy(destCount)
            srcInventory[srcSlotIndex] = srcInventory[srcSlotIndex].copy(count - destCount)

            return srcInventory[srcSlotIndex].isEmpty
        }
        else -> return false // 宛先に別のアイテムが入っているので何もできない
    }
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


// Move

fun move(power: Int, srcItemHandler: IItemHandlerModifiable, destItemHandler: IItemHandlerModifiable): List<ItemStack> {
    val movedItemStacks = mutableListOf<ItemStack>()
    var remainingPower = power

    run finish@{
        srcItemHandler.indices.forEach nextSrcSlot@{ srcIndex ->

            if (remainingPower <= 0) return@finish // もうパワーがない
            // まだパワーが残っている

            val virtualSrcItemStack = srcItemHandler.extractItem(srcIndex, 64, true).copy() // srcから搬出可能なアイテム
            val originalSrcItemStack = virtualSrcItemStack.copy()

            if (virtualSrcItemStack.isEmpty) return@nextSrcSlot // 空のsrcスロットは無視
            // srcスロットに何かが入っている

            // 成立

            var movedCount = 0
            try {
                val notEmptyDestIndices = mutableListOf<Int>()
                val emptyDestIndices = mutableListOf<Int>()
                destItemHandler.indices.forEach { destIndex ->
                    val destItemStack = destItemHandler[destIndex]
                    if (destItemStack.isEmpty) {
                        emptyDestIndices += destIndex
                    } else if (destItemStack equalsItemDamageTag virtualSrcItemStack) {
                        notEmptyDestIndices += destIndex
                    }
                }
                (notEmptyDestIndices + emptyDestIndices).forEach nextDestSlot@{ destIndex ->

                    if (virtualSrcItemStack.isEmpty) return@nextSrcSlot // すべて移動し終えた
                    // まだ移動するものが残っている

                    if (!destItemHandler.isItemValid(destIndex, virtualSrcItemStack)) return@nextDestSlot // 宛先がこのアイテムを受け付けない
                    // 宛先がこのアイテムを受け付ける

                    // 移動を試す
                    val remainingSrcItemStack = destItemHandler.insertItem(destIndex, virtualSrcItemStack.copy(), false)
                    if (remainingSrcItemStack.count != virtualSrcItemStack.count) { // 移動が発生した場合
                        movedCount += virtualSrcItemStack.count - remainingSrcItemStack.count
                        virtualSrcItemStack.count = remainingSrcItemStack.count
                    }

                }
            } finally {
                if (movedCount > 0) { // 移動が発生した
                    originalSrcItemStack.count = movedCount
                    movedItemStacks += originalSrcItemStack // 移動したアイテム標本に追加
                    remainingPower-- // パワー消費
                    val removedSrcItemStack = srcItemHandler.extractItem(srcIndex, movedCount, false) // 抽出イベント発火
                    assert(removedSrcItemStack.count == movedCount) { "異常な移動処理が発生しました: ${removedSrcItemStack.count}, $movedCount" }
                }
            }

        }
    }

    return movedItemStacks
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


/**
 * @param itemStack 非破壊的
 * @param count 指定時、itemStackの個数を無視する。最大スタック数を超える値を指定可能
 */
fun EntityPlayer.obtain(itemStack: ItemStack, count: Int = itemStack.count) {
    val maxStackSize = itemStack.item.getItemStackLimit(itemStack)
    var remainingCount = count
    while (remainingCount > 0) {
        val nextCount = remainingCount atMost maxStackSize
        val nextItemStack = itemStack.copy(nextCount)
        if (!this.inventory.addItemStackToInventory(nextItemStack)) {
            this.dropItem(nextItemStack, false)
        }
        remainingCount -= nextCount
    }
}
