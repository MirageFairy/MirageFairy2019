package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.fairy.EnumManaType;

public interface IFairyWeaponStatusHelper
{

	public default FairyWeaponStatusPropertyCalculator formula()
	{
		return new FairyWeaponStatusPropertyCalculator();
	}

	public default FairyWeaponStatusPropertyCalculator formula(int initialValue)
	{
		return new FairyWeaponStatusPropertyCalculator(initialValue);
	}

	//

	public default IFairyWeaponStatusPropertyFactor value(double value)
	{
		return new FairyWeaponStatusPropertyFactorSourceValue(value);
	}

	public default IFairyWeaponStatusPropertyFactor cost()
	{
		return new FairyWeaponStatusPropertyFactorSourceGet(new FairyWeaponStatusPropertySourceCost());
	}

	public default IFairyWeaponStatusPropertyFactor mana(EnumManaType manaType)
	{
		return new FairyWeaponStatusPropertyFactorSourceGet(new FairyWeaponStatusPropertySourceMana(manaType));
	}

	public default IFairyWeaponStatusPropertyFactor ability(EnumAbilityType abilityType)
	{
		return new FairyWeaponStatusPropertyFactorSourceGet(new FairyWeaponStatusPropertySourceAbility(abilityType));
	}

	//

	/**
	 * 0超え～2以下の範囲の周波数倍率を、-12～12の範囲のピッチとして表示します。
	 */
	public default IFairyWeaponStatusPropertyView<Double> pitch()
	{
		return value -> String.format("%.2f", Math.log(value) / Math.log(2) * 12);
	}

	public default IFairyWeaponStatusPropertyView<Double> float0()
	{
		return value -> String.format("%.0f", value);
	}

	public default IFairyWeaponStatusPropertyView<Double> float1()
	{
		return value -> String.format("%.1f", value);
	}

	public default IFairyWeaponStatusPropertyView<Double> float2()
	{
		return value -> String.format("%.2f", value);
	}

	public default IFairyWeaponStatusPropertyView<Double> float3()
	{
		return value -> String.format("%.3f", value);
	}

	public default IFairyWeaponStatusPropertyView<Double> percent0()
	{
		return value -> String.format("%.0f%%", value * 100);
	}

	public default IFairyWeaponStatusPropertyView<Double> percent1()
	{
		return value -> String.format("%.1f%%", value * 100);
	}

	public default IFairyWeaponStatusPropertyView<Double> percent2()
	{
		return value -> String.format("%.2f%%", value * 100);
	}

	public default IFairyWeaponStatusPropertyView<Double> percent3()
	{
		return value -> String.format("%.3f%%", value * 100);
	}

	public default IFairyWeaponStatusPropertyView<Integer> integer()
	{
		return value -> "" + value;
	}

}
