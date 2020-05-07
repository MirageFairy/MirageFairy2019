package miragefairy2019.mod.api.fairyweapon;

import miragefairy2019.mod.lib.EventRegistryMod;

public class ApiFairyWeapon
{

	public static void init(EventRegistryMod erMod)
	{
		miragefairy2019.mod.modules.fairyweapon.item.Loader.init(erMod);
		miragefairy2019.mod.modules.fairyweapon.damagesource.Loader.init(erMod);
		miragefairy2019.mod.modules.fairyweapon.recipe.Loader.init(erMod);
	}

}
