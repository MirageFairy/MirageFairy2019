package miragefairy2019.mod.recipes

import miragefairy2019.api.IMortarRecipeHandler
import miragefairy2019.api.MortarRecipeRegistry
import miragefairy2019.common.toOreName
import miragefairy2019.lib.MortarRecipeHandler
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.oreIngredient
import mirrg.kotlin.hydrogen.toUpperCamelCase
import mirrg.kotlin.hydrogen.unit

val mortarRecipeModule = module {
    onAddRecipe {
        fun r(recipe: IMortarRecipeHandler) = unit { MortarRecipeRegistry.mortarRecipeHandlers += recipe }
        fun r(level: Int, inputOreName: String, outputOreName: String) = r(MortarRecipeHandler(level, inputOreName.oreIngredient) { outputOreName.toOreName().copyItemStack() })
        fun r(level: Int, materialName: String) = r(level, "gem${materialName.toUpperCamelCase()}", "dust${materialName.toUpperCamelCase()}")

        r(1, "coal")
        r(1, "charcoal")
        r(4, "diamond")
        r(2, "lapis")
        r(3, "emerald")
        r(2, "quartz")
        r(1, "enderpearl", "dustEnderPearl")

        r(1, "apatite")
        r(1, "fluorite")
        r(1, "sulfur")
        r(1, "cinnabar")
        r(2, "moonstone")
        r(1, "magnetite")

        r(1, "saltpeter")
        r(3, "pyrope")
        r(1, "smithsonite")
        r(2, "nephrite")
        r(4, "topaz")
        r(3, "tourmaline")
        r(2, "heliolite")
        r(2, "labradorite")
        r(3, "peridot")
        r(4, "ruby")
        r(4, "sapphire")

        r(2, "crystalCertusQuartz", "dustCertusQuartz")
        r(2, "crystalFluix", "dustFluix")

    }
}
