package miragefairy2019.mod.artifacts

import miragefairy2019.lib.get
import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.readFromNBT
import miragefairy2019.lib.writeToNBT
import miragefairy2019.libkt.DimensionalPos
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.block
import miragefairy2019.libkt.dimensionalPos
import miragefairy2019.libkt.displayText
import miragefairy2019.libkt.drawBlockNameplateMultiLine
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setOrRemove
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.sq
import miragefairy2019.libkt.string
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.tileEntityRenderer
import miragefairy2019.libkt.toUnit
import miragefairy2019.libkt.x
import miragefairy2019.libkt.y
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.configProperty
import miragefairy2019.mod.systems.Daemon
import miragefairy2019.mod.systems.DaemonManager
import miragefairy2019.mod.systems.IBlockDaemon
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.util.InventoryTileEntity
import miragefairy2019.util.SmartSlot
import mirrg.kotlin.castOrNull
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.toJson
import mirrg.kotlin.minus
import mirrg.kotlin.utf8ByteArray
import mirrg.kotlin.utf8String
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.util.Random

object ChatWebhook {
    lateinit var enableChatWebhook: () -> Boolean
    lateinit var blockChatWebhookTransmitter: () -> BlockChatWebhookTransmitter
    lateinit var itemChatWebhookTransmitter: () -> ItemBlock
    lateinit var blockCreativeChatWebhookTransmitter: () -> BlockCreativeChatWebhookTransmitter
    lateinit var itemCreativeChatWebhookTransmitter: () -> ItemBlock
    val chatWebhookModule = module {
        enableChatWebhook = configProperty { it.getBoolean("enableChatWebhook", Main.categoryFeatures, true, "Whether the machines that send the in-game chat to the webhook is enabled") }


        // 通常
        blockChatWebhookTransmitter = block({ BlockChatWebhookTransmitter() }, "chat_webhook_transmitter") {
            setUnlocalizedName("chatWebhookTransmitter")
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataBlockStates(
                    variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                        "facing=${facing.first}" to DataBlockState(
                            "miragefairy2019:chat_webhook_transmitter",
                            y = facing.second
                        )
                    }
                )
            }
            makeBlockModel(resourceName.path) {
                DataModel(
                    parent = "block/block",
                    elements = listOf(
                        DataElement(
                            from = DataPoint(0.0, 0.0, 0.0),
                            to = DataPoint(16.0, 16.0, 16.0),
                            faces = DataFaces(
                                down = DataFace(texture = "#side", cullface = "down"),
                                up = DataFace(texture = "#side", cullface = "up"),
                                north = DataFace(texture = "#front", cullface = "north"),
                                south = DataFace(texture = "#side", cullface = "south"),
                                west = DataFace(texture = "#side", cullface = "west"),
                                east = DataFace(texture = "#side", cullface = "east")
                            )
                        )
                    ),
                    textures = mapOf(
                        "particle" to "miragefairy2019:blocks/fairy_machine",
                        "front" to "miragefairy2019:blocks/fairy_machine_display",
                        "side" to "miragefairy2019:blocks/fairy_machine"
                    )
                )
            }
        }
        itemChatWebhookTransmitter = item({ ItemBlock(blockChatWebhookTransmitter()) }, "chat_webhook_transmitter") {
            setCustomModelResourceLocation(variant = "facing=north")
        }
        makeRecipe("chat_webhook_transmitter") {
            DataShapedRecipe(
                pattern = listOf(
                    "wBw",
                    "s#s",
                    "wmw"
                ),
                key = mapOf(
                    "w" to DataOreIngredient(ore = "mirageFairy2019SphereWarp"),
                    "B" to DataSimpleIngredient(item = "minecraft:beacon"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSound"),
                    "#" to DataOreIngredient(ore = "blockMiragium"),
                    "m" to DataOreIngredient(ore = "mirageFairy2019FairyMirageRank1")
                ),
                result = DataResult(
                    item = "${ModMirageFairy2019.MODID}:chat_webhook_transmitter"
                )
            )
        }
        onMakeLang { enJa("tile.chatWebhookTransmitter.name", "Chat Webhook Transmitter", "天耳通の祠") }


        // クリエイティブ用
        blockCreativeChatWebhookTransmitter = block({ BlockCreativeChatWebhookTransmitter() }, "creative_chat_webhook_transmitter") {
            setUnlocalizedName("creativeChatWebhookTransmitter")
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataBlockStates(
                    variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                        "facing=${facing.first}" to DataBlockState(
                            "miragefairy2019:creative_chat_webhook_transmitter",
                            y = facing.second
                        )
                    }
                )
            }
            makeBlockModel(resourceName.path) {
                DataModel(
                    parent = "block/block",
                    elements = listOf(
                        DataElement(
                            from = DataPoint(0.0, 0.0, 0.0),
                            to = DataPoint(16.0, 16.0, 16.0),
                            faces = DataFaces(
                                down = DataFace(texture = "#side", cullface = "down"),
                                up = DataFace(texture = "#side", cullface = "up"),
                                north = DataFace(texture = "#front", cullface = "north"),
                                south = DataFace(texture = "#side", cullface = "south"),
                                west = DataFace(texture = "#side", cullface = "west"),
                                east = DataFace(texture = "#side", cullface = "east")
                            )
                        )
                    ),
                    textures = mapOf(
                        "particle" to "miragefairy2019:blocks/creative_fairy_machine",
                        "front" to "miragefairy2019:blocks/creative_fairy_machine_display",
                        "side" to "miragefairy2019:blocks/creative_fairy_machine"
                    )
                )
            }
        }
        itemCreativeChatWebhookTransmitter = item({ ItemBlock(blockCreativeChatWebhookTransmitter()) }, "creative_chat_webhook_transmitter") {
            setCustomModelResourceLocation(variant = "facing=north")
        }
        onMakeLang { enJa("tile.creativeChatWebhookTransmitter.name", "Creative Chat Webhook Transmitter", "アカーシャのお導きによる天耳通の祠") }


        // 共通

        tileEntity("chat_webhook_transmitter", TileEntityChatWebhookTransmitter::class.java)
        tileEntityRenderer(TileEntityChatWebhookTransmitter::class.java, { TileEntityRendererChatWebhookTransmitter() })

        // Gui
        onInit {
            Main.registerGuiHandler(GuiId.chatWebhookTransmitter, object : ISimpleGuiHandler {
                override fun GuiHandlerContext.onServer() = (tileEntity as? TileEntityChatWebhookTransmitter)?.let { ContainerChatWebhookTransmitter(it, player.inventory, it.inventory) }
                override fun GuiHandlerContext.onClient() = onServer()?.let { GuiChatWebhookTransmitter(it) }
            }.guiHandler)
        }

        // チャット監視ルーチン
        onInit {
            MinecraftForge.EVENT_BUS.register(object {
                @[Suppress("unused") SubscribeEvent]
                fun handle(event: IotMessageEvent) {
                    sendToWebhook(event.senderName, event.message)
                }

                @[Suppress("unused") SubscribeEvent]
                fun handle(event: ServerChatEvent) {
                    sendToWebhook(event.player.name, event.message)
                }

                fun sendToWebhook(playerName: String, message: String) {
                    val manager = DaemonManager.instance ?: return

                    // 本体ブロックが現存しないデーモンを除去する
                    val onFinish = mutableListOf<() -> Unit>()
                    manager.chatWebhook.forEach { (dimensionalPos, daemon) ->
                        val isInvalid = run invalidDaemon@{
                            val world = DimensionManager.getWorld(dimensionalPos.dimension) ?: return@invalidDaemon false // ディメンションがロードされていない
                            if (!world.isBlockLoaded(dimensionalPos.pos)) return@invalidDaemon false // チャンクがロードされていない
                            val block = world.getBlockState(dimensionalPos.pos).block as? IBlockDaemon ?: return@invalidDaemon true // ブロックがおかしい
                            if (!block.canSupportDaemon(world, dimensionalPos.pos, daemon)) return@invalidDaemon true // このデーモンをサポートしていない
                            false // 正常
                        }
                        if (isInvalid) {
                            onFinish += { manager.chatWebhook.setOrRemove(dimensionalPos, null) }
                            return@forEach
                        }
                    }
                    onFinish.forEach { it() }

                    // すべての監視デーモンに対して処理
                    manager.chatWebhook.forEach { (_, daemon) ->

                        // タイムリミット判定
                        val remaining = daemon.timeLimit - Instant.now()
                        if (remaining.isNegative) return@forEach // タイムリミットを越している場合は中止
                        // 除去すると自動復活するため除去はしない

                        // 送信処理
                        // TODO 複数のチャットをまとめる
                        // TODO 複数のデーモンに対してDiscordの負荷対策のためディレイ
                        Thread({
                            if (enableChatWebhook()) {
                                Main.logger.trace("http request start")
                                try {

                                    // 接続設定
                                    val connection = URL(daemon.webhookUrl).openConnection() as HttpURLConnection
                                    connection.requestMethod = "POST"
                                    connection.doOutput = true
                                    connection.setRequestProperty("Content-Type", "application/json")
                                    connection.setRequestProperty("User-Agent", "MirageFairy2019")

                                    // 接続開始
                                    Main.logger.trace("connecting")
                                    connection.connect()

                                    // 入力の送信
                                    Main.logger.trace("sending")
                                    val json = jsonObject(
                                        "username" to "${daemon.username} @ ${remaining.displayText.unformattedText}".jsonElement,
                                        "content" to "<$playerName> $message".jsonElement
                                    ).toJson()
                                    Main.logger.trace(json)
                                    connection.outputStream.use { out -> out.write(json.utf8ByteArray) }

                                    // 出力の受信
                                    Main.logger.trace("receiving")
                                    val responseBody = connection.inputStream.use { it.readBytes() }.utf8String

                                    // 接続終了
                                    Main.logger.trace("disconnecting")
                                    connection.disconnect()

                                    // 結果表示
                                    Main.logger.trace("${connection.responseCode}")
                                    Main.logger.trace(responseBody)

                                } catch (e: Throwable) {
                                    Main.logger.warn("failure", e)
                                }
                                Main.logger.trace("http request finished")
                            }
                        }, "ChatWebhook Request Thread").start()

                    }

                }
            })
        }

    }
}


class IotMessageEvent(val senderName: String, val message: String) : Event()

fun IotMessageEvent.send() = MinecraftForge.EVENT_BUS.post(this)

class ChatWebhookDaemon(val created: Instant, val username: String, val webhookUrl: String, val durationSeconds: Long) : Daemon() {
    val timeLimit: Instant get() = created.plusSeconds(durationSeconds)
}


abstract class BlockChatWebhookTransmitterBase : BlockContainer(Material.IRON), IBlockDaemon {
    init {
        defaultState = blockState.baseState.withProperty(FACING, EnumFacing.NORTH)
        soundType = SoundType.METAL
        tickRandomly = true
    }


    // Variant

    override fun getMetaFromState(state: IBlockState) = when (state.getValue(FACING)) {
        EnumFacing.NORTH -> 0
        EnumFacing.SOUTH -> 1
        EnumFacing.WEST -> 2
        EnumFacing.EAST -> 3
        else -> 0
    }

    override fun getStateFromMeta(meta: Int): IBlockState = when (meta) {
        0 -> defaultState.withProperty(FACING, EnumFacing.NORTH)
        1 -> defaultState.withProperty(FACING, EnumFacing.SOUTH)
        2 -> defaultState.withProperty(FACING, EnumFacing.WEST)
        3 -> defaultState.withProperty(FACING, EnumFacing.EAST)
        else -> defaultState
    }

    fun getFacing(blockState: IBlockState): EnumFacing = blockState.getValue(FACING)
    override fun createBlockState() = BlockStateContainer(this, FACING)
    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)))


    // Graphics
    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL


    // 特殊なインスタンス
    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityChatWebhookTransmitter()
    override fun canSupportDaemon(world: World, blockPos: BlockPos, daemon: Daemon) = daemon is ChatWebhookDaemon


    // アクション

    // 設置時
    override fun onBlockPlacedBy(world: World, blockPos: BlockPos, blockState: IBlockState, placer: EntityLivingBase, itemStack: ItemStack) {
        world.setBlockState(blockPos, blockState.withProperty(FACING, placer.horizontalFacing.opposite), 2) // Variant
        if (!world.isRemote) world.getTileEntity(blockPos)?.castOrNull<TileEntityChatWebhookTransmitter>()?.updateDaemon(true) // タイルエンティティの契約を更新
    }

    // 破壊時
    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        world.getTileEntity(blockPos)?.castOrNull<TileEntityChatWebhookTransmitter>()?.let { tileEntity ->
            tileEntity.inventory.itemStacks.forEach { InventoryHelper.spawnItemStack(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), it) }
            world.updateComparatorOutputLevel(blockPos, this)
        } // 中身のアイテムの放出
        DaemonManager.instance?.chatWebhook?.remove(DimensionalPos(world.provider.dimension, blockPos)) // デーモンを消去
        super.breakBlock(world, blockPos, blockState)
    }

    // デーモンが存在しない場合、読み込んでいるだけで自動的にデーモンを立ち上げる
    // ただし契約の更新はしない
    override fun updateTick(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        if (!world.isRemote) world.getTileEntity(blockPos)?.castOrNull<TileEntityChatWebhookTransmitter>()?.updateDaemon(false)
    }

    // 右クリック時
    override fun onBlockActivated(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (world.isRemote) return true
        world.getTileEntity(blockPos)?.castOrNull<TileEntityChatWebhookTransmitter>()?.updateDaemon(true) // 契約更新
        if (!requireCreative || player.isCreative) player.openGui(ModMirageFairy2019.instance, GuiId.chatWebhookTransmitter, world, blockPos.x, blockPos.y, blockPos.z) // GUIを開く
        return true
    }


    abstract val durationSeconds: Long
    abstract val requireCreative: Boolean


    companion object {
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)
    }
}

class BlockChatWebhookTransmitter : BlockChatWebhookTransmitterBase() {
    init {
        setHardness(10.0f)
        setResistance(50.0f)
        setHarvestLevel("pickaxe", 1)
    }

    override val durationSeconds = 60L * 60L * 24L * 30L
    override val requireCreative = false
}

class BlockCreativeChatWebhookTransmitter : BlockChatWebhookTransmitterBase() {
    init {
        setBlockUnbreakable()
        setResistance(6000000.0f)
    }

    override val durationSeconds = 60L * 60L * 24L * 99999L
    override val requireCreative = true
    override fun quantityDropped(random: Random) = 0
}


class TileEntityChatWebhookTransmitter : TileEntity() {
    val inventory = InventoryChatWebhookTransmitter(this, "tile.chatWebhookTransmitter.name", false, 2)

    val daemon get() = DaemonManager.instance?.chatWebhook?.get(dimensionalPos)
    val username get() = inventory[0].string
    val webhookUrl get() = inventory[1].string

    /** サーバーワールドのみ */
    fun updateDaemon(resetTimestamp: Boolean) {
        if (world.isRemote) return
        val block = world.getBlockState(pos).block as? BlockChatWebhookTransmitterBase ?: return
        val manager = DaemonManager.instance ?: return
        manager.chatWebhook.setOrRemove(dimensionalPos, run fail@{
            ChatWebhookDaemon(
                (if (resetTimestamp) null else daemon?.created) ?: Instant.now(),
                username?.let { "$it at ${world.provider.dimensionType.getName()} (${pos.x},${pos.y},${pos.z})" } ?: return@fail null,
                webhookUrl ?: return@fail null,
                block.durationSeconds
            )
        })
    }

    // データ関係
    fun sendUpdatePacket() = world.playerEntities.forEach { if (it is EntityPlayerMP) it.connection.sendPacket(updatePacket) }
    override fun readFromNBT(nbt: NBTTagCompound) = nbt.also { super.readFromNBT(it) }.also { inventory.readFromNBT(it) }.toUnit()
    override fun writeToNBT(nbt: NBTTagCompound) = nbt.also { super.writeToNBT(it) }.also { inventory.writeToNBT(it) }
    override fun getUpdatePacket() = SPacketUpdateTileEntity(pos, 9, updateTag)
    override fun getUpdateTag() = writeToNBT(NBTTagCompound())
    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
        super.onDataPacket(net, pkt)
    }
}

@SideOnly(Side.CLIENT)
class TileEntityRendererChatWebhookTransmitter : TileEntitySpecialRenderer<TileEntityChatWebhookTransmitter>() {
    // 送信先情報のポップアップオーバーレイGUI
    override fun render(tileEntity: TileEntityChatWebhookTransmitter, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(tileEntity, x, y, z, partialTicks, destroyStage, alpha)

        if (!Minecraft.isGuiEnabled()) return // Guiがオフなら中止
        val blockPosTarget = rendererDispatcher.cameraHitResult?.blockPos ?: return // 何にもヒットしていないなら中止
        if (blockPosTarget != tileEntity.pos) return // このブロックを見ていないなら中止
        if (tileEntity.getDistanceSq(rendererDispatcher.entity.posX, rendererDispatcher.entity.posY, rendererDispatcher.entity.posZ) > 12.sq()) return // 離れすぎているなら中止

        // ネームプレートを表示
        setLightmapDisabled(true)
        val y2 = drawBlockNameplateMultiLine(fontRenderer, tileEntity.webhookUrl ?: "", Vec3d(x, y, z), rendererDispatcher.entityYaw, rendererDispatcher.entityPitch)
        drawBlockNameplateMultiLine(fontRenderer, tileEntity.username ?: "", Vec3d(x, y2, z), rendererDispatcher.entityYaw, rendererDispatcher.entityPitch)
        setLightmapDisabled(false)
    }
}

class InventoryChatWebhookTransmitter(tileEntity: TileEntityChatWebhookTransmitter, title: String, customName: Boolean, slotCount: Int) : InventoryTileEntity<TileEntityChatWebhookTransmitter>(tileEntity, title, customName, slotCount) {
    init {
        addInventoryChangeListener {
            // スロットの状態が変更された場合、タイルエンティティのデーモンを更新
            if (tileEntity.world.takeIf { true } != null && !tileEntity.world.isRemote) tileEntity.updateDaemon(true)
        }
    }

    override fun getInventoryStackLimit() = 1
    override fun isItemValidForSlot(index: Int, itemStack: ItemStack) = itemStack.string != null
}

class ContainerChatWebhookTransmitter(val tileEntity: TileEntityChatWebhookTransmitter, val inventoryPlayer: IInventory, val inventoryTileEntity: IInventory) : Container() {
    init {
        var yi = 3 + 3 + 8 + 3 + 8 + 3
        addSlotToContainer(SmartSlot(inventoryTileEntity, 0, 7 + 18 * 0 + 1, yi + 1))
        yi += 18 + 3 + 8 + 3
        addSlotToContainer(SmartSlot(inventoryTileEntity, 1, 7 + 18 * 0 + 1, yi + 1))
        yi += 18 + 18 + 3 + 8 + 3
        repeat(3) { r -> repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, 9 + 9 * r + c, 7 + 18 * c + 1, yi + 18 * r + 1)) } }
        yi += 18 * 3 + 4
        repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, c, 7 + 18 * c + 1, yi + 1)) }
    }

    override fun canInteractWith(playerIn: EntityPlayer) = inventoryTileEntity.isUsableByPlayer(playerIn)

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val slot = inventorySlots[index] ?: return ItemStack.EMPTY // スロットがnullなら終了
        if (!slot.hasStack) return ItemStack.EMPTY // スロットが空なら終了

        val itemStack = slot.stack
        val itemStackOriginal = itemStack.copy()

        // 移動処理
        // itemStackを改変する
        if (index < 2) { // タイルエンティティ→プレイヤー
            if (!mergeItemStack(itemStack, 2, 2 + 9 * 4, true)) return ItemStack.EMPTY
        } else { // プレイヤー→タイルエンティティ
            if (!mergeItemStack(itemStack, 0, 2, false)) return ItemStack.EMPTY
        }

        if (itemStack.isEmpty) { // スタックが丸ごと移動した
            slot.putStack(ItemStack.EMPTY)
        } else { // 部分的に残った
            slot.onSlotChanged()
        }

        if (itemStack.count == itemStackOriginal.count) return ItemStack.EMPTY // アイテムが何も移動していない場合は終了

        // スロットが改変を受けた場合にここを通過する

        slot.onTake(playerIn, itemStack)

        return itemStackOriginal
    }
}

@SideOnly(Side.CLIENT)
class GuiChatWebhookTransmitter(val container: ContainerChatWebhookTransmitter) : GuiContainer(container) {
    init {
        xSize = 7 + 18 * 9 + 7
        ySize = 3 + 3 + 8 + 3 + 8 + 3 + 18 + 3 + 8 + 3 + 18 + 18 + 3 + 8 + 3 + 18 * 3 + 4 + 18 + 4 + 3
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()

        var yi = 3 + 3 + 8 + 3 + 8 + 3
        drawSlot((x + 7 + 18 * 0).toFloat(), (y + yi).toFloat())
        yi += 18 + 3 + 8 + 3
        drawSlot((x + 7 + 18 * 0).toFloat(), (y + yi).toFloat())
        yi += 18 + 18 + 3 + 8 + 3
        repeat(3) { r -> repeat(9) { c -> drawSlot((x + 7 + 18 * c).toFloat(), (y + yi + 18 * r).toFloat()) } }
        yi += 18 * 3 + 4
        repeat(9) { c -> drawSlot((x + 7 + 18 * c).toFloat(), (y + yi).toFloat()) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        var yi = 0
        yi += 3
        yi += 3

        fontRenderer.drawStringCentered(container.inventoryTileEntity.displayName.unformattedText, xSize / 2, yi, 0x404040)
        val remaining = container.tileEntity.daemon?.timeLimit?.let { it - Instant.now() }
        if (remaining != null) fontRenderer.drawStringRightAligned("残り${remaining.displayText.unformattedText}", xSize - 3 - 3, yi, 0x000088) // TODO translate
        yi += 8

        yi += 3

        fontRenderer.drawString("Bot名", 7 + 1, yi, 0x8B8B8B) // TODO translate
        yi += 8

        yi += 3

        container.inventoryTileEntity[0].string?.let { fontRenderer.drawSplitString(it, 7 + 18 + 4, yi, xSize - 7 - 7 - 18 - 4 - 4, 0x000000) }
        yi += 18

        yi += 3

        fontRenderer.drawString("Webhook URL", 7 + 1, yi, 0x8B8B8B) // TODO translate
        yi += 8

        yi += 3

        container.inventoryTileEntity[1].string?.let { fontRenderer.drawSplitString(it, 7 + 18 + 4, yi, xSize - 7 - 7 - 18 - 4 - 4, 0x000000) }
        yi += 18 + 18

        yi += 3

        fontRenderer.drawString(container.inventoryPlayer.displayName.unformattedText, 8, yi, 0x404040)
    }
}
