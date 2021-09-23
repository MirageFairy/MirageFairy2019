package miragefairy2019.modkt.api.skill

import io.netty.buffer.ByteBuf
import miragefairy2019.jei.JeiUtilities.Companion.drawStringRightAligned
import miragefairy2019.libkt.Module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod3.main.registerGuiHandler
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.Container
import net.minecraft.item.ItemAppleGold
import net.minecraft.item.ItemFishFood
import net.minecraft.item.ItemSeedFood
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import org.lwjgl.opengl.GL11
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

@Suppress("EnumEntryName")
enum class EnumMastery(private val parent: IMastery?, val layer: Int) : IMastery {
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

class ContainerSkill() : Container() {
    override fun canInteractWith(playerIn: EntityPlayer) = true
}

class GuiSkill() : GuiContainer(ContainerSkill()) {
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        if (mouseX in x + xSize - 44..x + xSize - 24 && mouseY in y + 4..y + 14) {
            drawHoveringText(listOf("マスタリレベルはある領域に関する理解の深さです。"), mouseX, mouseY)
        }
        if (mouseX in x + xSize - 24..x + xSize - 4 && mouseY in y + 4..y + 14) {
            drawHoveringText(listOf("スキルレベルは個々のアクションの強さです。"), mouseX, mouseY)
        }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        fun drawGuiBackground(left: Int, top: Int, width: Int, height: Int) {
            Gui.drawRect(left + 0, top + 0, left + width, top + height, 0xFF000000.toInt())
            Gui.drawRect(left + 3, top + 3, left + width - 1, top + height - 1, 0xFF555555.toInt())
            Gui.drawRect(left + 1, top + 1, left + 3, top + height - 1, 0xFFFFFFFF.toInt())
            Gui.drawRect(left + 1, top + 1, left + width - 1, top + 3, 0xFFFFFFFF.toInt())
            Gui.drawRect(left + 3, top + 3, left + width - 3, top + height - 3, 0xFFC6C6C6.toInt())
        }

        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        drawGuiBackground(x, y, xSize, ySize)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString("マスタリ名", 4, 4, 0x404040)
        fontRenderer.drawStringRightAligned("MLv", xSize - 24, 4, 0x404040)
        fontRenderer.drawStringRightAligned("SLv", xSize - 4, 4, 0x404040)
        EnumMastery.values().forEachIndexed { i, it ->
            fontRenderer.drawString(it.displayName.unformattedText, 4 + 8 * it.layer, 14 + 10 * i, 0x000000)
            fontRenderer.drawStringRightAligned("${ApiSkill.skillManager.clientSkillContainer.getMasteryLevel(it)}", xSize - 24, 14 + 10 * i, 0x000000)
            fontRenderer.drawStringRightAligned("${ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it)}", xSize - 4, 14 + 10 * i, 0x000000)
        }
    }
}
