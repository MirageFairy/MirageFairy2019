package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemBellBase extends ItemFairyWeaponBase
{

	public ItemBellBase()
	{
		addComponent(Components.MIRAGIUM, 0.5);
		addComponent(Components.MIRAGIUM, 3);
		addComponent(Components.fairyAbilityType(EnumAbilityType.submission));
		setMaxDamage(64 - 1);
		setDescription("妖精の力を解放せよ");
	}

}
