package miragefairy2019.mod.lib;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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
}
