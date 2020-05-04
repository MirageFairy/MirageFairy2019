package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandMelting2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandMelting2()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(Components.fluorite.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.flame.get()));
		setMaxDamage(64 - 1);
		setDescription("高温注意！");
	}

}
