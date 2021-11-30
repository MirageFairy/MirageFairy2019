package miragefairy2019.mod.modules.ore

import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.lib.multi.ItemMultiMaterial
import miragefairy2019.mod.lib.multi.ItemVariantMaterial
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Items
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.FillBucketEvent
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.Optional
import java.util.function.Supplier

private typealias Ivifb = ItemVariantInitializer<ItemFilledBucket, ItemVariantFilledBucket>

private fun Ivifb.registerEmptyBucketFiller(getBlockState: () -> IBlockState) {
    itemInitializer.modInitializer.onRegisterItem {
        MinecraftForge.EVENT_BUS.register(object : Any() {
            @SubscribeEvent
            fun accept(event: FillBucketEvent) {
                if (event.result != Event.Result.DEFAULT) return
                if (event.emptyBucket.item === Items.BUCKET) {
                    if (ItemFilledBucket.tryDrainFluid(event.world, event.entityPlayer, event.emptyBucket, event.target, getBlockState())) {
                        event.filledBucket = itemVariant.createItemStack()
                        event.result = Event.Result.ALLOW
                    }
                }
            }
        })
    }
}


object Ore {
    val module: Module = {

        // マテリアルアイテム
        val itemMaterials = item({ ItemMultiMaterial<ItemVariantMaterial>() }, "materials") {
            setUnlocalizedName("materials")
            setCreativeTab { ApiMain.creativeTab }

            itemVariant({ ItemVariantMaterial("apatite_gem", "gemApatite") }, 0).addOreName("gemApatite")
            itemVariant({ ItemVariantMaterial("fluorite_gem", "gemFluorite") }, 1).addOreName("gemFluorite")
            itemVariant({ ItemVariantMaterial("sulfur_gem", "gemSulfur") }, 2).addOreName("gemSulfur")
            itemVariant({ ItemVariantMaterial("miragium_dust", "dustMiragium") }, 3).addOreName("dustMiragium")
            itemVariant({ ItemVariantMaterial("miragium_tiny_dust", "dustTinyMiragium") }, 4).addOreName("dustTinyMiragium")
            itemVariant({ ItemVariantMaterial("miragium_ingot", "ingotMiragium") }, 5).addOreName("ingotMiragium")
            itemVariant({ ItemVariantMaterial("cinnabar_gem", "gemCinnabar") }, 6).addOreName("gemCinnabar")
            itemVariant({ ItemVariantMaterial("moonstone_gem", "gemMoonstone") }, 7).addOreName("gemMoonstone")
            itemVariant({ ItemVariantMaterial("magnetite_gem", "gemMagnetite") }, 8).addOreName("gemMagnetite")
            itemVariant({ ItemVariantMaterial("saltpeter_gem", "gemSaltpeter") }, 9).addOreName("gemSaltpeter")
            itemVariant({ ItemVariantMaterial("pyrope_gem", "gemPyrope") }, 10).addOreName("gemPyrope")
            itemVariant({ ItemVariantMaterial("smithsonite_gem", "gemSmithsonite") }, 11).addOreName("gemSmithsonite")
            itemVariant({ ItemVariantMaterial("miragium_rod", "rodMiragium") }, 12).addOreName("rodMiragium")
            itemVariant({ ItemVariantMaterial("miragium_nugget", "nuggetMiragium") }, 13).addOreName("nuggetMiragium")
            itemVariant({ ItemVariantMaterial("nephrite_gem", "gemNephrite") }, 14).addOreName("gemNephrite")
            itemVariant({ ItemVariantMaterial("topaz_gem", "gemTopaz") }, 15).addOreName("gemTopaz")
            itemVariant({ ItemVariantMaterial("tourmaline_gem", "gemTourmaline") }, 16).addOreName("gemTourmaline")
            itemVariant({ ItemVariantMaterial("heliolite_gem", "gemHeliolite") }, 17).addOreName("gemHeliolite")
            itemVariant({ ItemVariantMaterial("labradorite_gem", "gemLabradorite") }, 18).addOreName("gemLabradorite")
            itemVariant({ ItemVariantMaterial("lilagium_ingot", "ingotLilagium") }, 19).addOreName("ingotLilagium")

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }

        // 中身入りバケツ
        val itemFilledBucket = item({ ItemFilledBucket() }, "filled_bucket") {
            setUnlocalizedName("filledBucket")
            setCreativeTab { ApiMain.creativeTab }

            itemVariant({
                ItemVariantFilledBucket(
                    "miragium_water_bucket",
                    "bucketMiragiumWater",
                    true
                ) { ModuleOre.blockFluidMiragiumWater.defaultState }
            }, 0) {
                addOreName("bucketMiragiumWater")
                addOreName("container1000MiragiumWater")
                registerEmptyBucketFiller { ModuleOre.blockFluidMiragiumWater.defaultState }
            }
            itemVariant({
                ItemVariantFilledBucket(
                    "mirage_flower_extract_bucket",
                    "bucketMirageFlowerExtract",
                    true
                ) { ModuleOre.blockFluidMirageFlowerExtract.defaultState }
            }, 1) {
                addOreName("bucketMirageFlowerExtract")
                addOreName("container1000MirageFlowerExtract")
                registerEmptyBucketFiller { ModuleOre.blockFluidMirageFlowerExtract.defaultState }
            }
            itemVariant({
                ItemVariantFilledBucket(
                    "mirage_flower_oil_bucket",
                    "bucketMirageFlowerOil",
                    true
                ) { ModuleOre.blockFluidMirageFlowerOil.defaultState }
            }, 2) {
                addOreName("bucketMirageFlowerOil")
                addOreName("container1000MirageFlowerOil")
                registerEmptyBucketFiller { ModuleOre.blockFluidMirageFlowerOil.defaultState }
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
