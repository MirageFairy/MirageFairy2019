package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleFormulaPowDouble implements IFormula<Double>
{

	private IFormula<Double> a;
	private double b;

	/**
	 * @param a
	 *            非負でなければなりません。
	 * @param b
	 *            非負でなければなりません。
	 */
	public FormulaDoubleFormulaPowDouble(IFormula<Double> a, double b)
	{
		this.a = a;
		this.b = b;
		if (a.getMin() < 0) throw new IllegalArgumentException("" + a + ", " + a.getMin());
		if (b < 0) throw new IllegalArgumentException("" + b);
	}

	@Override
	public Double get(IFairyType fairyType)
	{
		return Math.pow(a.get(fairyType), b);
	}

	@Override
	public Double getMax()
	{
		return Math.pow(a.getMax(), b);
	}

	@Override
	public Double getMin()
	{
		return Math.pow(a.getMin(), b);
	}

	@Override
	public ISuppliterator<ISource> getSources()
	{
		return a.getSources();
	}

}
