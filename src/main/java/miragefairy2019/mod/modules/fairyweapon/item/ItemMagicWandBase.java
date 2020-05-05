package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemMagicWandBase extends ItemFairyWeaponBase
{

	public ItemMagicWandBase()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(Components.fluorite.get(), 1);
		addComponent(Components.miragium.get(), 4);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.knowledge.get()));
		setMaxDamage(128 - 1);
	}

}
