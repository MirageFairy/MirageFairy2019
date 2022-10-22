package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.onServerSave
import miragefairy2019.libkt.DimensionalPos
import miragefairy2019.libkt.existsOrNull
import miragefairy2019.libkt.mkdirsParent
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.ChatWebhookDaemon
import miragefairy2019.mod.artifacts.ChatWebhookDaemonFactory
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.toJson
import mirrg.kotlin.gson.hydrogen.toJsonElement
import mirrg.kotlin.gson.hydrogen.toJsonWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object DaemonSystem {
    private fun getFile(server: MinecraftServer) = server.getWorld(0).saveHandler.worldDirectory.resolve("miragefairy2019/daemon_entities.json")
    val daemonSystemModule = module {

        // セーブデータ読み込みとデーモンマネージャーの初期化
        onServerStarting {
            Main.logger.info("DaemonSystem: Loading")
            val data = getFile(server).existsOrNull?.readText()?.toJsonElement().toJsonWrapper().orNull
            DaemonManager.instance = if (data != null) {
                DaemonManager(
                    // TODO 分離
                    chatWebhook = data["chatWebhook"].asMap().map { (dimensionalPosExpression, daemonData) ->
                        DimensionalPos.parse(dimensionalPosExpression) to ChatWebhookDaemonFactory.fromJson(daemonData)
                    }.toMap().toMutableMap()
                )
            } else {
                DaemonManager(
                    // TODO 分離
                    chatWebhook = mutableMapOf()
                )
            }
        }

        // 保存イベント
        onServerSave {
            Main.logger.info("DaemonSystem: Saving")
            val daemonEntityManager = DaemonManager.instance ?: return@onServerSave
            val server = world.minecraftServer ?: return@onServerSave
            getFile(server).mkdirsParent()
            getFile(server).writeText(
                jsonObject(
                    // TODO 分離
                    "chatWebhook" to daemonEntityManager.chatWebhook.map { (dimensionalPos, daemon) ->
                        dimensionalPos.expression to ChatWebhookDaemonFactory.toJson(daemon)
                    }.jsonObject
                ).toJson { setPrettyPrinting() }
            )
        }

        // サーバーが閉じたときにマネージャーをリセット
        onServerStopping {
            DaemonManager.instance = null
            Main.logger.info("DaemonSystem: Terminated")
        }

    }
}

class DaemonManager(
    val chatWebhook: MutableMap<DimensionalPos, ChatWebhookDaemon> // TODO 分離
) {
    companion object {
        var instance: DaemonManager? = null
    }
}

abstract class Daemon

interface IBlockDaemon {
    fun canSupportDaemon(world: World, blockPos: BlockPos, daemon: Daemon): Boolean
}
