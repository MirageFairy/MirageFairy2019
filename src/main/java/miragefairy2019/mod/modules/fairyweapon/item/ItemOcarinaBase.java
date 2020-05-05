package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemOcarinaBase extends ItemFairyWeaponBase
{

	public ItemOcarinaBase()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(Components.apatite.get(), 4);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.art.get()));
		setMaxDamage(128 - 1);
	}

}
