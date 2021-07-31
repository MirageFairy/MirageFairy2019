package miragefairy2019.mod.modules.fairyweapon.damagesource;

import miragefairy2019.mod.api.fairyweapon.damagesource.IDamageSourceLooting;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Loader {

    public static void init(EventRegistryMod erMod) {
        erMod.init.register(e -> {

            // LootingLevel付きのダメージソースが来たら適用するリスナ登録
            MinecraftForge.EVENT_BUS.register(new Object() {
                @SubscribeEvent
                public void accept(LootingLevelEvent event) {
                    if (event.getDamageSource() instanceof IDamageSourceLooting) {
                        event.setLootingLevel(Math.max(event.getLootingLevel(), ((IDamageSourceLooting) event.getDamageSource()).getLootingLevel()));
                    }
                }
            });

        });
    }

}
