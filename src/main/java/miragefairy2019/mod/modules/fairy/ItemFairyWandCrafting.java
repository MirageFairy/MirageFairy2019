package miragefairy2019.mod.modules.fairy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFairyWandCrafting extends Item
{

	public ItemFairyWandCrafting()
	{
		this.maxStackSize = 1;
		this.setMaxDamage(4);
	}

	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		itemStack = itemStack.copy();
		if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) return ItemStack.EMPTY;
		itemStack.setItemDamage(itemStack.getItemDamage() + 1);
		return itemStack;
	}

}
