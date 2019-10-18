package miragefairy2019.mod.lib.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class Composite
{

	/**
	 * Longはナノ個単位で表されます。
	 */
	public final ImmutableArray<Tuple<Component, Long>> components;

	public Composite(ISuppliterator<Tuple<Component, Long>> components)
	{
		Map<Component, Long> map = new HashMap<>();
		components.forEach(e -> map.compute(e.x, (k, v) -> v != null ? v + e.y : e.y));
		this.components = ISuppliterator.ofIterable(map.entrySet())
			.map(e -> Tuple.of(e.getKey(), e.getValue()))
			.sorted((a, b) -> {
				int i;

				i = -Double.compare(a.y, b.y);
				if (i != 0) return i;

				i = a.x.compareTo(b.x);
				if (i != 0) return i;

				return 0;
			})
			.toImmutableArray();
	}

	public static Composite empty()
	{
		return new Composite(ISuppliterator.of());
	}

	//

	public Optional<Tuple<Component, Long>> getEntry(Component component)
	{
		for (Tuple<Component, Long> entry : components) {
			if (entry.x == component) return Optional.of(entry);
		}
		return Optional.empty();
	}

	public Composite add(Component component)
	{
		return new Composite(ISuppliterator.concat(
			components.suppliterator(),
			ISuppliterator.of(Tuple.of(component, 1_000_000_000L))));
	}

	public Composite add(Component component, int amount)
	{
		return new Composite(ISuppliterator.concat(
			components.suppliterator(),
			ISuppliterator.of(Tuple.of(component, amount * 1_000_000_000L))));
	}

	public Composite add(Component component, double amount)
	{
		return new Composite(ISuppliterator.concat(
			components.suppliterator(),
			ISuppliterator.of(Tuple.of(component, (long) (amount * 1_000_000_000.0)))));
	}

	public String getLocalizedString()
	{
		return components.suppliterator()
			.map(t -> {
				if (t.y == 1_000_000_000L) {
					return t.x.getLocalizedName();
				} else {
					return t.x.getLocalizedName() + "(" + String.format("%.3f", (t.y / 1_000_000L) * 0.001) + ")";
				}
			})
			.join(", ");
	}

}
