package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandHydrating2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandHydrating2()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.water.get()));
		setMaxDamage(32 - 1);
	}

}
