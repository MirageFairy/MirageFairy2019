package miragefairy2019.mod.api.composite;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.util.text.ITextComponent;

public interface IComposite
{

	/**
	 * コンポーネントの量はナノ個単位で表されます。
	 */
	public ISuppliterator<IComponentInstance> getComponents();

	public boolean isEmpty();

	public long getComponentAmount(IComponent component);

	/**
	 * 新しく生成したコンポジットを返します。
	 */
	public IComposite add(IComponentInstance componentInstance);

	/**
	 * 新しく生成したコンポジットを返します。
	 */
	public IComposite add(IComposite composite);

	public ITextComponent getDisplayString();

}
