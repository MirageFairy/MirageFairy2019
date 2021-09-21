package miragefairy2019.modkt.modules.playeraura

import io.netty.buffer.ByteBuf
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.modkt.api.mana.IManaSet
import miragefairy2019.modkt.api.mana.IManaType
import miragefairy2019.modkt.api.mana.ManaTypes
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura
import miragefairy2019.modkt.impl.ManaSet
import miragefairy2019.modkt.impl.getMana
import miragefairy2019.modkt.impl.mana.displayName
import miragefairy2019.modkt.impl.playeraura.PlayerAuraManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemFood
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import java.lang.Math.sin
import java.util.function.Consumer
import kotlin.math.PI
import kotlin.math.cos

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
                            event.toolTip.add(buildText {
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
                            event.toolTip.add(buildText {
                                translate("miragefairy2019.gui.playerAura.title")
                                text(":")
                            }.formattedText)
                            fun f1(manaType: IManaType): ITextComponent {
                                val before = auraBefore.getMana(manaType)
                                val after = auraAfter.getMana(manaType)
                                val difference = after - before
                                return buildText {
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
                            event.toolTip.add(f1(ManaTypes.shine).formattedText)
                            event.toolTip.add(f1(ManaTypes.fire).formattedText)
                            event.toolTip.add(f1(ManaTypes.wind).formattedText)
                            event.toolTip.add(f1(ManaTypes.gaia).formattedText)
                            event.toolTip.add(f1(ManaTypes.aqua).formattedText)
                            event.toolTip.add(f1(ManaTypes.dark).formattedText)

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

        // ワールド停止時にロード済みのデータを破棄
        erMod.init.register(Consumer {
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @SubscribeEvent
                fun hook(event: WorldEvent.Unload) {
                    if (!event.world.isRemote) {
                        if (event.world.minecraftServer!!.getWorld(0) === event.world) {
                            ApiMain.logger().info("Unloading all playerauras")
                            ApiPlayerAura.playerAuraManager.unloadAllServerPlayerAuraHandlers()
                        }
                    }
                }
            })
        })

    }
}

val modulePlayerAura: Module = {

    // オーラゲージオーバーレイ
    onInit {
        if (ApiMain.side().isClient) {
            MinecraftForge.EVENT_BUS.register(object {
                @SubscribeEvent
                fun hook(event: RenderGameOverlayEvent.Post) {
                    if (event.type != RenderGameOverlayEvent.ElementType.POTION_ICONS) return // ポーションアイコンと同時
                    val player = Minecraft.getMinecraft().player ?: return
                    val playerAuraHandler = ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler

                    val (foodAura, healAmount) = run result@{
                        player.heldItemMainhand.let next@{
                            val item = it.item as? ItemFood ?: return@next
                            val manaSet = playerAuraHandler.getLocalFoodAura(it) ?: return@result Pair(ManaSet.ZERO, 0)
                            return@result Pair(manaSet, item.getHealAmount(it))
                        }
                        player.heldItemOffhand.let next@{
                            val item = it.item as? ItemFood ?: return@next
                            val manaSet = playerAuraHandler.getLocalFoodAura(it) ?: return@result Pair(ManaSet.ZERO, 0)
                            return@result Pair(manaSet, item.getHealAmount(it))
                        }
                        null
                    } ?: return // 食べ物を持っている場合のみ


                    data class Complex(val re: Double, val im: Double)

                    fun drawTriangle(p1: Complex, p2: Complex, p3: Complex, r: Double, g: Double, b: Double) {
                        val tessellator = Tessellator.getInstance()
                        val bufferbuilder = tessellator.buffer
                        GlStateManager.enableBlend()
                        GlStateManager.disableTexture2D()
                        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
                        GlStateManager.color(r.toFloat(), g.toFloat(), b.toFloat(), 1f)
                        bufferbuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION)
                        bufferbuilder.pos(p1.re, p1.im, 0.0).endVertex()
                        bufferbuilder.pos(p2.re, p2.im, 0.0).endVertex()
                        bufferbuilder.pos(p3.re, p3.im, 0.0).endVertex()
                        tessellator.draw()
                        GlStateManager.enableTexture2D()
                        GlStateManager.disableBlend()
                    }

                    fun drawPiece(center: Complex, length: Double, i: Double, rgb: Int, brightness: Double) = drawTriangle(
                            Complex(center.re, center.im),
                            Complex(center.re + cos(PI / 3 * i) * length, center.im - sin(PI / 3 * i) * length),
                            Complex(center.re + cos(PI / 3 * (i + 1)) * length, center.im - sin(PI / 3 * (i + 1)) * length),
                            (rgb shr 16 and 255) / 255.0 * brightness,
                            (rgb shr 8 and 255) / 255.0 * brightness,
                            (rgb and 255) / 255.0 * brightness
                    )

                    fun drawPieces(center: Complex, radius: Double, rgb: Int) = repeat(6) { drawPiece(center, radius, it.toDouble(), rgb, 1.0) }

                    fun drawPieces(center: Complex, radius: Double, health: Double, foodAura: IManaSet) {
                        drawPiece(center, radius * health, 0.0, ManaTypes.wind.color, 0.1 * foodAura.wind)
                        drawPiece(center, radius * health, 1.0, ManaTypes.shine.color, 0.1 * foodAura.shine)
                        drawPiece(center, radius * health, 2.0, ManaTypes.fire.color, 0.1 * foodAura.fire)
                        drawPiece(center, radius * health, 3.0, ManaTypes.gaia.color, 0.1 * foodAura.gaia)
                        drawPiece(center, radius * health, 4.0, ManaTypes.dark.color, 0.1 * foodAura.dark)
                        drawPiece(center, radius * health, 5.0, ManaTypes.aqua.color, 0.1 * foodAura.aqua)
                    }

                    fun drawAuraGauge(center: Complex, radius: Double) {
                        drawPieces(center, radius, 1 + 0.05 + healAmount / 100.0, foodAura)
                        drawPieces(center, radius * 1.05, 0xFFFFFF)
                        ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler.foodHistory.forEach { entry ->
                            drawPieces(center, radius, entry.health, entry.baseLocalFoodAura)
                        }
                    }

                    if (player.isSneaking) {
                        drawAuraGauge(Complex(88.0, 80.0), 80.0)
                    } else {
                        drawAuraGauge(Complex(22.0, 20.0), 20.0)
                    }

                }
            })
        }
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
