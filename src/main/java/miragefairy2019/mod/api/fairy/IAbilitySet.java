package miragefairy2019.mod.api.fairy;

import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

public interface IAbilitySet
{

	public ISuppliterator<Tuple<IAbilityType, Double>> getAbilities();

	public double getAbilityPower(IAbilityType abilityType);

}
