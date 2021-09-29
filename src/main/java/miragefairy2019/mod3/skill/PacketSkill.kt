package miragefairy2019.mod3.skill

import miragefairy2019.libkt.MessageJson
import miragefairy2019.mod3.skill.api.ApiSkill
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side


const val discriminatorSkill = 2

class PacketSkill : IMessageHandler<MessageSkill, IMessage> {
    override fun onMessage(message: MessageSkill, messageContext: MessageContext): IMessage? {
        if (messageContext.side == Side.CLIENT) ApiSkill.skillManager.receive(message.json)
        return null
    }
}

class MessageSkill(json: String? = null) : MessageJson(json)
