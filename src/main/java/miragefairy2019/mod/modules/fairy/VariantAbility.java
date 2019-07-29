package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantAbility extends ItemVariant
{

	public final EnumAbilityType abilityType;

	public VariantAbility(String registryName, String oreName, EnumAbilityType abilityType)
	{
		super(registryName, oreName);
		this.abilityType = abilityType;
	}

}
