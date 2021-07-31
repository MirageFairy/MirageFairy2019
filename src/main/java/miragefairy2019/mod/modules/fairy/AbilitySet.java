package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.IAbilitySet;
import miragefairy2019.mod.api.fairy.IAbilityType;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

import java.util.HashMap;
import java.util.Map;

public final class AbilitySet implements IAbilitySet {

    private final ImmutableArray<Tuple<IAbilityType, Double>> tuples;
    private Map<IAbilityType, Double> map = new HashMap<>();

    public AbilitySet(ImmutableArray<Tuple<IAbilityType, Double>> tuples) {
        this.tuples = tuples;
        for (Tuple<IAbilityType, Double> tuple : tuples) {
            map.put(tuple.x, tuple.y);
        }
    }

    @Override
    public ISuppliterator<Tuple<IAbilityType, Double>> getAbilities() {
        return tuples.suppliterator();
    }

    @Override
    public double getAbilityPower(IAbilityType abilityType) {
        return map.getOrDefault(abilityType, 0.0);
    }

}
