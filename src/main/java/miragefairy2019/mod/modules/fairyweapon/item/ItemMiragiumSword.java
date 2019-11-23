package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemMiragiumSword extends ItemFairyWeaponBase
{

	public ItemMiragiumSword()
	{
		composite = composite
			.add(Components.MIRAGIUM, 2)
			.add(Components.WOOD, 0.5)
			.add(Components.fairyAbilityType(EnumAbilityType.attack))
			.add(Components.fairyAbilityType(EnumAbilityType.slash));
		setMaxDamage(64 - 1);
		setDescription("その刃で何を切る？");
	}

}
