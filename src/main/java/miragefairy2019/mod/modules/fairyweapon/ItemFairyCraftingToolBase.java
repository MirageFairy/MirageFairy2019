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
	public boolean hasContainerItem(ItemStack itemStack)
	{
		return !(itemStack.getItemDamage() >= itemStack.getMaxDamage());
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
