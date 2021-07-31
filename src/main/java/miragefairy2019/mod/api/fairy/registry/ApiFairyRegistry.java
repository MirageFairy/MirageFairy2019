package miragefairy2019.mod.api.fairy.registry;

import miragefairy2019.mod.modules.fairy.registry.FairyRegistry;
import miragefairy2019.mod.modules.fairy.registry.FairyRelationRegistry;

public class ApiFairyRegistry {

    public static IFairyRegistry getFairyRegistry() {
        return FairyRegistry.instance;
    }

    public static IFairyRelationRegistry getFairyRelationRegistry() {
        return FairyRelationRegistry.instance;
    }

}
