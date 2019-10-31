package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.lib.component.Composite;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

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

	//

	@Override
	public  NonNullList<Ingredient> getRepairmentSpheres(ItemStack itemStack)
	{
		return NonNullList.create();
	}

}
