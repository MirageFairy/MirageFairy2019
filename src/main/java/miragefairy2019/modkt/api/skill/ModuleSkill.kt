package miragefairy2019.modkt.api.skill

import io.netty.buffer.ByteBuf
import miragefairy2019.libkt.Module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.main.ApiMain
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemAppleGold
import net.minecraft.item.ItemFishFood
import net.minecraft.item.ItemSeedFood
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.io.File

val moduleSkill: Module = {

    // マネージャー初期化
    onInstantiation {
        ApiSkill.skillManager = object : SkillManager() {
            override fun getFile(player: EntityPlayer): File = player.world.minecraftServer!!.getWorld(0).saveHandler.worldDirectory.resolve("${ModMirageFairy2019.MODID}/skill/${player.cachedUniqueIdString}.json")
            override fun send(player: EntityPlayerMP, json: String) {
                ApiMain.simpleNetworkWrapper.sendTo(MessageSkill(json), player)
            }
        }
    }

    // ネットワークメッセージ登録
    onRegisterNetworkMessage {
        ApiMain.simpleNetworkWrapper.registerMessage(PacketSkill::class.java, MessageSkill::class.java, 2, Side.CLIENT)
    }

    onInit {
        MinecraftForge.EVENT_BUS.register(object {

            // ログインイベント
            @[Suppress("unused") SubscribeEvent]
            fun handle(event: FMLNetworkEvent.ServerConnectionFromClientEvent) {
                val handler = event.handler as? NetHandlerPlayServer ?: return
                ApiSkill.skillManager.getServerSkillContainer(handler.player).send(handler.player)
            }

            // 保存イベント
            @[Suppress("unused") SubscribeEvent]
            fun handle(event: PlayerEvent.SaveToFile) {
                val player = event.entityPlayer as? EntityPlayerMP ?: return
                ApiSkill.skillManager.getServerSkillContainer(player).save(player)
            }

            // ワールド停止時にロード済みのデータを破棄
            @[Suppress("unused") SubscribeEvent]
            fun handle(event: WorldEvent.Unload) {
                if (event.world.isRemote) return // サーバーワールドのみ
                if (event.world.minecraftServer!!.getWorld(0) !== event.world) return // 地上ディメンションのみ
                ApiMain.logger().info("Unloading all the skill containers")
                ApiSkill.skillManager.resetServer()
            }

        })
    }

}

@Suppress("EnumEntryName")
enum class EnumMastery(private val parent: IMastery?, private val layer: Int) : IMastery {
    root(null, 0),
    /**/ combat(root, 1),
    /**/ /**/ closeCombat(combat, 2),
    /**/ /**/ rangedCombat(combat, 2),
    /**/ /**/ magicCombat(combat, 2),
    /**/ production(root, 1),
    /**/ /**/ harvest(production, 2),
    /**/ /**/ processing(production, 2),
    /**/ /**/ fabrication(production, 2),
    /**/ /**/ /**/ brewing(fabrication, 3),
    ;

    override fun getParent() = parent
    override fun getName() = name
    override fun getCoefficient() = when (layer) {
        0 -> 2
        1 -> 3
        2 -> 5
        3 -> 10
        else -> throw RuntimeException()
    }
}

private class PacketSkill : IMessageHandler<MessageSkill, IMessage> {
    override fun onMessage(message: MessageSkill, messageContext: MessageContext): IMessage? {
        if (messageContext.side == Side.CLIENT) {
            ApiSkill.skillManager.receive(message.json)
        }
        return null
    }
}

private class MessageSkill : IMessage {
    var json: String? = null

    @Suppress("unused") // リフレクションで呼ばれる
    constructor()

    constructor(json: String) {
        this.json = json
    }

    override fun fromBytes(buf: ByteBuf) {
        val packetBuffer = PacketBuffer(buf)
        val size = packetBuffer.readInt()
        json = (0 until size).joinToString("") { packetBuffer.readString(10000) }
    }

    override fun toBytes(buf: ByteBuf) {
        val json = this.json!!
        if (json.length > 1_000_000) throw Exception("Too long json: ${json.length}")
        val strings = json.chunked(10000)
        val packetBuffer = PacketBuffer(buf)
        packetBuffer.writeInt(strings.size)
        strings.forEach {
            packetBuffer.writeString(it)
        }
    }
}
