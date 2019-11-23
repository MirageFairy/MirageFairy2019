package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemMagicWandBase extends ItemFairyWeaponBase
{

	public ItemMagicWandBase()
	{
		composite = composite
			.add(Components.MIRAGIUM, 1)
			.add(Components.FLUORITE, 1)
			.add(Components.MIRAGIUM, 4)
			.add(Components.fairyAbilityType(EnumAbilityType.knowledge));
		setMaxDamage(128 - 1);
		setDescription("風の心、探求");
	}

}
