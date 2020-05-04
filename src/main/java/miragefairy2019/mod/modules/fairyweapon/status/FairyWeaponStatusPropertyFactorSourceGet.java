package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FairyWeaponStatusPropertyFactorSourceGet implements IFairyWeaponStatusPropertyFactor
{

	private final IFairyWeaponStatusPropertySource source;

	public FairyWeaponStatusPropertyFactorSourceGet(IFairyWeaponStatusPropertySource source)
	{
		this.source = source;
	}

	@Override
	public double get(IFairyType fairyType)
	{
		return source.get(fairyType);
	}

	@Override
	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources()
	{
		return ISuppliterator.of(source);
	}

}
