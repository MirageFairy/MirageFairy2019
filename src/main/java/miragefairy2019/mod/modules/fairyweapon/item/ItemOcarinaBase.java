package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemOcarinaBase extends ItemFairyWeaponBase
{

	public ItemOcarinaBase()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.APATITE, 4);
		addComponent(Components.fairyAbilityType(EnumAbilityType.art));
		setMaxDamage(128 - 1);
		setDescription("適当に吹いても音楽になる笛");
	}

}
