package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleAbility implements IFormula<Double>
{

	private IAbilityType abilityType;

	public FormulaDoubleAbility(IAbilityType abilityType)
	{
		this.abilityType = abilityType;
	}

	@Override
	public Double get(IFairyType fairyType)
	{
		return fairyType.getAbilities().getAbilityPower(abilityType);
	}

	@Override
	public Double getMax()
	{
		return Double.MAX_VALUE;
	}

	@Override
	public Double getMin()
	{
		return 0.0;
	}

	@Override
	public ISuppliterator<ISource> getSources()
	{
		return ISuppliterator.of(new SourceAbility(abilityType));
	}

}
