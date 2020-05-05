package miragefairy2019.mod.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Configurator<T> implements Supplier<T>
{

	private List<Consumer<EventRegistryMod>> listeners = new ArrayList<>();

	public void hook(Consumer<EventRegistryMod> listener)
	{
		listeners.add(listener);
	}

	public void init(EventRegistryMod erMod)
	{
		listeners.forEach(l -> l.accept(erMod));
	}

	//

	private T t;

	public void set(T t)
	{
		this.t = t;
	}

	@Override
	public T get()
	{
		return t;
	}

}
