package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraftforge.oredict.OreDictionary

lateinit var itemFairyCrystal: () -> ItemFairyCrystal
lateinit var variantFairyCrystal: () -> VariantFairyCrystal
lateinit var variantFairyCrystalChristmas: () -> VariantFairyCrystal
lateinit var variantFairyCrystalPure: () -> VariantFairyCrystal

object FairyCrystal {
    val module: Module = {

        // フェアリークリスタル
        itemFairyCrystal = item({ ItemFairyCrystal() }, "fairy_crystal") {
            setUnlocalizedName("fairyCrystal")
            setCreativeTab { ApiMain.creativeTab }
            onRegisterItem {
                variantFairyCrystal = itemVariant("fairy_crystal", { VariantFairyCrystal(it, "fairyCrystal", "mirageFairyCrystal") }, 0)
                variantFairyCrystalChristmas = itemVariant("christmas_fairy_crystal", { VariantFairyCrystal(it, "fairyCrystalChristmas", "mirageFairyCrystalChristmas") }, 1)
                variantFairyCrystalPure = itemVariant("pure_fairy_crystal", { VariantFairyCrystalPure(it, "fairyCrystalPure", "mirageFairyCrystalPure") }, 2)
                if (ApiMain.side.isClient) {
                    item.variants.forEach { item.setCustomModelResourceLocation(it.y!!.registryName, it.x!!) }
                }
            }
            // 鉱石辞書
            onCreateItemStack {
                item.variants.forEach {
                    OreDictionary.registerOre(it.y!!.oreName, it.y!!.createItemStack())
                    OreDictionary.registerOre("mirageFairyCrystalAny", it.y!!.createItemStack())
                }
            }
        }

    }
}
