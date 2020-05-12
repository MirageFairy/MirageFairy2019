package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.IAbilitySet;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairy.IManaSet;
import mirrg.boron.util.struct.ImmutableArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class FairyTypeEmpty implements IFairyType
{

	@Override
	public boolean isEmpty()
	{
		return true;
	}

	@Override
	public ResourceLocation getName()
	{
		return new ResourceLocation("minecraft", "special_empty");
	}

	@Override
	public int getColor()
	{
		return 0xFFFFFF;
	}

	@Override
	public double getCost()
	{
		return 50;
	}

	@Override
	public IManaSet getManas()
	{
		return new ManaSet(0, 0, 0, 0, 0, 0);
	}

	@Override
	public IAbilitySet getAbilities()
	{
		return new AbilitySet(ImmutableArray.empty());
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString("empty");
	}

}
