package miragefairy2019.mod.api.fairystick;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;

public interface IFairyStickCraftCondition
{

	public boolean test(IFairyStickCraftEnvironment environment, IFairyStickCraftExecutor executor);

	public default ISuppliterator<Iterable<ItemStack>> getIngredientsInput()
	{
		return ISuppliterator.empty();
	}

	public default ISuppliterator<String> getStringsInput()
	{
		return ISuppliterator.empty();
	}

	public default ISuppliterator<Iterable<ItemStack>> getIngredientsOutput()
	{
		return ISuppliterator.empty();
	}

	public default ISuppliterator<String> getStringsOutput()
	{
		return ISuppliterator.empty();
	}

}
