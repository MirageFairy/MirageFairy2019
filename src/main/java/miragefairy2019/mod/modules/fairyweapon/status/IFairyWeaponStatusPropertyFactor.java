package miragefairy2019.mod.modules.fairyweapon.status;

import java.util.function.DoubleUnaryOperator;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public interface IFairyWeaponStatusPropertyFactor
{

	public double get(IFairyType fairyType);

	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources();

	public default IFairyWeaponStatusPropertyFactor max(double max)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return Math.min(super.get(fairyType), max);
			}
		};
	}

	public default IFairyWeaponStatusPropertyFactor add(double a)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return super.get(fairyType) + a;
			}
		};
	}

	public default IFairyWeaponStatusPropertyFactor sub(double a)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return super.get(fairyType) - a;
			}
		};
	}

	public default IFairyWeaponStatusPropertyFactor mul(double a)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return super.get(fairyType) * a;
			}
		};
	}

	public default IFairyWeaponStatusPropertyFactor div(double a)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return super.get(fairyType) / a;
			}
		};
	}

	public default IFairyWeaponStatusPropertyFactor xp(double a)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return Math.pow(a, super.get(fairyType));
			}
		};
	}

	public default IFairyWeaponStatusPropertyFactor pow(double a)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return Math.pow(super.get(fairyType), a);
			}
		};
	}

	public default IFairyWeaponStatusPropertyFactor apply(DoubleUnaryOperator function)
	{
		return new FairyWeaponStatusPropertyFactorWrapper(this) {
			@Override
			public double get(IFairyType fairyType)
			{
				return function.applyAsDouble(super.get(fairyType));
			}
		};
	}

}
