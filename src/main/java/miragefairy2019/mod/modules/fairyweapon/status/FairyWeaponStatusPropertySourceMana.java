package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairy.IManaType;
import net.minecraft.util.text.TextFormatting;

public final class FairyWeaponStatusPropertySourceMana implements IFairyWeaponStatusPropertySource
{

	public final IManaType manaType;

	public FairyWeaponStatusPropertySourceMana(IManaType manaType)
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
		return manaType.getTextColor();
	}

	@Override
	public double raw(IFairyType fairyType)
	{
		return fairyType.getManas().getMana(manaType) / (fairyType.getCost() / 50.0);
	}

	@Override
	public double get(IFairyType fairyType)
	{
		return fairyType.getManas().getMana(manaType);
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
