package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.EnumManaType;
import miragefairy2019.mod.api.fairy.FairyType;
import net.minecraft.util.text.TextFormatting;

public final class FairyWeaponStatusPropertySourceMana implements IFairyWeaponStatusPropertySource
{

	public final EnumManaType manaType;

	public FairyWeaponStatusPropertySourceMana(EnumManaType manaType)
	{
		this.manaType = manaType;
	}

	@Override
	public String getLocalizedName()
	{
		return manaType.getLocalizedName();
	}

	@Override
	public TextFormatting getTextColor()
	{
		return manaType.colorText;
	}

	@Override
	public double raw(FairyType fairyType)
	{
		return fairyType.manaSet.get(manaType) / (fairyType.cost / 50.0);
	}

	@Override
	public double get(FairyType fairyType)
	{
		return fairyType.manaSet.get(manaType);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((manaType == null) ? 0 : manaType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FairyWeaponStatusPropertySourceMana other = (FairyWeaponStatusPropertySourceMana) obj;
		if (manaType != other.manaType) return false;
		return true;
	}

}
