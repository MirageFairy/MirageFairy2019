package miragefairy2019.mod.api.fairy;

import miragefairy2019.modkt.api.fairy.IAbilityType;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;

/**
 * すべてのアビリティの値は非負です。
 */
public interface IAbilitySet {

    public ISuppliterator<Tuple<IAbilityType, Double>> getAbilities();

    public double getAbilityPower(IAbilityType abilityType);

}
