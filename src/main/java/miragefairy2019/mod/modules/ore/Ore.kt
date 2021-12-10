package miragefairy2019.mod.modules.ore

import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.generated
import miragefairy2019.libkt.handheld
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeItemVariantModel
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

            itemVariant("apatite_gem", { ItemVariantMaterial(it, "gemApatite") }, 0).apply { addOreName("gemApatite") }.apply { makeItemVariantModel { generated } }
            itemVariant("fluorite_gem", { ItemVariantMaterial(it, "gemFluorite") }, 1).apply { addOreName("gemFluorite") }.apply { makeItemVariantModel { generated } }
            itemVariant("sulfur_gem", { ItemVariantMaterial(it, "gemSulfur") }, 2).apply { addOreName("gemSulfur") }.apply { makeItemVariantModel { generated } }
            itemVariant("miragium_dust", { ItemVariantMaterial(it, "dustMiragium") }, 3).apply { addOreName("dustMiragium") }.apply { makeItemVariantModel { generated } }
            itemVariant("miragium_tiny_dust", { ItemVariantMaterial(it, "dustTinyMiragium") }, 4).apply { addOreName("dustTinyMiragium") }.apply { makeItemVariantModel { generated } }
            itemVariant("miragium_ingot", { ItemVariantMaterial(it, "ingotMiragium") }, 5).apply { addOreName("ingotMiragium") }.apply { makeItemVariantModel { generated } }
            itemVariant("cinnabar_gem", { ItemVariantMaterial(it, "gemCinnabar") }, 6).apply { addOreName("gemCinnabar") }.apply { makeItemVariantModel { generated } }
            itemVariant("moonstone_gem", { ItemVariantMaterial(it, "gemMoonstone") }, 7).apply { addOreName("gemMoonstone") }.apply { makeItemVariantModel { generated } }
            itemVariant("magnetite_gem", { ItemVariantMaterial(it, "gemMagnetite") }, 8).apply { addOreName("gemMagnetite") }.apply { makeItemVariantModel { generated } }
            itemVariant("saltpeter_gem", { ItemVariantMaterial(it, "gemSaltpeter") }, 9).apply { addOreName("gemSaltpeter") }.apply { makeItemVariantModel { generated } }
            itemVariant("pyrope_gem", { ItemVariantMaterial(it, "gemPyrope") }, 10).apply { addOreName("gemPyrope") }.apply { makeItemVariantModel { generated } }
            itemVariant("smithsonite_gem", { ItemVariantMaterial(it, "gemSmithsonite") }, 11).apply { addOreName("gemSmithsonite") }.apply { makeItemVariantModel { generated } }
            itemVariant("miragium_rod", { ItemVariantMaterial(it, "rodMiragium") }, 12).apply { addOreName("rodMiragium") }.apply { makeItemVariantModel { handheld } }
            itemVariant("miragium_nugget", { ItemVariantMaterial(it, "nuggetMiragium") }, 13).apply { addOreName("nuggetMiragium") }.apply { makeItemVariantModel { generated } }
            itemVariant("nephrite_gem", { ItemVariantMaterial(it, "gemNephrite") }, 14).apply { addOreName("gemNephrite") }.apply { makeItemVariantModel { generated } }
            itemVariant("topaz_gem", { ItemVariantMaterial(it, "gemTopaz") }, 15).apply { addOreName("gemTopaz") }.apply { makeItemVariantModel { generated } }
            itemVariant("tourmaline_gem", { ItemVariantMaterial(it, "gemTourmaline") }, 16).apply { addOreName("gemTourmaline") }.apply { makeItemVariantModel { generated } }
            itemVariant("heliolite_gem", { ItemVariantMaterial(it, "gemHeliolite") }, 17).apply { addOreName("gemHeliolite") }.apply { makeItemVariantModel { generated } }
            itemVariant("labradorite_gem", { ItemVariantMaterial(it, "gemLabradorite") }, 18).apply { addOreName("gemLabradorite") }.apply { makeItemVariantModel { generated } }
            itemVariant("lilagium_ingot", { ItemVariantMaterial(it, "ingotLilagium") }, 19).apply { addOreName("ingotLilagium") }.apply { makeItemVariantModel { generated } }
            itemVariant("miragium_plate", { ItemVariantMaterial(it, "plateMiragium") }, 20).apply { addOreName("plateMiragium") }.apply { makeItemVariantModel { generated } }

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }

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
                registerEmptyBucketFiller { ModuleOre.blockFluidMiragiumWater.defaultState }
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
                registerEmptyBucketFiller { ModuleOre.blockFluidMirageFlowerExtract.defaultState }
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
