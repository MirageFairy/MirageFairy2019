package miragefairy2019.mod.modules.placeditem;

import javax.annotation.Nullable;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityPlacedItem extends TileEntity
{

	private NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		ItemStackHelper.saveAllItems(nbt, itemStacks);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		ItemStackHelper.loadAllItems(nbt, itemStacks);
	}

	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 9, getUpdateTag());
	}

	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	//

	public void setItemStack(ItemStack itemStack)
	{
		itemStacks.set(0, itemStack);
	}

	public ItemStack getItemStack()
	{
		return itemStacks.get(0);
	}

}
