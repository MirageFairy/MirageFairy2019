package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.send
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

val iotMessageModule = module {
    onInit {
        MinecraftForge.EVENT_BUS.register(object {
            @Suppress("unused")
            @SubscribeEvent
            fun handle(event: ServerChatEvent) {
                IotGlobalMessageEvent(event.player.name, event.message).send()
            }

            @Suppress("unused")
            @SubscribeEvent
            fun handle(event: IotGlobalMessageEvent) {
                val daemons = DaemonManager.daemons ?: return

                // すべての監視デーモンに対して処理
                daemons.values.toList().forEach { daemon ->
                    if (!daemon.checkBlock()) return@forEach
                    if (daemon !is IIotMessageDaemon) return@forEach
                    daemon.onIotMessage(event.senderName, event.message)
                }

            }
        })
    }
}

class IotGlobalMessageEvent(val senderName: String, val message: String) : Event()

interface IIotMessageDaemon {
    fun onIotMessage(senderName: String, message: String)
}
