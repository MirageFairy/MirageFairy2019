package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaConstant<T> implements IFormula<T>
{

	private T value;

	public FormulaConstant(T value)
	{
		this.value = value;
	}

	@Override
	public T get(IFairyType fairyType)
	{
		return value;
	}

	@Override
	public T getMax()
	{
		return value;
	}

	@Override
	public T getMin()
	{
		return value;
	}

	@Override
	public ISuppliterator<ISource> getSources()
	{
		return ISuppliterator.empty();
	}

}
