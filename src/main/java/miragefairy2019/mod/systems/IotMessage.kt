package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.send
import miragefairy2019.libkt.setOrRemove
import net.minecraftforge.common.DimensionManager
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
                onIotMessage(event.senderName, event.message)
            }

            fun onIotMessage(playerName: String, message: String) {
                val daemons = DaemonManager.daemons ?: return

                // TODO
                // 本体ブロックが現存しないデーモンを除去する
                val onFinish = mutableListOf<() -> Unit>()
                daemons.forEach { (dimensionalPos, daemon) ->
                    val isInvalid = run invalidDaemon@{
                        val world = DimensionManager.getWorld(dimensionalPos.dimension) ?: return@invalidDaemon false // ディメンションがロードされていない
                        if (!world.isBlockLoaded(dimensionalPos.pos)) return@invalidDaemon false // チャンクがロードされていない
                        val block = world.getBlockState(dimensionalPos.pos).block as? IDaemonBlock ?: return@invalidDaemon true // ブロックがおかしい
                        if (!block.supportsDaemon(world, dimensionalPos.pos, daemon)) return@invalidDaemon true // このデーモンをサポートしていない
                        false // 正常
                    }
                    if (isInvalid) {
                        onFinish += { daemons.setOrRemove(dimensionalPos, null) }
                        return@forEach
                    }
                }
                onFinish.forEach { it() }

                // すべての監視デーモンに対して処理
                daemons.forEach { (_, daemon) ->
                    if (daemon !is IIotMessageDaemon) return@forEach
                    daemon.onIotMessage(playerName, message)
                }

            }
        })
    }
}

class IotGlobalMessageEvent(val senderName: String, val message: String) : Event()

interface IIotMessageDaemon {
    fun onIotMessage(senderName: String, message: String)
}
