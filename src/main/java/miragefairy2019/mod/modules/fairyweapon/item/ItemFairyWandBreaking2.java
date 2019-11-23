package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemFairyWandBreaking2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandBreaking2()
	{
		composite = composite
			.add(Components.MIRAGIUM, 1)
			.add(Components.SULFUR, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.breaking));
		setMaxDamage(64 - 1);
		setDescription("実はガラスより脆い");
	}

}
