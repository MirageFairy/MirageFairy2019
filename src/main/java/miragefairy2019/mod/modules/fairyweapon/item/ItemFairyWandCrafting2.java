package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemFairyWandCrafting2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandCrafting2()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.craft));
		setMaxDamage(32 - 1);
		setDescription("靴を作ってくれる妖精さん");
	}

}
