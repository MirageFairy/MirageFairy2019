package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.OreIngredientComplex
import net.minecraft.init.Items
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

object WandPulverizationRecipes {
    val module: Module = {
        onAddRecipe {

            fun r(registryName: String, input: Ingredient, outputOreName: String) {
                val output = outputOreName.oreIngredient.matchingStacks.firstOrNull() ?: return
                GameRegistry.addShapelessRecipe(
                    ResourceLocation("${ModMirageFairy2019.MODID}:wand_pulverization/$registryName"),
                    null,
                    output,
                    OreIngredientComplex("mirageFairy2019CraftingToolFairyWandBreaking"),
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

        }
    }
}
