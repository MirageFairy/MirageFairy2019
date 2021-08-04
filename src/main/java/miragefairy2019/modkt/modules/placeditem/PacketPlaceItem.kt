package miragefairy2019.modkt.modules.placeditem

import io.netty.buffer.ByteBuf
import miragefairy2019.mod.api.ApiPlacedItem
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

class PacketPlaceItem : IMessageHandler<MessagePlaceItem, IMessage> {
    override fun onMessage(message: MessagePlaceItem, ctx: MessageContext): IMessage? {
        if (ctx.side != Side.SERVER) return null // サーバーワールド側でのみ処理する

        val handler = ctx.netHandler
        if (handler !is NetHandlerPlayServer) return null

        val player = handler.player
        //PacketThreadUtil.checkThreadAndEnqueue(this, handler, player.getServerWorld());
        if (player.isSpectator) return null // スペクテイターなら中止

        val world = player.world

        fun effect(blockPos: BlockPos) = world.playSound(
                null,
                blockPos,
                SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.PLAYERS,
                0.2f,
                ((Math.random() - Math.random()) * 1.4 + 2.0).toFloat())


        fun tryBreak(): EnumActionResult { // 撤去
            val blockPos = message.blockPos!! // 起点座標
            val blockState = world.getBlockState(blockPos) // 指定座標のブロック
            if (blockState !== ApiPlacedItem.blockPlacedItem.defaultState) return EnumActionResult.PASS // 指定座標は置かれたブロックでなければならない

            // 発動

            // プレイヤーの行動更新
            player.markPlayerActive()

            if (!player.canPlayerEdit(blockPos, EnumFacing.UP, ItemStack.EMPTY)) return EnumActionResult.FAIL // 改変禁止なら中止

            val tileEntity = world.getTileEntity(blockPos)
            if (tileEntity !is TileEntityPlacedItem) return EnumActionResult.FAIL // 異常なTileだった場合は中止

            // アイテムを回収
            val itemStackContained = tileEntity.itemStack
            tileEntity.itemStack = ItemStack.EMPTY
            tileEntity.markDirty()

            // アイテムを増やす
            if (!player.inventory.addItemStackToInventory(itemStackContained)) {
                player.dropItem(itemStackContained, false)
            }

            // ブロックを削除
            val res = world.setBlockState(blockPos, Blocks.AIR.defaultState, 11)
            if (!res) return EnumActionResult.FAIL // 設置に失敗したら中止

            // エフェクト
            effect(blockPos)

            return EnumActionResult.SUCCESS
        }
        if (tryBreak() != EnumActionResult.PASS) return null

        fun tryPut(): EnumActionResult { // 設置
            val blockPos = message.blockPos!!.offset(message.facing!!) // オフセット座標
            val blockState = world.getBlockState(blockPos) // 指定座標のブロック
            if (!blockState.block.isAir(blockState, world, blockPos)) return EnumActionResult.PASS // 指定座標は空気でなければならない

            val itemStackHeld = player.getHeldItem(EnumHand.MAIN_HAND)
            if (itemStackHeld.isEmpty) return EnumActionResult.PASS // 何も持っていない場合は中止

            if (!world.mayPlace(ApiPlacedItem.blockPlacedItem, blockPos, false, EnumFacing.UP, player)) return EnumActionResult.PASS // 設置不可能なら中止

            // 発動

            // プレイヤーの行動更新
            player.markPlayerActive()

            if (!player.canPlayerEdit(blockPos, EnumFacing.UP, itemStackHeld)) return EnumActionResult.FAIL // 改変禁止なら中止

            // ブロックを設置
            val res = world.setBlockState(blockPos, ApiPlacedItem.blockPlacedItem.defaultState, 11)
            if (!res) return EnumActionResult.FAIL // 設置に失敗したら中止

            val tileEntity = world.getTileEntity(blockPos)
            if (tileEntity !is TileEntityPlacedItem) return EnumActionResult.FAIL // タイルエンティティが変なら中止

            // アイテムを設置
            tileEntity.itemStack = itemStackHeld.splitStack(1)
            tileEntity.markDirty()

            // エフェクト
            effect(blockPos)

            return EnumActionResult.SUCCESS
        }
        if (tryPut() != EnumActionResult.PASS) return null

        return null
    }
}

class MessagePlaceItem : IMessage {
    var blockPos: BlockPos? = null
    var facing: EnumFacing? = null

    @Suppress("unused") // リフレクションで呼ばれる
    constructor()

    constructor(blockPos: BlockPos, facing: EnumFacing) {
        this.blockPos = blockPos
        this.facing = facing
    }

    override fun fromBytes(buf: ByteBuf) {
        val packetBuffer = PacketBuffer(buf)
        blockPos = packetBuffer.readBlockPos()
        facing = packetBuffer.readEnumValue(EnumFacing::class.java)
    }

    override fun toBytes(buf: ByteBuf) {
        val packetBuffer = PacketBuffer(buf)
        packetBuffer.writeBlockPos(blockPos)
        packetBuffer.writeEnumValue(facing)
    }
}
