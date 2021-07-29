package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.IFormulaInteger;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaIntegerRoundFormulaDouble implements IFormulaInteger
{

	private IFormulaDouble formula;

	public FormulaIntegerRoundFormulaDouble(IFormulaDouble formula)
	{
		this.formula = formula;
	}

	@Override
	public Integer get(IFairyType fairyType)
	{
		return (int) Math.round(formula.get(fairyType));
	}

	@Override
	public Integer getMax()
	{
		return (int) Math.round(formula.getMax());
	}

	@Override
	public Integer getMin()
	{
		return (int) Math.round(formula.getMin());
	}

	@Override
	public ISuppliterator<ISource> getSources()
	{
		return formula.getSources();
	}

}
