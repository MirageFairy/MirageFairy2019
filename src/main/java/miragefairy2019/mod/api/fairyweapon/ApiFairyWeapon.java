package miragefairy2019.mod.api.fairyweapon;

import miragefairy2019.mod.lib.EventRegistryMod;

public class ApiFairyWeapon
{

	public static void init(EventRegistryMod erMod)
	{
		miragefairy2019.mod.modules.fairyweapon.ModuleFairyWeapon.init(erMod);
		miragefairy2019.mod.modules.fairyweapon.item.Loader.init(erMod);
	}

}
