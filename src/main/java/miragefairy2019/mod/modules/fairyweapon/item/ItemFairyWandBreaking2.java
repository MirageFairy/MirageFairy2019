package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandBreaking2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandBreaking2()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(Components.sulfur.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.breaking.get()));
		setMaxDamage(64 - 1);
		setDescription("実はガラスより脆い");
	}

}
