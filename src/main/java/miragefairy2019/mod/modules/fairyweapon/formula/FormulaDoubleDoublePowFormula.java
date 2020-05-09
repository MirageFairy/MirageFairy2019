package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleDoublePowFormula implements IFormula<Double>
{

	private double a;
	private IFormula<Double> b;

	/**
	 * @param a
	 *            非負でなければなりません。
	 */
	public FormulaDoubleDoublePowFormula(double a, IFormula<Double> b)
	{
		this.a = a;
		this.b = b;
		if (a < 0) throw new IllegalArgumentException("" + a);
	}

	@Override
	public Double get(IFairyType fairyType)
	{
		return Math.pow(a, b.get(fairyType));
	}

	@Override
	public Double getMax()
	{
		return a >= 1 ? Math.pow(a, b.getMax()) : Math.pow(a, b.getMin());
	}

	@Override
	public Double getMin()
	{
		return a >= 1 ? Math.pow(a, b.getMin()) : Math.pow(a, b.getMax());
	}

	@Override
	public ISuppliterator<ISource> getSources()
	{
		return b.getSources();
	}

}
