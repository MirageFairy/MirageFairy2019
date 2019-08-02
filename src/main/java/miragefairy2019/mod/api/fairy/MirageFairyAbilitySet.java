package miragefairy2019.mod.api.fairy;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;

public final class MirageFairyAbilitySet
{

	public final ImmutableArray<Tuple<IMirageFairyAbilityType, Double>> tuples;

	public MirageFairyAbilitySet(ImmutableArray<Tuple<IMirageFairyAbilityType, Double>> tuples)
	{
		this.tuples = tuples;
	}

}
