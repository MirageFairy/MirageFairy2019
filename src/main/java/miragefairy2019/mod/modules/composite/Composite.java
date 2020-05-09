package miragefairy2019.mod.modules.composite;

import java.util.HashMap;
import java.util.Map;

import miragefairy2019.mod.api.composite.IComponent;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.composite.IComposite;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

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
	public ITextComponent getDisplayString()
	{
		return components.suppliterator()
			.map(ci -> {
				TextComponentString textComponent = new TextComponentString("");

				textComponent.appendSibling(ci.getComponent().getDisplayName());

				if (ci.getNanoAmount() != 1_000_000_000L) {
					textComponent.appendSibling(new TextComponentString(String.format("(%.3f)", (ci.getNanoAmount() / 1_000_000L) * 0.001)));
				}

				return textComponent;
			})
			.sandwich(new TextComponentString(", "))
			.apply(tcs -> {
				TextComponentString textComponent = new TextComponentString("");
				tcs.forEach(textComponent::appendSibling);
				return textComponent;
			});
	}

}
