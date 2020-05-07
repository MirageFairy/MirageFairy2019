package miragefairy2019.mod.modules.fairyweapon.item;

import net.minecraft.item.ItemStack;

public class ItemFairyWeaponCraftingTool extends ItemFairyWeaponBase
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
	public boolean canSphereReplace(ItemStack itemStack)
	{
		return false;
	}

}
