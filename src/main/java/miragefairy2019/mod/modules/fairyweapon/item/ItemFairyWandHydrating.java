package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandHydrating extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandHydrating()
	{
		addComponent(Components.wood.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.water.get()));
		setMaxDamage(16 - 1);
	}

}
