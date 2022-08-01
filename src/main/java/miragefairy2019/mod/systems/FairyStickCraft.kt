package miragefairy2019.mod.systems

import miragefairy2019.api.IFairyStickCraftItem
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item

val fairyStickCraftModule = module {

    // TODO シンプル化
    // 初期化
    onInstantiation {
        ApiFairyStickCraft.fairyStickCraftRegistry = FairyStickCraftRegistry()
    }

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
