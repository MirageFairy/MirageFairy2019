package miragefairy2019.mod3.damagesource

import miragefairy2019.libkt.Module
import miragefairy2019.mod3.damagesource.api.IDamageSourceLooting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LootingLevelEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

val moduleDamageSource: Module = {
    onInit {

        // LootingLevel付きのダメージソースが来たら適用するリスナー登録
        MinecraftForge.EVENT_BUS.register(object : Any() {
            @SubscribeEvent
            fun accept(event: LootingLevelEvent) {
                val damageSource = event.damageSource
                if (damageSource is IDamageSourceLooting) {
                    event.lootingLevel = event.lootingLevel.coerceAtLeast(damageSource.lootingLevel)
                }
            }
        })

    }
}
