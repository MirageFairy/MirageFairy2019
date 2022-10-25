package miragefairy2019.lib

import com.google.gson.JsonElement
import io.netty.buffer.ByteBuf
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.gson.hydrogen.toJson
import mirrg.kotlin.gson.hydrogen.toJsonElement
import mirrg.kotlin.java.hydrogen.mkdirsOrThrow
import mirrg.kotlin.log4j.hydrogen.getLogger
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.io.File

abstract class ExtraPlayerStatusManager<H : ExtraPlayerStatusMessageHandler<M, D>, M : ExtraPlayerStatusMessage, D> {

    abstract val name: String

    abstract val simpleNetworkWrapper: SimpleNetworkWrapper

    abstract val discriminator: Int

    abstract val messageHandlerClass: Class<H>

    abstract val messageClass: Class<M>

    abstract fun createMessage(): M

    abstract fun createData(): D

    abstract fun toJson(data: D): JsonElement?

    /** @throws RuntimeException */
    abstract fun fromJson(jsonElement: JsonElement?): D


    private val logger = getLogger()

    private val lock = Any()

    private var clientData: D? = null

    private val serverData = mutableMapOf<String, D>()

    /** Only Client World */
    fun getClientData(): D {
        synchronized(lock) {
            val data = clientData
            if (data != null) return data
            val data2 = createData()
            clientData = data2
            return data2
        }
    }

    /** Only Client World */
    fun setClientData(data: D) {
        synchronized(lock) {
            clientData = data
        }
    }

    /** Only Server World */
    fun getServerData(player: EntityPlayerMP): D {
        synchronized(lock) {
            val data = serverData[player.cachedUniqueIdString]
            if (data != null) return data
            val data2 = load(player)
            serverData[player.cachedUniqueIdString] = data2
            return data2
        }
    }

    /** Only Server World */
    fun resetAllServerData() {
        synchronized(lock) {
            serverData.clear()
        }
    }

    /** Only Server World */
    fun getFile(player: EntityPlayer) = File(player.world.minecraftServer!!.getWorld(0).saveHandler.worldDirectory, "${ModMirageFairy2019.MODID}/$name/${player.cachedUniqueIdString}.json")

    /** Only Server World */
    fun save(player: EntityPlayerMP) {
        synchronized(lock) {
            val data = getServerData(player)
            val file = getFile(player)
            logger.trace("Saving: ${player.name} > $file")
            try {
                file.parentFile.mkdirsOrThrow()
                file.writeText(toJson(data).toJson { setPrettyPrinting() })
            } catch (e: Exception) {
                logger.error("Can not save $name file")
                logger.error("  Player: ${player.name}(${player.cachedUniqueIdString})")
                logger.error("  File: $file")
                logger.error("  Data: ${toJson(data).toJson()}")
                logger.error(e)
            }
        }
    }

    /** Only Server World */
    fun load(player: EntityPlayerMP): D {
        synchronized(lock) {
            val file = getFile(player)
            if (file.exists()) {
                try {
                    return fromJson(file.readText().toJsonElement())
                } catch (e: Exception) {
                    logger.error("Can not load $name file")
                    logger.error("  Player: ${player.name}(${player.cachedUniqueIdString})")
                    logger.error("  File: $file")
                    logger.error(e)
                    return createData()
                }
            } else {
                return createData()
            }
        }
    }

    /** Only Server World */
    fun send(player: EntityPlayerMP) {
        synchronized(lock) {
            val data = getServerData(player)
            val message = createMessage()
            message.json = toJson(data).toJson()
            simpleNetworkWrapper.sendTo(message, player)
        }
    }


    val module
        get() = module {

            // ネットワークメッセージ登録
            onRegisterNetworkMessage {
                simpleNetworkWrapper.registerMessage(messageHandlerClass, messageClass, discriminator, Side.CLIENT)
            }

            onInit {
                MinecraftForge.EVENT_BUS.register(object {

                    // ログインイベント
                    @Suppress("unused")
                    @SubscribeEvent
                    fun hook(event: FMLNetworkEvent.ServerConnectionFromClientEvent) {
                        val handler = event.handler
                        if (handler is NetHandlerPlayServer) send(handler.player)
                    }

                    // 保存イベント
                    @Suppress("unused")
                    @SubscribeEvent
                    fun hook(event: PlayerEvent.SaveToFile) {
                        val player = event.entityPlayer
                        if (player is EntityPlayerMP) save(player)
                    }

                    // ワールド停止時にロード済みのデータを破棄
                    @Suppress("unused")
                    @SubscribeEvent
                    fun hook(event: WorldEvent.Unload) {
                        if (!event.world.isRemote) {
                            if (event.world.minecraftServer!!.getWorld(0) === event.world) {
                                logger.debug("Unloading all player's $name data")
                                resetAllServerData()
                            }
                        }
                    }

                })
            }

        }

}

/** 継承したクラスは空引数のコンストラクタを持つ必要があります。 */
abstract class ExtraPlayerStatusMessageHandler<M : ExtraPlayerStatusMessage, D> : IMessageHandler<M, IMessage> {
    abstract val manager: ExtraPlayerStatusManager<*, M, D>

    @SideOnly(Side.CLIENT)
    override fun onMessage(message: M, ctx: MessageContext): IMessage? {
        if (ctx.side == Side.CLIENT) {
            try {
                manager.setClientData(manager.fromJson(message.json.toJsonElement()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
}

/** 継承したクラスは空引数のコンストラクタを持つ必要があります。 */
abstract class ExtraPlayerStatusMessage : IMessage {
    lateinit var json: String

    override fun fromBytes(buf: ByteBuf) {
        val packetBuffer = PacketBuffer(buf)
        val size = packetBuffer.readInt()
        json = (0 until size).joinToString("") { packetBuffer.readString(10000) }
    }

    override fun toBytes(buf: ByteBuf) {
        val json = this.json
        if (json.length > 1_000_000) throw Exception("Too long json: ${json.length}")
        val strings = json.chunked(10000)
        val packetBuffer = PacketBuffer(buf)
        packetBuffer.writeInt(strings.size)
        strings.forEach {
            packetBuffer.writeString(it)
        }
    }
}
