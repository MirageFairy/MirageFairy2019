package miragefairy2019.mod.modules.fairyweapon;

import net.minecraft.item.ItemStack;

public abstract class ItemFairyCraftingToolBase extends ItemFairyWeaponBase
{

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		if (itemStack.getItemDamage() < itemStack.getMaxDamage()) {
			itemStack = itemStack.copy();
			itemStack.setItemDamage(itemStack.getItemDamage() + 1);
			return itemStack;
		}
		return super.getContainerItem(itemStack);
	}

	//

	@Override
	public boolean canRepair(ItemStack itemStack)
	{
		return false;
	}

}
