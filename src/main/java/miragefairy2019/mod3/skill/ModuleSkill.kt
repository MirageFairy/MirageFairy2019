package miragefairy2019.mod3.skill

import io.netty.buffer.ByteBuf
import miragefairy2019.libkt.Component
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.TextAlignment
import miragefairy2019.libkt.argb
import miragefairy2019.libkt.component
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.label
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.position
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.toArgb
import miragefairy2019.libkt.tooltip
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod3.main.registerGuiHandler
import miragefairy2019.mod3.skill.api.ApiSkill
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.Container
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.io.File

const val guiIdSkill = 2

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

    // スキルGUI
    onInit {
        registerGuiHandler(guiIdSkill, object : IGuiHandler {
            override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = ContainerSkill()
            override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = GuiSkill()
        })
    }

}

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

class ContainerSkill : Container() {
    override fun canInteractWith(playerIn: EntityPlayer) = true
}

class GuiSkill : GuiContainer(ContainerSkill()) {

    private val components = mutableListOf<Component>()

    init {
        components += component(RectangleInt(4, 4, xSize - 4 - 4 - 20 - 20, 10)) {
            label(::fontRenderer, "マスタリ名", color = 0xFF404040.toArgb())
        }
        components += component(RectangleInt(xSize - 44, 4, 20, 10)) {
            label(::fontRenderer, "MLv", color = argb(0xFF404040.toInt()), align = TextAlignment.RIGHT)
            tooltip("マスタリレベルはある領域に関する理解の深さです。")
        }
        components += component(RectangleInt(xSize - 24, 4, 20, 10)) {
            label(::fontRenderer, "SLv", color = argb(0xFF404040.toInt()), align = TextAlignment.RIGHT)
            tooltip("スキルレベルは個々のアクションの強さです。")
        }
        EnumMastery.values().forEachIndexed { i, it ->
            components += component(RectangleInt(4 + 8 * it.layer, 14 + 10 * i, xSize - 4 - 4 - 20 - 20 - 8 * it.layer, 10)) {
                label(::fontRenderer, it.displayName.unformattedText, color = 0xFF000000.toArgb())
                tooltip(it.displayPoem.unformattedText)
            }
            components += component(RectangleInt(xSize - 44, 14 + 10 * i, 20, 10)) {
                label(::fontRenderer, "${ApiSkill.skillManager.clientSkillContainer.getMasteryLevel(it)}", color = 0xFF000000.toArgb(), align = TextAlignment.RIGHT)
            }
            components += component(RectangleInt(xSize - 24, 14 + 10 * i, 20, 10)) {
                label(::fontRenderer, "${ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it)}", color = 0xFF000000.toArgb(), align = TextAlignment.RIGHT)
            }
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        components.forEach { it.onScreenDraw.fire { it(PointInt(mouseX, mouseY) - position, partialTicks) } }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) = rectangle.drawGuiBackground()

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        components.forEach { it.onForegroundDraw.fire { it(PointInt(mouseX, mouseY) - position) } }
    }
}
