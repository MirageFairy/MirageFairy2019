package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairy.ComponentsAbilityType;
import miragefairy2019.mod.modules.fairy.ModuleFairy;

public class ApiFairy
{

	public static void init(EventRegistryMod erMod)
	{
		ModuleFairy.init(erMod);
	}

	public static IComponentAbilityType getComponentAbilityType(IAbilityType abilityType)
	{
		return ComponentsAbilityType.getComponentAbilityType(abilityType);
	}

}
