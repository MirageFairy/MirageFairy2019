package miragefairy2019.mod.api.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public interface IFormula<T>
{

	public T get(IFairyType fairyType);

	public T getMax();

	public T getMin();

	public ISuppliterator<ISource> getSources();

}
