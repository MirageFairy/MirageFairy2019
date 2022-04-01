package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DimensionalPos
import miragefairy2019.libkt.existsOrNull
import miragefairy2019.libkt.mkdirsParent
import miragefairy2019.libkt.module
import miragefairy2019.libkt.onServerSave
import miragefairy2019.mod3.main.Main
import mirrg.kotlin.gson.jsonElement
import mirrg.kotlin.gson.jsonWrapper
import mirrg.kotlin.gson.toJson
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.time.Instant

object DaemonSystem {
    private fun getFile(server: MinecraftServer) = server.getWorld(0).saveHandler.worldDirectory.resolve("miragefairy2019/daemon_entities.json")
    val module = module {

        // セーブデータ読み込みとデーモンマネージャーの初期化
        onServerStarting {
            Main.logger.info("DaemonSystem: Loading")
            val data = getFile(server).existsOrNull?.readText()?.jsonWrapper?.orNull
            DaemonManager.instance = if (data != null) {
                DaemonManager(
                    // TODO 分離
                    chatWebhook = data["chatWebhook"].asMap.map { (dimensionalPosExpression, daemonData) ->
                        val dimensionalPos = DimensionalPos.parse(dimensionalPosExpression)
                        dimensionalPos to ChatWebhookDaemon(
                            created = Instant.ofEpochSecond(daemonData["created"].asBigDecimal.toLong()),
                            username = daemonData["username"].asString,
                            webhookUrl = daemonData["webhookUrl"].asString,
                            durationSeconds = daemonData["duration"].orNull?.asLong ?: (60L * 60L * 24L * 30L)
                        )
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
                jsonElement(
                    // TODO 分離
                    "chatWebhook" to daemonEntityManager.chatWebhook.map { (dimensionalPos, daemon) ->
                        dimensionalPos.expression to jsonElement(
                            "created" to daemon.created.epochSecond.jsonElement,
                            "username" to daemon.username.jsonElement,
                            "webhookUrl" to daemon.webhookUrl.jsonElement,
                            "duration" to daemon.durationSeconds.jsonElement
                        )
                    }.jsonElement
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
