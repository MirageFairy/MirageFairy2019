package miragefairy2019.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryBasic
import net.minecraft.tileentity.TileEntity

open class InventoryTileEntity<out T : TileEntity>(val tileEntity: T, title: String, customName: Boolean, slotCount: Int) : InventoryBasic(title, customName, slotCount) {
    init {
        addInventoryChangeListener { tileEntity.markDirty() }
    }

    override fun isUsableByPlayer(player: EntityPlayer): Boolean {
        if (tileEntity.world.getTileEntity(tileEntity.pos) != tileEntity) return false
        return player.getDistanceSq(tileEntity.pos.x.toDouble() + 0.5, tileEntity.pos.y.toDouble() + 0.5, tileEntity.pos.z.toDouble() + 0.5) <= 64.0
    }
}
