package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemMiragiumSword extends ItemFairyWeaponBase
{

	public ItemMiragiumSword()
	{
		addComponent(Components.MIRAGIUM, 2);
		addComponent(Components.WOOD, 0.5);
		addComponent(Components.fairyAbilityType(EnumAbilityType.attack));
		addComponent(Components.fairyAbilityType(EnumAbilityType.slash));
		setMaxDamage(64 - 1);
		setDescription("その刃で何を切る？");
	}

}
