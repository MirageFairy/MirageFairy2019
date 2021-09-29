package miragefairy2019.mod3.skill

import io.netty.buffer.ByteBuf
import miragefairy2019.libkt.MessageJson
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod3.skill.api.ApiSkill
import net.minecraft.init.SoundEvents
import net.minecraft.network.PacketBuffer
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.time.Instant


const val discriminatorSkill = 2

class PacketSkill : IMessageHandler<MessageSkill, IMessage> {
    override fun onMessage(message: MessageSkill, messageContext: MessageContext): IMessage? {
        if (messageContext.side == Side.CLIENT) ApiSkill.skillManager.receive(message.json)
        return null
    }
}

class MessageSkill(json: String? = null) : MessageJson(json)


const val discriminatorTrainMastery = 3

class PacketTrainMastery : IMessageHandler<MessageTrainMastery, IMessage> {
    override fun onMessage(message: MessageTrainMastery, messageContext: MessageContext): IMessage? {
        if (messageContext.side == Side.SERVER) {
            val player = messageContext.serverHandler.player
            val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)
            val mastery = EnumMastery.valueOf(message.masteryName!!)
            if (skillContainer.remainingSkillPoints > 0) {
                skillContainer.setMasteryLevel(mastery.name, skillContainer.getMasteryLevel(mastery.name) + 1)
                skillContainer.send(player)
                player.sendStatusMessage(textComponent { (!mastery.displayName).gold + !" のレベルが 1 上がった！" }, false) // TODO translate
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.PLAYERS, 0.75f, 1.2f)
            }
        }
        return null
    }
}

class MessageTrainMastery(var masteryName: String? = null) : IMessage {
    override fun fromBytes(buf: ByteBuf) {
        val packetBuffer = PacketBuffer(buf)
        masteryName = packetBuffer.readString(100)
    }

    override fun toBytes(buf: ByteBuf) {
        val packetBuffer = PacketBuffer(buf)
        packetBuffer.writeString(masteryName!!)
    }
}


const val discriminatorResetMastery = 4

class PacketResetMastery : IMessageHandler<MessageResetMastery, IMessage> {
    override fun onMessage(message: MessageResetMastery, messageContext: MessageContext): IMessage? {
        if (messageContext.side == Side.SERVER) {
            val player = messageContext.serverHandler.player
            val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)
            val now = Instant.now()
            if (skillContainer.canResetMastery(now)) {
                skillContainer.variables.lastMasteryResetTime = now
                skillContainer.masteryList.forEach { skillContainer.setMasteryLevel(it, 0) }
                skillContainer.send(player)
                player.sendStatusMessage(textComponent { !"すべてのマスタリレベルをリセットしました。" }, false) // TODO translate
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.75f, 0.5f)
            }
        }
        return null
    }
}

class MessageResetMastery() : IMessage {
    override fun fromBytes(buf: ByteBuf) = Unit
    override fun toBytes(buf: ByteBuf) = Unit
}
