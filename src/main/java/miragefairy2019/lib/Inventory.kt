package miragefairy2019.lib

import miragefairy2019.libkt.copy
import miragefairy2019.libkt.equalsItemDamageTag
import miragefairy2019.libkt.get
import miragefairy2019.libkt.indices
import miragefairy2019.libkt.set
import mirrg.kotlin.atMost
import net.minecraft.inventory.IInventory

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
