package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.util.ResourceLocation
import net.minecraftforge.oredict.OreDictionary

lateinit var itemFairyCrystal: () -> ItemFairyCrystal
lateinit var variantFairyCrystal: () -> VariantFairyCrystal
lateinit var variantFairyCrystalChristmas: () -> VariantFairyCrystal
lateinit var variantFairyCrystalPure: () -> VariantFairyCrystal

object FairyCrystal {
    val module = module {

        // フェアリークリスタル
        itemFairyCrystal = item({ ItemFairyCrystal() }, "fairy_crystal") {
            setUnlocalizedName("fairyCrystal")
            setCreativeTab { ApiMain.creativeTab }
            variantFairyCrystal = itemVariant("fairy_crystal", { VariantFairyCrystal(it, "fairyCrystal", "mirageFairyCrystal") }, 0)
            variantFairyCrystalChristmas = itemVariant("christmas_fairy_crystal", { VariantFairyCrystal(it, "fairyCrystalChristmas", "mirageFairyCrystalChristmas") }, 1)
            variantFairyCrystalPure = itemVariant("pure_fairy_crystal", { VariantFairyCrystalPure(it, "fairyCrystalPure", "mirageFairyCrystalPure") }, 2)
            onRegisterItem {
                if (ApiMain.side.isClient) {
                    item.variants.forEach { item.setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.registryName)) }
                }
            }
            onCreateItemStack {
                item.variants.forEach {
                    OreDictionary.registerOre(it.oreName, it.createItemStack())
                    OreDictionary.registerOre("mirageFairyCrystalAny", it.createItemStack())
                }
            }
        }

        // 高純度フェアリークリスタル
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "pure_fairy_crystal"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "blockMirageFairyCrystal"),
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandPolishing")
                ),
                result = DataResult(
                    item = "miragefairy2019:fairy_crystal",
                    data = 2
                )
            )
        )

    }
}
