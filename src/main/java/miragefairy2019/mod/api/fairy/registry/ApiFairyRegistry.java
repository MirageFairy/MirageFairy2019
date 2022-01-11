package miragefairy2019.mod.api.fairy.registry;

import miragefairy2019.mod.modules.fairy.registry.FairyRegistry;

public class ApiFairyRegistry {

    public static IFairyRegistry getFairyRegistry() {
        return FairyRegistry.instance;
    }

}
