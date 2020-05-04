package miragefairy2019.mod.api.composite;

import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

public interface IComposite
{

	/**
	 * コンポーネントの量はナノ個単位で表されます。
	 */
	public ISuppliterator<Tuple<IComponent, Long>> getComponents();

	public long getComponentAmount(IComponent component);

	/**
	 * 新しく生成したコンポジットを返します。
	 */
	public IComposite addNano(IComponent component, long nanoAmount);

	/**
	 * 新しく生成したコンポジットを返します。
	 */
	public default IComposite add(IComponent component)
	{
		return addNano(component, 1_000_000_000L);
	}

	/**
	 * 新しく生成したコンポジットを返します。
	 */
	public default IComposite add(IComponent component, int amount)
	{
		return addNano(component, amount * 1_000_000_000L);
	}

	/**
	 * 新しく生成したコンポジットを返します。
	 */
	public default IComposite add(IComponent component, double amount)
	{
		return addNano(component, (long) (amount * 1_000_000_000.0));
	}

	public String getLocalizedString();

}
