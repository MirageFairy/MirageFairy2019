package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemFairyWandMelting2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandMelting2()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.FLUORITE, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.flame));
		setMaxDamage(64 - 1);
		setDescription("高温注意！");
	}

}
