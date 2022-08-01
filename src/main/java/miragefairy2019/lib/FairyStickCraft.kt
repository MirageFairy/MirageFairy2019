package miragefairy2019.lib

import miragefairy2019.api.IFairyStickCraftItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item

fun addFairyStickCraftCoolTime(player: EntityPlayer, ticks: Int) {
    Item.REGISTRY.forEach { item ->
        if (item is Item && item is IFairyStickCraftItem) {
            if (item.isFairyStickCraftItem) {
                player.cooldownTracker.setCooldown(item, ticks)
            }
        }
    }
}
