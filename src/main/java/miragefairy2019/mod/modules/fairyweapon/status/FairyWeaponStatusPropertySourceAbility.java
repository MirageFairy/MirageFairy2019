package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.fairy.FairyType;
import net.minecraft.util.text.TextFormatting;

public final class FairyWeaponStatusPropertySourceAbility implements IFairyWeaponStatusPropertySource
{

	public final EnumAbilityType abilityType;

	public FairyWeaponStatusPropertySourceAbility(EnumAbilityType abilityType)
	{
		this.abilityType = abilityType;
	}

	@Override
	public String getLocalizedName()
	{
		return abilityType.getLocalizedName();
	}

	@Override
	public TextFormatting getTextColor()
	{
		return abilityType.getTextColor();
	}

	@Override
	public double raw(FairyType fairyType)
	{
		return fairyType.abilitySet.get(abilityType);
	}

	@Override
	public double get(FairyType fairyType)
	{
		return fairyType.abilitySet.get(abilityType) * (fairyType.cost / 50.0);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abilityType == null) ? 0 : abilityType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FairyWeaponStatusPropertySourceAbility other = (FairyWeaponStatusPropertySourceAbility) obj;
		if (abilityType != other.abilityType) return false;
		return true;
	}

}
