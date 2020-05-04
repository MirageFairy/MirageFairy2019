package miragefairy2019.mod.modules.composite;

import java.util.HashMap;
import java.util.Map;

import miragefairy2019.mod.api.composite.IComponent;
import miragefairy2019.mod.api.composite.IComposite;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class Composite implements IComposite
{

	/**
	 * Longはナノ個単位で表されます。
	 */
	private final ImmutableArray<Tuple<IComponent, Long>> components;

	public Composite()
	{
		this(ISuppliterator.empty());
	}

	public Composite(ISuppliterator<Tuple<IComponent, Long>> components)
	{
		Map<IComponent, Long> map = new HashMap<>();
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

	//

	@Override
	public ISuppliterator<Tuple<IComponent, Long>> getComponents()
	{
		return components.suppliterator();
	}

	@Override
	public long getComponentAmount(IComponent component)
	{
		for (Tuple<IComponent, Long> entry : components) {
			if (entry.x.equals(component)) return entry.y;
		}
		return 0;
	}

	@Override
	public IComposite addNano(IComponent component, long nanoAmount)
	{
		return new Composite(ISuppliterator.concat(
			components.suppliterator(),
			ISuppliterator.of(Tuple.of(component, nanoAmount))));
	}

	@Override
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
