package miragefairy2019.modkt.modules.placeditem

import io.netty.buffer.ByteBuf
import miragefairy2019.mod.api.ApiPlacedItem
import net.minecraft.init.SoundEvents
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.PacketBuffer
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

        val itemStack = player.getHeldItem(EnumHand.MAIN_HAND)
        if (itemStack.isEmpty) return null // 何も持っていない場合は中止

        val blockPos = message.blockPos!!
        if (!player.world.getBlockState(blockPos).block.isReplaceable(player.world, blockPos)) return null // 対象空間が埋まっているなら中止
        if (!player.canPlayerEdit(blockPos, EnumFacing.UP, itemStack)) return null // 設置禁止なら中止
        if (!player.world.mayPlace(ApiPlacedItem.blockPlacedItem, blockPos, false, EnumFacing.UP, player)) return null // 設置不可能なら中止

        // 発動

        player.markPlayerActive()

        // ブロックを設置
        if (player.world.setBlockState(blockPos, ApiPlacedItem.blockPlacedItem.defaultState, 11)) {

            val tileEntity = player.world.getTileEntity(blockPos)
            if (tileEntity !is TileEntityPlacedItem) return null // タイルエンティティが変なら中止

            // アイテムを設置
            tileEntity.itemStack = itemStack.splitStack(1)
            tileEntity.markDirty()

            // エフェクト
            player.world.playSound(
                    null,
                    blockPos,
                    SoundEvents.ENTITY_ITEM_PICKUP,
                    SoundCategory.PLAYERS,
                    0.2f,
                    ((Math.random() - Math.random()) * 1.4 + 2.0).toFloat())

        }

        return null
    }
}

class MessagePlaceItem : IMessage {
    var blockPos: BlockPos? = null

    @Suppress("unused") // リフレクションで呼ばれる
    constructor()

    constructor(blockPos: BlockPos) {
        this.blockPos = blockPos
    }

    override fun fromBytes(buf: ByteBuf) {
        blockPos = PacketBuffer(buf).readBlockPos()
    }

    override fun toBytes(buf: ByteBuf) {
        PacketBuffer(buf).writeBlockPos(blockPos)
    }
}
