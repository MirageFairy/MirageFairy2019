package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemFairyWandCrafting3 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandCrafting3()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.MAGNETITE, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.craft));
		setMaxDamage(64 - 1);
		setDescription("腕が4本欲しくなったときにどうぞ");
	}

}
