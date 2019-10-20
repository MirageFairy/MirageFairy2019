package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.lib.component.Composite;
import net.minecraft.item.ItemStack;

public abstract class ItemFairyCraftingToolBase extends ItemFairyWeaponBase
{

	public ItemFairyCraftingToolBase(Composite composite)
	{
		super(composite);
	}

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

}
