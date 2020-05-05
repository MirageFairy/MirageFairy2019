package miragefairy2019.mod.api.composite;

import mirrg.boron.util.suppliterator.ISuppliterator;

public class ApiComposite
{

	public static IComposite createComposite()
	{
		return new miragefairy2019.mod.modules.composite.Composite();
	}

	public static IComposite createComposite(ISuppliterator<IComponentInstance> components)
	{
		return new miragefairy2019.mod.modules.composite.Composite(components);
	}

	public static IComponentInstance createComponentInstanceNano(IComponent component, long nanoAmount)
	{
		return new miragefairy2019.mod.modules.composite.ComponentInstance(component, nanoAmount);
	}

	public static IComponentInstance createComponentInstance(IComponent component)
	{
		return createComponentInstanceNano(component, 1_000_000_000L);
	}

	public static IComponentInstance createComponentInstance(IComponent component, int amount)
	{
		return createComponentInstanceNano(component, amount * 1_000_000_000L);
	}

	public static IComponentInstance createComponentInstance(IComponent component, double amount)
	{
		return createComponentInstanceNano(component, (long) (amount * 1_000_000_000.0));
	}

}
