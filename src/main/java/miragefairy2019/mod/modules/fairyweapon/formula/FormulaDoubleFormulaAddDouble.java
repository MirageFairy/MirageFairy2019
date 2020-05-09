package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleFormulaAddDouble implements IFormula<Double>
{

	private IFormula<Double> a;
	private double b;

	public FormulaDoubleFormulaAddDouble(IFormula<Double> a, double b)
	{
		this.a = a;
		this.b = b;
	}

	@Override
	public Double get(IFairyType fairyType)
	{
		return a.get(fairyType) + b;
	}

	@Override
	public Double getMax()
	{
		return a.getMax() + b;
	}

	@Override
	public Double getMin()
	{
		return a.getMin() + b;
	}

	@Override
	public ISuppliterator<ISource> getSources()
	{
		return a.getSources();
	}

}
