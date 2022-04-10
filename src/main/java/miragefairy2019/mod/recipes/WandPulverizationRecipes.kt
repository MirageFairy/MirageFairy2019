package miragefairy2019.mod.recipes

import miragefairy2019.libkt.module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.WandType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

object WandPulverizationRecipes {
    val module = module {
        onAddRecipe {

            fun r(registryName: String, input: Ingredient, outputOreName: String) {
                val output = outputOreName.oreIngredient.matchingStacks.firstOrNull() ?: return
                GameRegistry.addShapelessRecipe(
                    ResourceLocation("${ModMirageFairy2019.MODID}:wand_pulverization/$registryName"),
                    null,
                    output,
                    WandType.BREAKING.ingredient,
                    input
                )
            }

            // 妖精MODによる粉
            r("miragium_dust", "ingotMiragium".oreIngredient, "dustMiragium")

            // 他MODによるバニラ素材の粉
            r("iron_dust", "ingotIron".oreIngredient, "dustIron")
            r("gold_dust", "ingotGold".oreIngredient, "dustGold")
            r("quartz_dust", "gemQuartz".oreIngredient, "dustQuartz")
            r("wheat_dust", "cropWheat".oreIngredient, "dustWheat")
            r("ender_pearl_dust", "enderpearl".oreIngredient, "dustEnderPearl")

            // 他MODの素材の粉
            r("certus_quartz_dust", "crystalCertusQuartz".oreIngredient, "dustCertusQuartz")
            r("fluix_dist", "crystalFluix".oreIngredient, "dustFluix")

        }
    }
}
