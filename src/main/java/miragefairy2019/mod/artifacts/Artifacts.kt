package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.module

val artifactsModule = module {
    fertilizerModule()
    twinkleStoneModule()
    fairyCollectionBoxModule(this)
    fairyWoodLogModule(this)
    BakedFairy.bakedFairyModule(this)
    FairyCrystalGlass.fairyCrystalGlassModule(this)
    potModule()
    debugItemsModule()
    sphereModule(this)
    fairyStickModule()
    skillBookModule()
    astronomicalObservationBookModule()
    chatWebhookModule(this)
    fairyLogModule(this)
    OreSeed.oreSeedModule(this)
    wandModule()
    fairyCrystalModule(this)
    potionModule()
}
