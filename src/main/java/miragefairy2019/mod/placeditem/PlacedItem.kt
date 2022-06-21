package miragefairy2019.mod.placeditem

import miragefairy2019.libkt.block
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.tileEntityRenderer
import miragefairy2019.mod.Main
import miragefairy2019.mod.Main.side
import miragefairy2019.resourcemaker.DataModel
import miragefairy2019.resourcemaker.makeBlockModel
import miragefairy2019.resourcemaker.makeBlockStates
import miragefairy2019.resourcemaker.normal
import net.minecraft.block.Block
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard

object PlacedItem {
    @SideOnly(Side.CLIENT)
    lateinit var keyBindingPlaceItem: KeyBinding
    lateinit var blockPlacedItem: () -> Block
    val module = module {

        // ブロック
        blockPlacedItem = block({ BlockPlacedItem() }, "placed_item") {
            setUnlocalizedName("placedItem")
            makeBlockStates(resourceName.path) { normal }
        }
        makeBlockModel("placed_item") {
            DataModel(
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to "minecraft:blocks/glass"
                ),
                elements = listOf()
            )
        }
        tileEntity("placed_item", TileEntityPlacedItem::class.java)
        tileEntityRenderer(TileEntityPlacedItem::class.java, { TileEntityRendererPlacedItem() })

        // キーバインディング
        onInitKeyBinding {
            if (side.isClient) {
                object : Any() {
                    @SideOnly(Side.CLIENT)
                    fun run() {
                        keyBindingPlaceItem = KeyBinding("miragefairy2019.placeItem", KeyConflictContext.IN_GAME, Keyboard.KEY_Z, "miragefairy2019 (MirageFairy2019)")
                    }
                }.run()
            }
        }

        // ネットワークメッセージ登録
        onRegisterNetworkMessage {
            Main.simpleNetworkWrapper.registerMessage(PacketPlaceItem::class.java, MessagePlaceItem::class.java, 0, Side.SERVER)
        }

        // キーリスナー
        onInit {
            if (side.isClient) {
                object : Any() {
                    @SideOnly(Side.CLIENT)
                    fun run() {
                        ClientRegistry.registerKeyBinding(keyBindingPlaceItem)
                        MinecraftForge.EVENT_BUS.register(object : Any() {
                            @SubscribeEvent
                            fun accept(event: InputUpdateEvent) {
                                while (keyBindingPlaceItem.isPressed) {

                                    // プレイヤー判定
                                    val player = event.entityPlayer
                                    if (player.isSpectator) return // スペクテイターの場合無効
                                    if (player !is EntityPlayerSP) return

                                    // 視線判定
                                    val result = player.rayTrace(player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue, 0f) ?: return // レイトレースが失敗したら中止
                                    if (result.typeOfHit != RayTraceResult.Type.BLOCK) return // ブロックにヒットしなければ中止

                                    // 成立
                                    Main.simpleNetworkWrapper.sendToServer(MessagePlaceItem(result.blockPos, result.sideHit))

                                }
                            }
                        })
                    }
                }.run()
            }
        }

    }
}
