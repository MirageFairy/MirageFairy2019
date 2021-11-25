package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.api.fairy.relation.IFairyRelationRegistry;
import miragefairy2019.mod.modules.fairy.ComponentsAbilityType;
import miragefairy2019.mod3.erg.api.EnumErgType;
import miragefairy2019.mod3.fairy.api.IFairyType;
import miragefairy2019.modkt.impl.fairy.FairyTypeEmpty;

public class ApiFairy {

    public static IFairyRelationRegistry fairyRelationRegistry;

    public static IComponentAbilityType getComponentAbilityType(EnumErgType abilityType) {
        return ComponentsAbilityType.getComponentAbilityType(abilityType);
    }

    public static IFairyType empty() {
        return new FairyTypeEmpty();
    }

}
