package miragefairy2019.mod.api.fairystick.contents;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRecipe;

public class FairyStickCraftRecipe implements IFairyStickCraftRecipe
{

	private List<IFairyStickCraftCondition> conditions = new ArrayList<>();

	public FairyStickCraftRecipe add(IFairyStickCraftCondition condition)
	{
		conditions.add(condition);
		return this;
	}

	@Override
	public List<IFairyStickCraftCondition> getConditions()
	{
		return conditions;
	}

}
