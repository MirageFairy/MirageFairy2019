package miragefairy2019.mod.modules.composite;

import miragefairy2019.mod.api.composite.IComponent;
import miragefairy2019.mod.api.composite.IComponentInstance;

public class ComponentInstance implements IComponentInstance
{

	private IComponent component;
	private long nanoAmount;

	public ComponentInstance(IComponent component, long nanoAmount)
	{
		this.component = component;
		this.nanoAmount = nanoAmount;
	}

	@Override
	public IComponent getComponent()
	{
		return component;
	}

	@Override
	public long getNanoAmount()
	{
		return nanoAmount;
	}

}
