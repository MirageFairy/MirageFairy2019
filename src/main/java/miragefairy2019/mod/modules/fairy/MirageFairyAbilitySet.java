package miragefairy2019.mod.modules.fairy;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;

public final class MirageFairyAbilitySet
{

	public final ImmutableArray<Tuple<EnumAbilityType, Double>> tuples;

	public MirageFairyAbilitySet(ImmutableArray<Tuple<EnumAbilityType, Double>> tuples)
	{
		this.tuples = tuples;
	}

}
