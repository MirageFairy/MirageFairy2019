package miragefairy2019.mod.modules.placeditem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public class TileEntityPlacedItem extends TileEntity {

    public NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
    public double rotation;
    public boolean standing;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        ItemStackHelper.saveAllItems(nbt, itemStacks);
        nbt.setDouble("rotation", rotation);
        nbt.setBoolean("standing", standing);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ItemStackHelper.loadAllItems(nbt, itemStacks);
        rotation = nbt.getDouble("rotation");
        standing = nbt.getBoolean("standing");
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 9, getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }

    public void sendUpdatePacket() {
        for (EntityPlayer entityPlayer : world.playerEntities) {
            if (entityPlayer instanceof EntityPlayerMP) {
                ((EntityPlayerMP) entityPlayer).connection.sendPacket(getUpdatePacket());
            }
        }
    }

    //

    public void setItemStack(ItemStack itemStack) {
        itemStacks.set(0, itemStack);
    }

    public ItemStack getItemStack() {
        return itemStacks.get(0);
    }

    //

    public void action() {
        rotation += 45;
        if (rotation >= 360) {
            rotation -= 360;
            standing = !standing;
        }
    }

}
