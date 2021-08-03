package miragefairy2019.modkt.modules.playeraura

import io.netty.buffer.ByteBuf
import miragefairy2019.mod.api.fairy.IManaSet
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.mod.modules.fairy.EnumManaType
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura
import miragefairy2019.modkt.impl.playeraura.PlayerAuraManager
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemFood
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
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
import kotlin.math.floor

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
                        if (event.itemStack.item !is ItemFood) return

                        // 現在オーラ
                        val playerAuraHandler = ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler

                        // TODO 摂食履歴の表示
                        // 食べた後のオーラ
                        val aura = playerAuraHandler.getLocalFoodAura(event.itemStack)
                        if (aura != null) {
                            fun format(value: Double): Int {
                                var value2 = floor(value).toInt()
                                if (value2 == 0 && value > 0) value2 = 1
                                if (value2 == 0 && value < 0) value2 = -1
                                return value2
                            }

                            fun f1(auraBefore: IManaSet, auraAfter: IManaSet, manaType: EnumManaType): String {
                                val before = auraBefore.getMana(manaType)
                                val after = auraAfter.getMana(manaType)
                                val difference = after - before
                                return TextComponentString("")
                                        .appendSibling(manaType.displayName)
                                        .appendText(": ")
                                        .appendText(String.format("%3d", format(before)))
                                        .appendText(" → ")
                                        .appendSibling(TextComponentString(String.format("%3d", format(after)))
                                                .setStyle(Style().setBold(true)))
                                        .appendText(" (")
                                        .appendSibling(TextComponentString("")
                                                .appendText(String.format("%4d", format(difference)))
                                                .setStyle(Style().setColor(if (difference > 0) TextFormatting.GREEN else if (difference < 0) TextFormatting.RED else TextFormatting.GRAY)))
                                        .appendText(")")
                                        .setStyle(Style().setColor(manaType.getTextColor()))
                                        .formattedText
                            }

                            event.toolTip.add("Aura:")
                            event.toolTip.add(f1(playerAuraHandler.playerAura, aura, EnumManaType.shine))
                            event.toolTip.add(f1(playerAuraHandler.playerAura, aura, EnumManaType.fire))
                            event.toolTip.add(f1(playerAuraHandler.playerAura, aura, EnumManaType.wind))
                            event.toolTip.add(f1(playerAuraHandler.playerAura, aura, EnumManaType.gaia))
                            event.toolTip.add(f1(playerAuraHandler.playerAura, aura, EnumManaType.aqua))
                            event.toolTip.add(f1(playerAuraHandler.playerAura, aura, EnumManaType.dark))
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
