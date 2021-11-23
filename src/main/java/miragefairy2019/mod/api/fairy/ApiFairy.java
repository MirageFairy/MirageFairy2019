package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.api.fairy.relation.IFairyRelationRegistry;
import miragefairy2019.mod.modules.fairy.ComponentsAbilityType;
import miragefairy2019.mod3.erg.api.IErgType;
import miragefairy2019.modkt.api.fairy.IFairyType;
import miragefairy2019.modkt.impl.fairy.FairyTypeEmpty;

public class ApiFairy {

    public static IFairyRelationRegistry fairyRelationRegistry;

    public static IComponentAbilityType getComponentAbilityType(IErgType abilityType) {
        return ComponentsAbilityType.getComponentAbilityType(abilityType);
    }

    public static IFairyType empty() {
        return new FairyTypeEmpty();
    }

}
