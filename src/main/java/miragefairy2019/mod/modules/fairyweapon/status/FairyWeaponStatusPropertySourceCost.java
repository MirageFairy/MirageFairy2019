package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.FairyType;
import net.minecraft.util.text.TextFormatting;

public final class FairyWeaponStatusPropertySourceCost implements IFairyWeaponStatusPropertySource
{

	@Override
	public String getLocalizedName()
	{
		return "Cost"; // TODO
	}

	@Override
	public TextFormatting getTextColor()
	{
		return TextFormatting.BLACK;
	}

	@Override
	public double raw(FairyType fairyType)
	{
		return 50;
	}

	@Override
	public double get(FairyType fairyType)
	{
		return fairyType.cost;
	}

	@Override
	public int hashCode()
	{
		@SuppressWarnings("unused")
		final int prime = 31;
		int result = 1;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		@SuppressWarnings("unused")
		FairyWeaponStatusPropertySourceCost other = (FairyWeaponStatusPropertySourceCost) obj;
		return true;
	}

}
