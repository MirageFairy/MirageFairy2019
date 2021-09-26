package miragefairy2019.mod3.skill

import io.netty.buffer.ByteBuf
import miragefairy2019.mod3.skill.api.ApiSkill
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

class PacketSkill : IMessageHandler<MessageSkill, IMessage> {
    override fun onMessage(message: MessageSkill, messageContext: MessageContext): IMessage? {
        if (messageContext.side == Side.CLIENT) {
            ApiSkill.skillManager.receive(message.json)
        }
        return null
    }
}

class MessageSkill : IMessage {
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
