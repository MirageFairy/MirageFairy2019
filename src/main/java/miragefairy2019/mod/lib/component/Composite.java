package miragefairy2019.mod.lib.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class Composite
{

	public final ImmutableArray<Tuple<Component, Double>> components;

	public Composite(ISuppliterator<Tuple<Component, Double>> components)
	{
		Map<Component, Double> map = new HashMap<>();
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

	public Optional<Tuple<Component, Double>> getEntry(Component component)
	{
		for (Tuple<Component, Double> entry : components) {
			if (entry.x == component) return Optional.of(entry);
		}
		return Optional.empty();
	}

	public Composite add(Component component, double amount)
	{
		return new Composite(ISuppliterator.concat(
			components.suppliterator(),
			ISuppliterator.of(Tuple.of(component, amount))));
	}

	public String getLocalizedString()
	{
		return components.suppliterator()
			.map(t -> {
				if (t.y == 1.0) {
					return t.x.getLocalizedName();
				} else {
					return t.x.getLocalizedName() + "(" + String.format("%.3f", t.y) + ")";
				}
			})
			.join(", ");
	}

}
