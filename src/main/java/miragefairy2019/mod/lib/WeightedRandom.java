package miragefairy2019.mod.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;

import mirrg.boron.util.suppliterator.ISuppliterator;

public class WeightedRandom
{

	public static <T> Optional<T> getRandomItem(Random random, List<Item<T>> items)
	{
		if (items.size() <= 0) return Optional.empty();
		return Optional.of(getRandomItem(items, random.nextDouble() * getTotalWeight(items)));
	}

	public static double getTotalWeight(List<? extends Item<?>> items)
	{
		return items.stream()
			.mapToDouble(i -> i.weight)
			.sum();
	}

	private static <T> T getRandomItem(List<Item<T>> items, double w)
	{
		for (Item<T> item : items) {
			w -= item.weight;
			if (w < 0) return item.item;
		}
		return items.get(items.size() - 1).item;
	}

	public static final class Item<T>
	{

		public final double weight;
		public final T item;

		public Item(T item, double weight)
		{
			this.weight = weight;
			this.item = item;
		}

	}

	public static <T> List<Item<T>> unique(List<Item<T>> dropTable, BiPredicate<T, T> equals)
	{
		class A
		{

			private T t;

			public A(T t)
			{
				this.t = t;
			}

			@Override
			public int hashCode()
			{
				return 0;
			}

			@Override
			public boolean equals(Object obj)
			{
				if (this == obj) return true;
				if (obj == null) return false;
				if (getClass() != obj.getClass()) return false;
				A other = (A) obj;
				if (t == null) {
					if (other.t != null) return false;
				} else if (!equals.test(t, other.t)) return false;
				return true;
			}

		}

		Map<A, Double> map = new HashMap<>();

		for (Item<T> item : dropTable) {
			map.put(new A(item.item), map.getOrDefault(new A(item.item), 0.0) + item.weight);
		}

		return ISuppliterator.ofIterable(map.entrySet())
			.map(e -> new Item<>(e.getKey().t, e.getValue()))
			.toList();
	}
}
