package miragefairy2019.mod.api.composite;

import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class ApiComposite
{

	public static IComposite createComposite()
	{
		return new miragefairy2019.mod.modules.composite.Composite();
	}

	public static IComposite createComposite(ISuppliterator<Tuple<IComponent, Long>> components)
	{
		return new miragefairy2019.mod.modules.composite.Composite(components);
	}

}
