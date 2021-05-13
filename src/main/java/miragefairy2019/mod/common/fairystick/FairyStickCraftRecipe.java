package miragefairy2019.mod.common.fairystick;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRecipe;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class FairyStickCraftRecipe implements IFairyStickCraftRecipe
{

	private List<IFairyStickCraftCondition> conditions = new ArrayList<>();

	@Override
	public ISuppliterator<IFairyStickCraftCondition> getConditions()
	{
		return ISuppliterator.ofIterable(conditions);
	}

	public static Consumer<FairyStickCraftRecipe> adderCondition(IFairyStickCraftCondition condition)
	{
		return recipe -> recipe.conditions.add(condition);
	}

}
