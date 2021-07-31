package miragefairy2019.mod.modules.placeditem;

import miragefairy2019.mod.api.ApiPlacedItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketPlaceItem implements IMessageHandler<MessagePlaceItem, IMessage> {

    @Override
    public IMessage onMessage(MessagePlaceItem message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            if (ctx.netHandler instanceof NetHandlerPlayServer) {
                NetHandlerPlayServer handler = (NetHandlerPlayServer) ctx.netHandler;
                EntityPlayerMP player = handler.player;

                //PacketThreadUtil.checkThreadAndEnqueue(this, handler, player.getServerWorld());
                player.markPlayerActive();

                if (!player.isSpectator()) {
                    ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);

                    // 対象空間が埋まっているなら中止
                    if (!player.world.getBlockState(message.blockPos).getBlock().isReplaceable(player.world, message.blockPos)) return null;

                    // 何も持っていない場合は中止
                    if (itemStack.isEmpty()) return null;

                    // 設置不可能なら中止
                    if (!player.canPlayerEdit(message.blockPos, EnumFacing.UP, itemStack)) return null;
                    if (!player.world.mayPlace(ApiPlacedItem.blockPlacedItem, message.blockPos, false, EnumFacing.UP, player)) return null;

                    // 発動

                    // ブロックを設置
                    if (player.world.setBlockState(message.blockPos, ApiPlacedItem.blockPlacedItem.getDefaultState(), 11)) {

                        // タイルエンティティが変なら中止
                        TileEntity tileEntity = player.world.getTileEntity(message.blockPos);
                        if (!(tileEntity instanceof TileEntityPlacedItem)) return null;

                        // アイテムを設置
                        ((TileEntityPlacedItem) tileEntity).setItemStack(itemStack.splitStack(1));
                        tileEntity.markDirty();

                        // エフェクト
                        player.world.playSound(
                                null,
                                message.blockPos,
                                SoundEvents.ENTITY_ITEM_PICKUP,
                                SoundCategory.PLAYERS,
                                0.2f,
                                (float) ((Math.random() - Math.random()) * 1.4 + 2.0));

                    }

                }

            }
        }
        return null;
    }

}
