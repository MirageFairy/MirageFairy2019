package miragefairy2019.mod.systems

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item

interface IFairyStickCraftItem {
    val isFairyStickCraftItem: Boolean
}

fun addFairyStickCraftCoolTime(player: EntityPlayer, ticks: Int) {
    Item.REGISTRY.forEach { item ->
        if (item is Item && item is IFairyStickCraftItem) {
            if (item.isFairyStickCraftItem) {
                player.cooldownTracker.setCooldown(item, ticks)
            }
        }
    }
}
