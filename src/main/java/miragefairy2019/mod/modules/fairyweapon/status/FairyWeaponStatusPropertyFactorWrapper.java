package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.FairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public abstract class FairyWeaponStatusPropertyFactorWrapper implements IFairyWeaponStatusPropertyFactor
{

	private final IFairyWeaponStatusPropertyFactor parent;

	public FairyWeaponStatusPropertyFactorWrapper(IFairyWeaponStatusPropertyFactor parent)
	{
		this.parent = parent;
	}

	@Override
	public double get(FairyType fairyType)
	{
		return parent.get(fairyType);
	}

	@Override
	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources()
	{
		return parent.getSources();
	}

}
