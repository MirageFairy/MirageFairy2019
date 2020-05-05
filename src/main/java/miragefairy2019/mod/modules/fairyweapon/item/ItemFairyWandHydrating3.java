package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandHydrating3 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandHydrating3()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(Components.moonstone.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.water.get()));
		setMaxDamage(64 - 1);
		setDescription("直射日光を避けて保管してください。");
	}

}
