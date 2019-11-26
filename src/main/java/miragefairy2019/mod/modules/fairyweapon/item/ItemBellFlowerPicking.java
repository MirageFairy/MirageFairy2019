package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;

public class ItemBellFlowerPicking extends ItemBellBase
{

	public ItemBellFlowerPicking()
	{
		addComponent(Components.MIRAGIUM, 0.5);
		addComponent(Components.MAGNETITE, 0.5);
		addComponent(Components.PYROPE, 2);
		addComponent(Components.GOLD, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.slash));
		setMaxDamage(128 - 1);
		setDescription("ちょっとお花を摘みに");
	}

}
