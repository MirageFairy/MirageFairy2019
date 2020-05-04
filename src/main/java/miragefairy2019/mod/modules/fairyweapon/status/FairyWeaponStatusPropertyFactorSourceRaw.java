package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FairyWeaponStatusPropertyFactorSourceRaw implements IFairyWeaponStatusPropertyFactor
{

	private final IFairyWeaponStatusPropertySource source;

	public FairyWeaponStatusPropertyFactorSourceRaw(IFairyWeaponStatusPropertySource source)
	{
		this.source = source;
	}

	@Override
	public double get(IFairyType fairyType)
	{
		return source.raw(fairyType);
	}

	@Override
	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources()
	{
		return ISuppliterator.of(source);
	}

}
