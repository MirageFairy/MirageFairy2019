package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandCrafting3 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandCrafting3()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(Components.magnetite.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.craft.get()));
		setMaxDamage(64 - 1);
		setDescription("腕が4本欲しくなったときにどうぞ");
	}

}
