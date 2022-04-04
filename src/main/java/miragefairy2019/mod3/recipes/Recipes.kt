package miragefairy2019.mod3.recipes

import miragefairy2019.libkt.module

object Recipes {
    val module = module {
        FairyMetamorphosis.module(this)
        WandPulverizationRecipes.module(this)
        AnvilPulverizationRecipes.module(this)
        FairyDilutionRecipe.module(this)
        FairySummoningRecipe.module(this)
        WandRecipe.module(this)
        Mfa.module(this)
    }
}
