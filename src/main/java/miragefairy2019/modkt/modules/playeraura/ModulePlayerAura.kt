package miragefairy2019.modkt.modules.playeraura

import io.netty.buffer.ByteBuf
import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.mod.modules.fairy.EnumManaType
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura
import miragefairy2019.modkt.impl.playeraura.PlayerAuraManager
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemFood
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.function.Consumer

object ModulePlayerAura {
    @JvmStatic // TODO remove jvm
    fun init(erMod: EventRegistryMod) {

        // マネージャー初期化
        erMod.initRegistry.register(Runnable { ApiPlayerAura.playerAuraManager = PlayerAuraManager() })

        // ネットワークメッセージ登録
        erMod.registerNetworkMessage.register(Runnable { ApiMain.simpleNetworkWrapper.registerMessage(PacketPlayerAura::class.java, MessagePlayerAura::class.java, 1, Side.CLIENT) })

        // ログインイベント
        erMod.init.register(Consumer {
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @Suppress("unused")
                @SubscribeEvent
                fun hook(event: ServerConnectionFromClientEvent) {
                    val handler = event.handler
                    if (handler is NetHandlerPlayServer) ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(handler.player).send()
                }
            })
        })

        // アイテムツールチップイベント
        erMod.init.register(Consumer {
            if (ApiMain.side().isClient) {
                MinecraftForge.EVENT_BUS.register(object : Any() {
                    @Suppress("unused")
                    @SubscribeEvent
                    fun hook(event: ItemTooltipEvent) {

                        // なぜかクライアント起動時に呼び出される
                        if (event.entityPlayer == null) return

                        // 食べ物以外には反応しない
                        val itemStack = event.itemStack
                        val item = itemStack.item
                        if (item !is ItemFood) return

                        // 食べた後のオーラ表示
                        val playerAuraHandler = ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler
                        val auraBefore = playerAuraHandler.playerAura
                        val auraAfter = playerAuraHandler.simulatePlayerAura(itemStack, item.getHealAmount(itemStack))
                        if (auraAfter != null) {

                            // 飽和率ポエム
                            event.toolTip.add(text {
                                translate(playerAuraHandler.getSaturationRate(itemStack).let { saturationRate ->
                                    when {
                                        saturationRate > 0.7 -> "miragefairy2019.gui.playerAura.poem.step5"
                                        saturationRate > 0.5 -> "miragefairy2019.gui.playerAura.poem.step4"
                                        saturationRate > 0.3 -> "miragefairy2019.gui.playerAura.poem.step3"
                                        saturationRate > 0.1 -> "miragefairy2019.gui.playerAura.poem.step2"
                                        else -> "miragefairy2019.gui.playerAura.poem.step1"
                                    }
                                })
                            }.formattedText)

                            // 栄養素表示
                            event.toolTip.add(text {
                                translate("miragefairy2019.gui.playerAura.title")
                                text(":")
                            }.formattedText)
                            fun f1(manaType: EnumManaType): ITextComponent {
                                val before = auraBefore.getMana(manaType)
                                val after = auraAfter.getMana(manaType)
                                val difference = after - before
                                return text {
                                    text("  ")
                                    text(manaType.displayName)
                                    text(": ")
                                    format("%3.0f", before)
                                    when {
                                        difference > 2 -> text(" +++++").color(TextFormatting.GREEN)
                                        difference < -2 -> text(" -----").color(TextFormatting.RED)
                                        difference > 1 -> text(" ++++").color(TextFormatting.GREEN)
                                        difference < -1 -> text(" ----").color(TextFormatting.RED)
                                        difference > 0.5 -> text(" +++").color(TextFormatting.GREEN)
                                        difference < -0.5 -> text(" ---").color(TextFormatting.RED)
                                        difference > 0.2 -> text(" ++").color(TextFormatting.GREEN)
                                        difference < -0.2 -> text(" --").color(TextFormatting.RED)
                                        difference > 0.1 -> text(" +").color(TextFormatting.GREEN)
                                        difference < -0.1 -> text(" -").color(TextFormatting.RED)
                                        else -> Unit
                                    }
                                }.color(manaType.getTextColor())
                            }
                            event.toolTip.add(f1(EnumManaType.shine).formattedText)
                            event.toolTip.add(f1(EnumManaType.fire).formattedText)
                            event.toolTip.add(f1(EnumManaType.wind).formattedText)
                            event.toolTip.add(f1(EnumManaType.gaia).formattedText)
                            event.toolTip.add(f1(EnumManaType.aqua).formattedText)
                            event.toolTip.add(f1(EnumManaType.dark).formattedText)

                        }
                    }
                })
            }
        })

        // アイテム使用イベント
        erMod.init.register(Consumer {
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @Suppress("unused")
                @SubscribeEvent
                fun hook(event: LivingEntityUseItemEvent.Finish) {
                    if (!event.entity.world.isRemote) {
                        val itemStack = event.item
                        val item = itemStack.item
                        if (item is ItemFood) {
                            val entityLivingBase = event.entityLiving
                            if (entityLivingBase is EntityPlayerMP) {
                                val playerAuraHandler = ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(entityLivingBase)
                                playerAuraHandler.onEat(itemStack, item.getHealAmount(itemStack))
                                playerAuraHandler.send()
                            }
                        }
                    }
                }
            })
        })

        // 保存イベント
        erMod.init.register(Consumer {
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @Suppress("unused")
                @SubscribeEvent
                fun hook(event: PlayerEvent.SaveToFile) {
                    val player = event.entityPlayer
                    if (player is EntityPlayerMP) ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(player).save()
                }
            })
        })

    }
}

class PacketPlayerAura : IMessageHandler<MessagePlayerAura, IMessage> {
    @SideOnly(Side.CLIENT)
    override fun onMessage(message: MessagePlayerAura, ctx: MessageContext): IMessage? {
        if (ctx.side == Side.CLIENT) {
            try {
                ApiPlayerAura.playerAuraManager.setClientPlayerAuraModelJson(message.json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
}

class MessagePlayerAura : IMessage {
    var json: String? = null // TODO メッセージングAPIの整備

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
