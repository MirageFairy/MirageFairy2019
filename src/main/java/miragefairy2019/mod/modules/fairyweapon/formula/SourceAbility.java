package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import net.minecraft.util.text.ITextComponent;

public class SourceAbility implements ISource
{

	private IAbilityType abilityType;

	public SourceAbility(IAbilityType abilityType)
	{
		this.abilityType = abilityType;
	}

	@Override
	public String getIdentifier()
	{
		return "ability." + abilityType.getName();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return abilityType.getDisplayName();
	}

}
