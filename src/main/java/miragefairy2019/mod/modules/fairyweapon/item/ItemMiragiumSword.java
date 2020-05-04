package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemMiragiumSword extends ItemFairyWeaponBase
{

	public ItemMiragiumSword()
	{
		addComponent(Components.miragium.get(), 2);
		addComponent(Components.wood.get(), 0.5);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.attack.get()));
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.slash.get()));
		setMaxDamage(64 - 1);
		setDescription("その刃で何を切る？");
	}

}
