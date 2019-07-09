package miragefairy2019.mod.lib.multi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMulti<V extends ItemVariant> extends Item
{

	public ItemMulti()
	{
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	//

	private Map<Integer, V> map = new HashMap<>();

	public void registerVariant(int metadata, V variant)
	{
		if (map.containsKey(metadata)) throw new IllegalArgumentException("Illegal metadata: " + metadata);
		map.put(metadata, variant);
	}

	public Optional<V> getVariant(ItemStack stack)
	{
		return getVariant(stack.getMetadata());
	}

	public Optional<V> getVariant(int metadata)
	{
		return Optional.ofNullable(map.get(metadata));
	}

	public Iterable<Tuple<Integer, V>> getVariants()
	{
		return ISuppliterator.ofIterable(map.entrySet())
			.map(e -> Tuple.of(e.getKey(), e.getValue()));
	}

	//

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getVariant(stack).map(v -> v.getUnlocalizedName()).orElse("item.null");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (!isInCreativeTab(tab)) return;
		map.keySet().forEach(metadata -> {
			items.add(new ItemStack(this, 1, metadata));
		});
	}

}
