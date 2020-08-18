package miragefairy2019.mod.api.fairystick;

import java.util.ArrayList;
import java.util.List;

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
