package miragefairy2019.lib.gui

import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class SlotResult(private val player: EntityPlayer, inventory: IInventory, slotIndex: Int, x: Int, y: Int) : Slot(inventory, slotIndex, x, y) {

    override fun isItemValid(stack: ItemStack) = false


    // craft

    private var removeCount = 0

    override fun decrStackSize(amount: Int): ItemStack {
        if (hasStack) removeCount += amount atMost stack.count
        return super.decrStackSize(amount)
    }

    override fun onTake(player: EntityPlayer, itemStack: ItemStack): ItemStack {
        onCrafting(itemStack)
        super.onTake(player, itemStack)
        return itemStack
    }

    override fun onCrafting(itemStack: ItemStack, amount: Int) {
        removeCount += amount
        onCrafting(itemStack)
    }

    override fun onCrafting(itemStack: ItemStack) {
        itemStack.onCrafting(player.world, player, removeCount)
        removeCount = 0
    }

}
