package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.module

val artifactsModule = module {
    dishModule(this)
    Fertilizer.fertilizerModule(this)
    TwinkleStone.twinkleStoneModule(this)
    FairyCollectionBox.fairyCollectionBoxModule(this)
    FairyWoodLog.fairyWoodLogModule(this)
    BakedFairy.bakedFairyModule(this)
    FairyCrystalGlass.fairyCrystalGlassModule(this)
    Pot.potModule(this)
    debugItemsModule(this)
    Sphere.sphereModule(this)
    FairyMaterials.fairyMaterialsModule(this)
    fairyStickModule(this)
    SkillBook.skillBookModule(this)
    astronomicalObservationBookModule(this)
    ChatWebhook.chatWebhookModule(this)
    FairyLog.fairyLogModule(this)
    MirageFlower.mirageFlowerModule(this)
    FluidMaterials.fluidMaterialsModule(this)
    OreSeed.oreSeedModule(this)
    wandModule(this)
    FairyCrystal.fairyCrystalModule(this)
}
