package miragefairy2019.mod.modules.composite;

import java.util.HashMap;
import java.util.Map;

import miragefairy2019.mod.api.composite.IComponent;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.composite.IComposite;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class Composite implements IComposite
{

	/**
	 * Longはナノ個単位で表されます。
	 */
	private final ImmutableArray<IComponentInstance> components;

	public Composite()
	{
		this(ISuppliterator.empty());
	}

	public Composite(ISuppliterator<IComponentInstance> components)
	{
		Map<IComponent, Long> map = new HashMap<>();
		components.forEach(e -> map.compute(e.getComponent(), (k, v) -> v != null ? v + e.getNanoAmount() : e.getNanoAmount()));
		this.components = ISuppliterator.ofIterable(map.entrySet())
			.<IComponentInstance> map(e -> new ComponentInstance(e.getKey(), e.getValue()))
			.sorted((a, b) -> {
				int i;

				i = -Double.compare(a.getNanoAmount(), b.getNanoAmount());
				if (i != 0) return i;

				i = a.getComponent().compareTo(b.getComponent());
				if (i != 0) return i;

				return 0;
			})
			.toImmutableArray();
	}

	//

	@Override
	public ISuppliterator<IComponentInstance> getComponents()
	{
		return components.suppliterator();
	}

	@Override
	public long getComponentAmount(IComponent component)
	{
		for (IComponentInstance entry : components) {
			if (entry.getComponent().equals(component)) return entry.getNanoAmount();
		}
		return 0;
	}

	@Override
	public IComposite add(IComponentInstance componentInstance)
	{
		return new Composite(components.suppliterator()
			.after(componentInstance));
	}

	@Override
	public IComposite add(IComposite composite)
	{
		IComposite result = this;
		for (IComponentInstance componentInstance : composite.getComponents()) {
			result = result.add(componentInstance);
		}
		return result;
	}

	@Override
	public String getLocalizedString()
	{
		return components.suppliterator()
			.map(t -> {
				if (t.getNanoAmount() == 1_000_000_000L) {
					return t.getComponent().getLocalizedName();
				} else {
					return t.getComponent().getLocalizedName() + "(" + String.format("%.3f", (t.getNanoAmount() / 1_000_000L) * 0.001) + ")";
				}
			})
			.join(", ");
	}

}
