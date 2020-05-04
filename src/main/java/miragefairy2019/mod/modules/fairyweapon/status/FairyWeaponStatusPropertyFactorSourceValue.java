package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FairyWeaponStatusPropertyFactorSourceValue implements IFairyWeaponStatusPropertyFactor
{

	private final double value;

	public FairyWeaponStatusPropertyFactorSourceValue(double value)
	{
		this.value = value;
	}

	@Override
	public double get(IFairyType fairyType)
	{
		return value;
	}

	@Override
	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources()
	{
		return ISuppliterator.empty();
	}

}
