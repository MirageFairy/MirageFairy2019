package miragefairy2019.mod.systems

import miragefairy2019.api.ILootingDamageSource
import miragefairy2019.libkt.module
import mirrg.kotlin.atLeast
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LootingLevelEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DamageSource {
    val module = module {
        onInit {

            // LootingLevel付きのダメージソースが来たら適用するリスナー登録
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @SubscribeEvent
                fun accept(event: LootingLevelEvent) {
                    val damageSource = event.damageSource as? ILootingDamageSource ?: return
                    event.lootingLevel = event.lootingLevel atLeast damageSource.lootingLevel
                }
            })

        }
    }
}
