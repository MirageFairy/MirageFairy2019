package miragefairy2019.mod.lib;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.item.Item;

public final class Configurator<T> implements Supplier<T>
{

	public final EventRegistryMod erMod;

	public Configurator(EventRegistryMod erMod)
	{
		this.erMod = erMod;
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

	//

	public static <I> Function<Configurator<I>, Monad<Configurator<I>>> onRegisterItem(Consumer<I> consumer)
	{
		return c -> {
			c.erMod.registerItem.register(ic -> consumer.accept(c.get()));
			return Monad.of(c);
		};
	}

	public static <I extends Item> Function<Configurator<I>, Monad<Configurator<I>>> onCreateItemStack(Consumer<I> consumer)
	{
		return c -> {
			c.erMod.createItemStack.register(ic -> consumer.accept(c.get()));
			return Monad.of(c);
		};
	}

}
