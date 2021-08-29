package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.api.fairy.relation.IFairyRelationRegistry;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairy.ComponentsAbilityType;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.modkt.api.erg.IErgType;
import miragefairy2019.modkt.api.fairy.FairyTypeEmpty;
import miragefairy2019.modkt.api.fairy.IFairyType;

public class ApiFairy {

    public static IFairyRelationRegistry fairyRelationRegistry;

    public static void init(EventRegistryMod erMod) {
        ModuleFairy.init(erMod);
    }

    public static IComponentAbilityType getComponentAbilityType(IErgType abilityType) {
        return ComponentsAbilityType.getComponentAbilityType(abilityType);
    }

    public static IFairyType empty() {
        return new FairyTypeEmpty();
    }

}
