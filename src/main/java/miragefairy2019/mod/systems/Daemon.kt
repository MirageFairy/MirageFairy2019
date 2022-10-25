package miragefairy2019.mod.systems

import com.google.gson.JsonElement
import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.onServerSave
import miragefairy2019.libkt.DimensionalPos
import miragefairy2019.libkt.existsOrNull
import miragefairy2019.libkt.mkdirsParent
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.ChatWebhookDaemonFactory
import mirrg.kotlin.gson.hydrogen.JsonWrapper
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.toJson
import mirrg.kotlin.gson.hydrogen.toJsonElement
import mirrg.kotlin.gson.hydrogen.toJsonWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object DaemonSystem {
    private fun getFile(server: MinecraftServer) = server.getWorld(0).saveHandler.worldDirectory.resolve("miragefairy2019/daemon_entities.json")
    val daemonSystemModule = module {

        // セーブデータ読み込みとデーモンマネージャーの初期化
        onServerStarting {
            Main.logger.info("DaemonSystem: Loading")
            val data = getFile(server).existsOrNull?.readText()?.toJsonElement().toJsonWrapper().orNull
            DaemonManager.daemons = if (data != null) {
                // TODO 分離
                data["chatWebhook"].asMap().map { (dimensionalPosExpression, daemonData) ->
                    val dimensionalPos = DimensionalPos.parse(dimensionalPosExpression)
                    dimensionalPos to ChatWebhookDaemonFactory.fromJson(dimensionalPos, daemonData)
                }.toMap().toMutableMap()
            } else {
                // TODO 分離
                mutableMapOf()
            }
        }

        // 保存イベント
        onServerSave {
            Main.logger.info("DaemonSystem: Saving")
            val daemons = DaemonManager.daemons ?: return@onServerSave
            val server = world.minecraftServer ?: return@onServerSave
            getFile(server).mkdirsParent()
            getFile(server).writeText(
                jsonObject(
                    // TODO 分離
                    "chatWebhook" to daemons.map { (dimensionalPos, daemon) ->
                        dimensionalPos.expression to daemon.toJson()
                    }.jsonObject
                ).toJson { setPrettyPrinting() }
            )
        }

        // サーバーが閉じたときにデーモンリストをリセット
        onServerStopping {
            DaemonManager.daemons = null
            Main.logger.info("DaemonSystem: Terminated")
        }

    }
}


object DaemonManager {
    val daemonFactories = mutableMapOf<ResourceLocation, IDaemonFactory<*>>()
    var daemons: MutableMap<DimensionalPos, Daemon>? = null
}

fun ModScope.daemonFactory(modId: String, name: String, daemonFactoryGetter: () -> IDaemonFactory<*>) = onInit {
    DaemonManager.daemonFactories[ResourceLocation(modId, name)] = daemonFactoryGetter()
}


interface IDaemonFactory<D : Daemon> {
    fun fromJson(dimensionalPos: DimensionalPos, data: JsonWrapper): D
}


abstract class Daemon(val dimensionalPos: DimensionalPos) {
    abstract fun toJson(): JsonElement
}


interface IDaemonBlock {
    fun supportsDaemon(world: World, blockPos: BlockPos, daemon: Daemon): Boolean
}
