package miragefairy2019.mod.lib;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// TODO mirrg
public final class Monad<T> implements Supplier<T>
{

	private T t;

	public static <T> Monad<T> of(T t)
	{
		return new Monad<>(t);
	}

	private Monad(T t)
	{
		this.t = t;
	}

	public <O> O bind(Function<T, O> function)
	{
		return function.apply(t);
	}

	public <O> Monad<O> map(Function<T, O> function)
	{
		return Monad.of(function.apply(t));
	}

	public Monad<T> peek(Consumer<T> consumer)
	{
		consumer.accept(t);
		return this;
	}

	@Override
	public T get()
	{
		return t;
	}

}
