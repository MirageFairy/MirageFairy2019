package miragefairy2019.mod.api.fairy;

import java.util.HashMap;
import java.util.Map;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;

public final class AbilitySet
{

	public final ImmutableArray<Tuple<IAbilityType, Double>> tuples;
	private Map<IAbilityType, Double> map = new HashMap<>();

	public AbilitySet(ImmutableArray<Tuple<IAbilityType, Double>> tuples)
	{
		this.tuples = tuples;
		for (Tuple<IAbilityType, Double> tuple : tuples) {
			map.put(tuple.x, tuple.y);
		}
	}

	public double get(EnumAbilityType abilityType)
	{
		return map.getOrDefault(abilityType, 0.0);
	}

}
