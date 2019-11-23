package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemMagicWandBase extends ItemFairyWeaponBase
{

	public ItemMagicWandBase()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.FLUORITE, 1);
		addComponent(Components.MIRAGIUM, 4);
		addComponent(Components.fairyAbilityType(EnumAbilityType.knowledge));
		setMaxDamage(128 - 1);
		setDescription("風の心、探求");
	}

}
