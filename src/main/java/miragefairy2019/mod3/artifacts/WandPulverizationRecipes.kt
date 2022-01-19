package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.OreIngredientComplex
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreIngredient

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
            r("miragium_dust", OreIngredient("ingotMiragium"), "dustMiragium")

            // 他MODによるバニラ素材の粉
            r("iron_dust", OreIngredient("ingotIron"), "dustIron")
            r("gold_dust", OreIngredient("ingotGold"), "dustGold")
            r("quartz_dust", OreIngredient("gemQuartz"), "dustQuartz")
            r("wheat_dust", OreIngredient("cropWheat"), "dustWheat")
            r("ender_pearl_dust", OreIngredient("enderpearl"), "dustEnderPearl")

        }
    }
}
