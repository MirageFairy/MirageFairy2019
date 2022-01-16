package miragefairy2019.mod.modules.ore

import miragefairy2019.libkt.DataItemModel
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.MakeItemVariantModelScope
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.generated
import miragefairy2019.libkt.handheld
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeItemVariantModel
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.multi.ItemMultiMaterial
import miragefairy2019.mod.lib.multi.ItemVariantMaterial
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.Fluid

object Ore {
    val module: Module = {

        // マテリアルアイテム
        val itemMaterials = item({ ItemMultiMaterial<ItemVariantMaterial>() }, "materials") {
            setUnlocalizedName("materials")
            setCreativeTab { ApiMain.creativeTab }

            fun r(
                metadata: Int,
                registryName: String,
                unlocalizedName: String,
                oreName: String,
                modelSupplier: MakeItemVariantModelScope<ItemMultiMaterial<ItemVariantMaterial>, ItemVariantMaterial>.() -> DataItemModel
            ) = itemVariant(registryName, { ItemVariantMaterial(it, unlocalizedName) }, metadata) {
                addOreName(oreName)
                makeItemVariantModel { modelSupplier() }
            }
            r(0, "apatite_gem", "gemApatite", "gemApatite", { generated })
            r(1, "fluorite_gem", "gemFluorite", "gemFluorite", { generated })
            r(2, "sulfur_gem", "gemSulfur", "gemSulfur", { generated })
            r(3, "miragium_dust", "dustMiragium", "dustMiragium", { generated })
            r(4, "miragium_tiny_dust", "dustTinyMiragium", "dustTinyMiragium", { generated })
            r(5, "miragium_ingot", "ingotMiragium", "ingotMiragium", { generated })
            r(6, "cinnabar_gem", "gemCinnabar", "gemCinnabar", { generated })
            r(7, "moonstone_gem", "gemMoonstone", "gemMoonstone", { generated })
            r(8, "magnetite_gem", "gemMagnetite", "gemMagnetite", { generated })
            r(9, "saltpeter_gem", "gemSaltpeter", "gemSaltpeter", { generated })
            r(10, "pyrope_gem", "gemPyrope", "gemPyrope", { generated })
            r(11, "smithsonite_gem", "gemSmithsonite", "gemSmithsonite", { generated })
            r(12, "miragium_rod", "rodMiragium", "rodMiragium", { handheld })
            r(13, "miragium_nugget", "nuggetMiragium", "nuggetMiragium", { generated })
            r(14, "nephrite_gem", "gemNephrite", "gemNephrite", { generated })
            r(15, "topaz_gem", "gemTopaz", "gemTopaz", { generated })
            r(16, "tourmaline_gem", "gemTourmaline", "gemTourmaline", { generated })
            r(17, "heliolite_gem", "gemHeliolite", "gemHeliolite", { generated })
            r(18, "labradorite_gem", "gemLabradorite", "gemLabradorite", { generated })
            r(19, "lilagium_ingot", "ingotLilagium", "ingotLilagium", { generated })
            r(20, "miragium_plate", "plateMiragium", "plateMiragium", { generated })

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }

        // 中身なしポット
        Pot.itemPot = item({ ItemPot() }, "pot") {
            setUnlocalizedName("pot")
            setCreativeTab { ApiMain.creativeTab }
            setCustomModelResourceLocation()
        }
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "pot"),
            DataShapedRecipe(
                pattern = listOf(
                    "   ",
                    "# #",
                    " # "
                ),
                key = mapOf(
                    "#" to DataOreIngredient(ore = "blockMirageFairyCrystal")
                ),
                result = DataResult(
                    item = "${ModMirageFairy2019.MODID}:pot"
                )
            )
        )

        // 中身入りバケツ
        val itemFilledBucket = item({ ItemFilledBucket() }, "filled_bucket") {
            setUnlocalizedName("filledBucket")
            setCreativeTab { ApiMain.creativeTab }

            itemVariant("miragium_water_bucket", {
                ItemVariantFilledBucket(
                    it,
                    "bucketMiragiumWater",
                    true
                ) { ModuleOre.blockFluidMiragiumWater.defaultState }
            }, 0) {
                addOreName("bucketMiragiumWater")
                addOreName("container1000MiragiumWater")
            }
            itemVariant("mirage_flower_extract_bucket", {
                ItemVariantFilledBucket(
                    it,
                    "bucketMirageFlowerExtract",
                    true
                ) { ModuleOre.blockFluidMirageFlowerExtract.defaultState }
            }, 1) {
                addOreName("bucketMirageFlowerExtract")
                addOreName("container1000MirageFlowerExtract")
            }
            itemVariant("mirage_flower_oil_bucket", {
                ItemVariantFilledBucket(
                    it,
                    "bucketMirageFlowerOil",
                    true
                ) { ModuleOre.blockFluidMirageFlowerOil.defaultState }
            }, 2) {
                addOreName("bucketMirageFlowerOil")
                addOreName("container1000MirageFlowerOil")
            }

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }

    }
}

class BlockFluidMiragiumWater(fluid: Fluid) : BlockFluidClassic(fluid, Material.WATER) {
    init {
        setHardness(100.0f)
        setLightOpacity(3)
    }
}

class ItemVariantFilledBucket(registryName: String, unlocalizedName: String, val vaporizable: Boolean, val getFluidBlockState: () -> IBlockState?) : ItemVariantMaterial(registryName, unlocalizedName)
