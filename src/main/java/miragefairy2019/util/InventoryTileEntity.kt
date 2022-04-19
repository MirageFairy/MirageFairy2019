package miragefairy2019.util

import miragefairy2019.libkt.unit
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryBasic
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity

open class InventoryTileEntity<out T : TileEntity>(val tileEntity: T, title: String, customName: Boolean, slotCount: Int) : InventoryBasic(title, customName, slotCount) {
    init {
        addInventoryChangeListener { tileEntity.markDirty() }
    }

    override fun isUsableByPlayer(player: EntityPlayer): Boolean {
        if (tileEntity.world.getTileEntity(tileEntity.pos) != tileEntity) return false
        return player.getDistanceSq(tileEntity.pos.x.toDouble() + 0.5, tileEntity.pos.y.toDouble() + 0.5, tileEntity.pos.z.toDouble() + 0.5) <= 64.0
    }


    var filter: (ItemStack) -> Boolean = { true }
    override fun isItemValidForSlot(index: Int, itemStack: ItemStack) = filter(itemStack)


    private var inventoryStackLimit = 64
    fun setInventoryStackLimit(inventoryStackLimit: Int) = unit { this.inventoryStackLimit = inventoryStackLimit }
    override fun getInventoryStackLimit() = inventoryStackLimit
}
