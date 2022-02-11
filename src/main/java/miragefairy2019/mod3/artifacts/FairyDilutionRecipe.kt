package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.OreIngredientComplex
import miragefairy2019.libkt.ingredient
import miragefairy2019.mod3.fairy.FairyTypes
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreIngredient

object FairyDilutionRecipe {
    val module: Module = {
        onAddRecipe {
            FairyTypes.instance.variants.forEach { fairyVariant ->
                val inputFairyBundle = fairyVariant.y
                val inputFairy = inputFairyBundle.main
                val outputFairyBundle = fairyVariant.y.main.type.parentFairy() ?: return@forEach
                val outputFairy = outputFairyBundle.main
                val rankDiff = (outputFairy.rare - inputFairy.rare).coerceAtLeast(0) // 必要凝縮回数

                val inputMotif = inputFairy.type.motif ?: return@forEach

                GameRegistry.addShapelessRecipe(
                    ResourceLocation("${inputMotif.resourceDomain}:fairy_dilution/${inputMotif.resourcePath}"),
                    null,
                    outputFairy.createItemStack(),
                    OreIngredientComplex("mirageFairy2019CraftingToolFairyWandSummoning"),
                    OreIngredient("container1000MirageFlowerExtract"),
                    inputFairyBundle[rankDiff].createItemStack().ingredient // レベルが上昇するような希釈はできない
                )
            }
        }
    }
}
