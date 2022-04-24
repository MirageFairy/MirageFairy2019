package miragefairy2019.lib

import miragefairy2019.libkt.equalsItemDamageTag
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

// Transfer ItemStacks

enum class MergeResult(val isChanged: Boolean, val isCompleted: Boolean) {
    FAILURE(false, false),
    UNNECESSARY(false, true),
    REMAINING(true, false),
    COMPLETED(true, true),
}

/**
 * 既にアイテムが半端に格納されたスロットに対してマージを試みます。
 * [destSlot]内のItemStackはコピーされないまま改変されます。
 */
fun mergeIntoOccupiedSlot(mutableSrcItemStack: ItemStack, destSlot: Slot): MergeResult {
    if (mutableSrcItemStack.isEmpty) return MergeResult.UNNECESSARY // 移動の必要はない
    val mutableDestItemStack = destSlot.stack
    if (mutableDestItemStack.isEmpty) return MergeResult.FAILURE // 宛先が空なので対象ではない
    if (!(mutableDestItemStack equalsItemDamageTag mutableSrcItemStack)) return MergeResult.FAILURE // 宛先のアイテムが異なる
    val maxCount = mutableSrcItemStack.maxStackSize atMost destSlot.slotStackLimit
    val acceptableCount = maxCount - mutableDestItemStack.count
    if (acceptableCount <= 0) return MergeResult.FAILURE // 宛先に空きがない

    // 成立

    val moveCount = mutableSrcItemStack.count atMost acceptableCount

    mutableSrcItemStack.count -= moveCount
    mutableDestItemStack.count += moveCount

    destSlot.onSlotChanged()

    return if (mutableSrcItemStack.isEmpty) MergeResult.COMPLETED else MergeResult.REMAINING // アイテムの移動が完了
}

/**
 * 空のスロットに対して移動を試みます。
 * [destSlot]内のItemStackはコピーされないまま改変されます。
 */
fun moveIntoEmptySlot(mutableSrcItemStack: ItemStack, destSlot: Slot): MergeResult {
    if (mutableSrcItemStack.isEmpty) return MergeResult.UNNECESSARY // 移動の必要はない
    val mutableDestItemStack = destSlot.stack
    if (!mutableDestItemStack.isEmpty) return MergeResult.FAILURE // 宛先が空でないので対象ではない
    if (!destSlot.isItemValid(mutableSrcItemStack)) return MergeResult.FAILURE // 宛先のスロットがこのアイテムを受け付けない

    // 成立

    val moveCount = mutableSrcItemStack.count atMost destSlot.slotStackLimit

    val stack = mutableSrcItemStack.splitStack(moveCount)
    destSlot.putStack(stack)

    destSlot.onSlotChanged()

    return if (mutableSrcItemStack.isEmpty) MergeResult.COMPLETED else MergeResult.REMAINING // アイテムの移動が完了
}

fun mergeItemStack(mutableSrcItemStack: ItemStack, destSlots: Iterable<Slot>): MergeResult {

    if (mutableSrcItemStack.isEmpty) return MergeResult.UNNECESSARY // 移動の必要はない

    var isChanged = false

    // 既に半端に格納されているスロットに優先的に搬入する
    if (mutableSrcItemStack.isStackable) {
        destSlots.forEach { destSlot ->
            val moveResult = mergeIntoOccupiedSlot(mutableSrcItemStack, destSlot)
            if (moveResult.isChanged) isChanged = true
            if (moveResult.isCompleted) return MergeResult.COMPLETED
        }
    }

    // 空のスロットに搬入する
    destSlots.forEach { destSlot ->
        val moveResult = moveIntoEmptySlot(mutableSrcItemStack, destSlot)
        if (moveResult.isChanged) return if (isChanged) MergeResult.REMAINING else MergeResult.FAILURE // 空のスロットに搬入する場合は一度に1スロットにしか搬入をしない
        if (moveResult.isCompleted) return MergeResult.COMPLETED
    }

    return if (isChanged) MergeResult.REMAINING else MergeResult.FAILURE
}
