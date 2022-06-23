package miragefairy2019.mod.recipes

import miragefairy2019.libkt.module

val recipesModule = module {
    fairyMetamorphosisModule(this)
    wandPulverizationRecipesModule(this)
    anvilPulverizationRecipesModule(this)
    fairyDilutionRecipeModule(this)
    fairySummoningRecipeModule(this)
    wandRecipeModule(this)
    Mfa.mfaModule(this)
    fairyCrystalDropLoaderModule(this)
    fairyLogDropLoaderModule(this)
    slabUncraftModule(this)
}
