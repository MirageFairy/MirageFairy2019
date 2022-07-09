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
        FairyCard.values().forEach { fairyCard ->
            val inputFairyBundle = fairyCard
            val inputFairy = inputFairyBundle.getVariant(1)
            val outputFairyBundle = fairyCard.getVariant(1).type.parentFairy() ?: return@forEach
            val outputFairy = outputFairyBundle.getVariant(1)
            val rankDiff = (outputFairy.rare - inputFairy.rare) atLeast 0 // 必要凝縮回数

            val inputMotif = inputFairy.type.motif ?: return@forEach

            GameRegistry.addShapelessRecipe(
                ResourceLocation("${inputMotif.resourceDomain}:fairy_dilution/${inputMotif.resourcePath}"),
                null,
                outputFairy.createItemStack(),
                WandType.SUMMONING.ingredient,
                OreIngredient("container1000MirageFlowerExtract"),
                inputFairyBundle.createItemStack(1 + rankDiff).ingredient // レベルが上昇するような希釈はできない
            )
        }
    }
}
