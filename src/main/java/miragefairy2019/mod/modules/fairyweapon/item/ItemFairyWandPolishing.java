package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemFairyWandPolishing extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandPolishing()
	{
		composite = composite
			.add(Components.MIRAGIUM, 1)
			.add(Components.MOONSTONE, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.crystal));
		setMaxDamage(64 - 1);
		setDescription("究極に手先の器用な妖精さん");
	}

}
