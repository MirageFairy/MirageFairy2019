package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;

public class ItemFairyWandCrafting2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandCrafting2()
	{
		addComponent(Components.miragium.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.craft.get()));
		setMaxDamage(32 - 1);
		setDescription("靴を作ってくれる妖精さん");
	}

}
