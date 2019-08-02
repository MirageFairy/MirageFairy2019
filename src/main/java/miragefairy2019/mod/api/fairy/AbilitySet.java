package miragefairy2019.mod.api.fairy;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;

public final class AbilitySet
{

	public final ImmutableArray<Tuple<IAbilityType, Double>> tuples;

	public AbilitySet(ImmutableArray<Tuple<IAbilityType, Double>> tuples)
	{
		this.tuples = tuples;
	}

}
