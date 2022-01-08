package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.oredict.OreDictionary
import java.util.function.Consumer

object ModuleFairyCrystal {
    lateinit var itemFairyCrystal: ItemFairyCrystal
    lateinit var variantFairyCrystal: VariantFairyCrystal
    lateinit var variantFairyCrystalChristmas: VariantFairyCrystal
    lateinit var variantFairyCrystalPure: VariantFairyCrystal
    fun init(erMod: EventRegistryMod) {
        erMod.registerItem.register(Consumer {

            // 妖晶のアイテム登録
            run {
                val item = ItemFairyCrystal()
                item.setRegistryName(ModMirageFairy2019.MODID, "fairy_crystal")
                item.unlocalizedName = "fairyCrystal"
                item.creativeTab = ApiMain.creativeTab
                item.registerVariant(0, VariantFairyCrystal("fairy_crystal", "fairyCrystal", "mirageFairyCrystal").also { variantFairyCrystal = it })
                item.registerVariant(1, VariantFairyCrystal("christmas_fairy_crystal", "fairyCrystalChristmas", "mirageFairyCrystalChristmas").also { variantFairyCrystalChristmas = it })
                item.registerVariant(2, VariantFairyCrystalPure("pure_fairy_crystal", "fairyCrystalPure", "mirageFairyCrystalPure").also { variantFairyCrystalPure = it })
                ForgeRegistries.ITEMS.register(item)
                if (ApiMain.side.isClient) {
                    item.variants.forEach {
                        ModelLoader.setCustomModelResourceLocation(
                            item,
                            it.x!!,
                            ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, it.y!!.registryName), null)
                        )
                    }
                }
                itemFairyCrystal = item
            }

        })
        erMod.createItemStack.register(Consumer {

            // 鉱石辞書
            itemFairyCrystal.variants.forEach {
                OreDictionary.registerOre(it.y!!.oreName, it.y!!.createItemStack())
                OreDictionary.registerOre("mirageFairyCrystalAny", it.y!!.createItemStack())
            }

        })
    }
}
