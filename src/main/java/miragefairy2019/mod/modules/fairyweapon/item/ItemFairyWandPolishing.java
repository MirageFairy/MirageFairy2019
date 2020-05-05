package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandPolishing extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandPolishing()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(Components.moonstone.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.crystal.get()));
		setMaxDamage(64 - 1);
	}

}
