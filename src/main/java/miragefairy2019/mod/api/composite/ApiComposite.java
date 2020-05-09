package miragefairy2019.mod.api.composite;

import miragefairy2019.mod.modules.composite.ComponentInstance;
import miragefairy2019.mod.modules.composite.Composite;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class ApiComposite
{

	public static IComposite composite()
	{
		return new Composite();
	}

	public static IComposite composite(ISuppliterator<IComponentInstance> components)
	{
		return new Composite(components);
	}

	public static IComponentInstance instanceNano(IComponent component, long nanoAmount)
	{
		return new ComponentInstance(component, nanoAmount);
	}

	public static IComponentInstance instance(IComponent component)
	{
		return instanceNano(component, 1_000_000_000L);
	}

	public static IComponentInstance instance(IComponent component, int amount)
	{
		return instanceNano(component, amount * 1_000_000_000L);
	}

	public static IComponentInstance instance(IComponent component, double amount)
	{
		return instanceNano(component, (long) (amount * 1_000_000_000.0));
	}

}
