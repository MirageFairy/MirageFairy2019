package miragefairy2019.modkt.modules.playeraura

import io.netty.buffer.ByteBuf
import miragefairy2019.mod.api.fairy.IManaSet
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.mod.modules.fairy.EnumManaType
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura
import miragefairy2019.modkt.impl.MutableManaSet
import miragefairy2019.modkt.impl.playeraura.PlayerAuraManager
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemFood
import net.minecraft.network.NetHandlerPlayServer
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
                    if (handler is NetHandlerPlayServer) ApiPlayerAura.playerAuraManager.getServerPlayerAura(handler.player).send(handler.player)
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
                        val playerAura = ApiPlayerAura.playerAuraManager.clientPlayerAura

                        // TODO 摂食履歴
                        // 食べた後のオーラ
                        val aura = playerAura.getFoodAura(event.itemStack)
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
                            event.toolTip.add(f1(playerAura.aura, aura, EnumManaType.shine))
                            event.toolTip.add(f1(playerAura.aura, aura, EnumManaType.fire))
                            event.toolTip.add(f1(playerAura.aura, aura, EnumManaType.wind))
                            event.toolTip.add(f1(playerAura.aura, aura, EnumManaType.gaia))
                            event.toolTip.add(f1(playerAura.aura, aura, EnumManaType.aqua))
                            event.toolTip.add(f1(playerAura.aura, aura, EnumManaType.dark))
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
                                ApiPlayerAura.playerAuraManager.getServerPlayerAura(entityLivingBase).onEat(entityLivingBase, itemStack, item.getHealAmount(itemStack))
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
                fun hook(event: PlayerEvent.SaveToFile) = ApiPlayerAura.playerAuraManager.getServerPlayerAura(event.entityPlayer).save(event.entityPlayer)
            })
        })

    }
}

class PacketPlayerAura : IMessageHandler<MessagePlayerAura, IMessage> {
    @SideOnly(Side.CLIENT)
    override fun onMessage(message: MessagePlayerAura, ctx: MessageContext): IMessage? {
        if (ctx.side == Side.CLIENT) ApiPlayerAura.playerAuraManager.clientPlayerAura.aura = message.aura
        return null
    }
}

class MessagePlayerAura : IMessage {
    val aura = MutableManaSet()

    @Suppress("unused") // リフレクションで呼ばれる
    constructor()

    constructor(aura: IManaSet) {
        this.aura.set(aura)
    }

    override fun fromBytes(buf: ByteBuf) {
        aura.shine = buf.readDouble()
        aura.fire = buf.readDouble()
        aura.wind = buf.readDouble()
        aura.gaia = buf.readDouble()
        aura.aqua = buf.readDouble()
        aura.dark = buf.readDouble()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeDouble(aura.shine)
        buf.writeDouble(aura.fire)
        buf.writeDouble(aura.wind)
        buf.writeDouble(aura.gaia)
        buf.writeDouble(aura.aqua)
        buf.writeDouble(aura.dark)
    }
}
