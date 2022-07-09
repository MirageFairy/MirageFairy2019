package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.ingredient
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredient
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.createItemStack
import miragefairy2019.mod.fairy.getVariant
import mirrg.kotlin.hydrogen.atLeast
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreIngredient

val fairyDilutionRecipeModule = module {
    onAddRecipe {
        FairyCard.values().forEach { inputFairyCard ->
            val inputFairyCard = inputFairyCard
            val inputFairyVariant = inputFairyCard.getVariant(1)
            val outputFairyCard = inputFairyCard.parentFairy() ?: return@forEach
            val outputFairyVariant = outputFairyCard.getVariant(1)
            val rankDiff = (outputFairyCard.rare - inputFairyCard.rare) atLeast 0 // 必要凝縮回数

            val inputMotif = inputFairyVariant.type.motif ?: return@forEach

            GameRegistry.addShapelessRecipe(
                ResourceLocation("${inputMotif.resourceDomain}:fairy_dilution/${inputMotif.resourcePath}"),
                null,
                outputFairyVariant.createItemStack(),
                WandType.SUMMONING.ingredient,
                OreIngredient("container1000MirageFlowerExtract"),
                inputFairyCard.createItemStack(1 + rankDiff).ingredient // レベルが上昇するような希釈はできない
            )
        }
    }
}
