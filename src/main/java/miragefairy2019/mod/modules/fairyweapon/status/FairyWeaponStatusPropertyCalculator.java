package miragefairy2019.mod.modules.fairyweapon.status;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.fairy.FairyType;

public class FairyWeaponStatusPropertyCalculator
{

	private List<IFairyWeaponStatusPropertyConfigurator> configurators = new ArrayList<>();
	private List<IFairyWeaponStatusPropertySource> sources = new ArrayList<>();

	public FairyWeaponStatusPropertyCalculator()
	{
		this(0);
	}

	public FairyWeaponStatusPropertyCalculator(double initialValue)
	{
		add(initialValue);
	}

	public double calculate(FairyType fairyType)
	{
		double value = 0;
		for (IFairyWeaponStatusPropertyConfigurator configurator : configurators) {
			value = configurator.apply(value, fairyType);
		}
		return value;
	}

	//

	public FairyWeaponStatusPropertyCalculator add(double value)
	{
		configurators.add((v, ft) -> v + value);
		return this;
	}

	public FairyWeaponStatusPropertyCalculator add(IFairyWeaponStatusPropertyFactor factor)
	{
		configurators.add((v, ft) -> v + factor.get(ft));
		factor.getSources().forEach(sources::add);
		return this;
	}

	public FairyWeaponStatusPropertyCalculator mul(double value)
	{
		configurators.add((v, ft) -> v * value);
		return this;
	}

	public FairyWeaponStatusPropertyCalculator mul(IFairyWeaponStatusPropertyFactor factor)
	{
		configurators.add((v, ft) -> v * factor.get(ft));
		factor.getSources().forEach(sources::add);
		return this;
	}

	/**
	 * {@code threshold(1, 2, 5, 10)} のように呼び出すと、10以上のときに4、5以上のときに3、2以上のときに2、1以上のときに1、どれでもない場合に0が返ります。
	 */
	public FairyWeaponStatusPropertyCalculator threshold(double... thresholds)
	{
		configurators.add((v, ft) -> {
			for (int i = thresholds.length - 1; i >= 0; i--) {
				if (v >= thresholds[i] - 0.0005) {
					return i + 1;
				}
			}
			return 0;
		});
		return this;
	}

	//

	public IFairyWeaponStatusProperty<Double> asDouble(String unlocalizedName, IFairyWeaponStatusPropertyView<Double> view)
	{
		return new FairyWeaponStatusPropertyBase<Double>(unlocalizedName, sources, view) {
			@Override
			public Double get(FairyType fairyType)
			{
				return calculate(fairyType);
			}
		};
	}

	public IFairyWeaponStatusProperty<Integer> asInt(String unlocalizedName, IFairyWeaponStatusPropertyView<Integer> view)
	{
		return new FairyWeaponStatusPropertyBase<Integer>(unlocalizedName, sources, view) {
			@Override
			public Integer get(FairyType fairyType)
			{
				return (int) (Math.round(calculate(fairyType) * 1000) / 1000);
			}
		};
	}

}
