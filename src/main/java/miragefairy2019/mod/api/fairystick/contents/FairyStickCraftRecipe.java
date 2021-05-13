package miragefairy2019.mod.api.fairystick.contents;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRecipe;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class FairyStickCraftRecipe implements IFairyStickCraftRecipe
{

	private List<IFairyStickCraftCondition> conditions = new ArrayList<>();

	public FairyStickCraftRecipe()
	{

	}

	public FairyStickCraftRecipe(IFairyStickCraftCondition... conditions)
	{
		for (IFairyStickCraftCondition condition : conditions) {
			add(condition);
		}
	}

	public FairyStickCraftRecipe add(IFairyStickCraftCondition condition)
	{
		conditions.add(condition);
		return this;
	}

	@Override
	public ISuppliterator<IFairyStickCraftCondition> getConditions()
	{
		return ISuppliterator.ofIterable(conditions);
	}

}
