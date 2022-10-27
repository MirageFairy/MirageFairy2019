package miragefairy2019.mod.systems

import com.google.gson.JsonElement
import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.onServerSave
import miragefairy2019.libkt.DimensionalPos
import miragefairy2019.libkt.existsOrNull
import miragefairy2019.libkt.mkdirsParent
import miragefairy2019.libkt.setOrRemove
import miragefairy2019.mod.artifacts.ChatWebhookDaemonFactory
import mirrg.kotlin.gson.hydrogen.JsonWrapper
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.toJson
import mirrg.kotlin.gson.hydrogen.toJsonElement
import mirrg.kotlin.gson.hydrogen.toJsonWrapper
import mirrg.kotlin.log4j.hydrogen.getLogger
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager

object DaemonSystem {
    private val logger = getLogger()
    private fun getFile(server: MinecraftServer) = server.getWorld(0).saveHandler.worldDirectory.resolve("miragefairy2019/daemon_entities.json")
    val daemonSystemModule = module {

        // セーブデータ読み込みとデーモンマネージャーの初期化
        onServerStarting {
            logger.info("Loading")
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
            logger.debug("Saving")
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
            logger.info("Terminated")
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
    fun checkBlock(): Boolean {
        val isInvalid = run invalidDaemon@{
            val world = DimensionManager.getWorld(dimensionalPos.dimension) ?: return@invalidDaemon false // ディメンションがロードされていない
            if (!world.isBlockLoaded(dimensionalPos.pos)) return@invalidDaemon false // チャンクがロードされていない
            val block = world.getBlockState(dimensionalPos.pos).block as? IDaemonBlock ?: return@invalidDaemon true // ブロックがおかしい
            if (!block.supportsDaemon(world, dimensionalPos.pos, this)) return@invalidDaemon true // このデーモンをサポートしていない
            false // 正常
        }
        if (isInvalid) DaemonManager.daemons?.setOrRemove(dimensionalPos, null)
        return !isInvalid
    }
}


interface IDaemonBlock {
    fun supportsDaemon(world: World, blockPos: BlockPos, daemon: Daemon): Boolean
}
