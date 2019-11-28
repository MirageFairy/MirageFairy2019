package miragefairy2019.mod.modules.fairy;

import java.util.ArrayList;
import java.util.List;

import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class FairyRegistry
{

	private static List<Tuple<Ingredient, VariantFairy>> list = new ArrayList<>();

	public static void registerFairy(Ingredient ingredient, VariantFairy fairy)
	{
		list.add(Tuple.of(ingredient, fairy));
	}

	public static ISuppliterator<VariantFairy> getFairies(ItemStack itemStackKey)
	{
		return ISuppliterator.ofIterable(list)
			.filter(t -> t.x.test(itemStackKey))
			.map(Tuple::getY);
	}

	public static ISuppliterator<ItemStack> getKeyItemStacks()
	{
		return ISuppliterator.ofIterable(list)
			.flatMap(t -> ISuppliterator.ofObjArray(t.x.getMatchingStacks()));
	}

}
