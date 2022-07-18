package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module

val recipesModule = module {
    fairyMetamorphosisRecipeModule()
    mortarRecipeModule()
    fairyDilutionRecipeModule()
    fairySummoningRecipeModule()
    Mfa.mfaModule(this)
    fairyCrystalDropLoaderModule()
    fairyLogDropLoaderModule()
    slabUncraftRecipeModule()
    miniaRecipe(this)
    fairyTamingRecipeModule()
}
