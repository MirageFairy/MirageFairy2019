package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.module

val artifactsModule = module {
    fertilizerModule()
    twinkleStoneModule()
    FairyCollectionBox.fairyCollectionBoxModule(this)
    FairyWoodLog.fairyWoodLogModule(this)
    BakedFairy.bakedFairyModule(this)
    FairyCrystalGlass.fairyCrystalGlassModule(this)
    potModule()
    debugItemsModule()
    Sphere.sphereModule(this)
    fairyMaterialsModule()
    fairyStickModule()
    skillBookModule()
    astronomicalObservationBookModule()
    ChatWebhook.chatWebhookModule(this)
    FairyLog.fairyLogModule(this)
    FluidMaterials.fluidMaterialsModule(this)
    OreSeed.oreSeedModule(this)
    wandModule()
    FairyCrystal.fairyCrystalModule(this)
}
