package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module

val recipesModule = module {
    fairyMetamorphosisModule(this)
    wandPulverizationRecipesModule(this)
    mortarRecipeModule(this)
    fairyDilutionRecipeModule(this)
    fairySummoningRecipeModule(this)
    wandRecipeModule(this)
    Mfa.mfaModule(this)
    fairyCrystalDropLoaderModule(this)
    fairyLogDropLoaderModule(this)
    slabUncraftModule(this)
}
