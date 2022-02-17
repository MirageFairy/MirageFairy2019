package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.BlockInitializer
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.resourceLocation
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object FluidMaterials {
    lateinit var fluidMiragiumWater: () -> Fluid
    lateinit var blockFluidMiragiumWater: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMiragiumWater: () -> ItemBlock

    lateinit var fluidMirageFlowerExtract: () -> Fluid
    lateinit var blockFluidMirageFlowerExtract: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMirageFlowerExtract: () -> ItemBlock

    lateinit var fluidMirageFlowerOil: () -> Fluid
    lateinit var blockFluidMirageFlowerOil: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMirageFlowerOil: () -> ItemBlock
    val module: Module = {

        // ユニバーサルバケツ
        onConstruction { FluidRegistry.enableUniversalBucket() }


        // ミラジウムウォーター
        fluidMiragiumWater = fluid("miragium_water") {
            viscosity = 600
        }
        blockFluidMiragiumWater = block({ BlockFluidMiragiumWater(fluidMiragiumWater()) }, "miragium_water") {
            setUnlocalizedName("miragiumWater")
            setCreativeTab { ApiMain.creativeTab }
            setFluidStateMapper()
        }
        itemFluidMiragiumWater = item({ ItemBlock(blockFluidMiragiumWater()) }, "miragium_water") {
            setCustomModelResourceLocation()
        }


        // ミラージュエキス
        fluidMirageFlowerExtract = fluid("mirage_flower_extract") {
            viscosity = 1000
        }
        blockFluidMirageFlowerExtract = block({ BlockFluidMiragiumWater(fluidMirageFlowerExtract()) }, "mirage_flower_extract") {
            setUnlocalizedName("mirageFlowerExtract")
            setCreativeTab { ApiMain.creativeTab }
            setFluidStateMapper()
        }
        itemFluidMirageFlowerExtract = item({ ItemBlock(blockFluidMirageFlowerExtract()) }, "mirage_flower_extract") {
            setCustomModelResourceLocation()
        }


        // ミラージュオイル
        fluidMirageFlowerOil = fluid("mirage_flower_oil") {
            viscosity = 1500
        }
        blockFluidMirageFlowerOil = block({ BlockFluidMiragiumWater(fluidMirageFlowerOil()) }, "mirage_flower_oil") {
            setUnlocalizedName("mirageFlowerOil")
            setCreativeTab { ApiMain.creativeTab }
            setFluidStateMapper()
        }
        itemFluidMirageFlowerOil = item({ ItemBlock(blockFluidMirageFlowerOil()) }, "mirage_flower_oil") {
            setCustomModelResourceLocation()
        }

    }
}


fun ModInitializer.fluid(name: String, initializer: Fluid.() -> Unit = {}): () -> Fluid {
    lateinit var fluid: Fluid
    onRegisterFluid {
        fluid = Fluid(
            name,
            ResourceLocation(ModMirageFairy2019.MODID, "blocks/${name}_still"),
            ResourceLocation(ModMirageFairy2019.MODID, "blocks/${name}_flow"),
            ResourceLocation(ModMirageFairy2019.MODID, "blocks/${name}_overlay")
        )
        fluid.initializer()
        FluidRegistry.registerFluid(fluid)
        FluidRegistry.addBucketForFluid(fluid)
    }
    return { fluid }
}


@SideOnly(Side.CLIENT)
class FluidStateMapper(val resourceLocation: ResourceLocation) : StateMapperBase() {
    override fun getModelResourceLocation(blockState: IBlockState) = ModelResourceLocation(resourceLocation, "fluid")
}

fun <B : Block> BlockInitializer<B>.setFluidStateMapper() = modInitializer.onRegisterBlock {
    if (ApiMain.side.isClient) ModelLoader.setCustomStateMapper(block, FluidStateMapper(resourceName.resourceLocation))
}


class BlockFluidMiragiumWater(fluid: Fluid) : BlockFluidClassic(fluid, Material.WATER) {
    init {
        setHardness(100.0f)
        setLightOpacity(3)
    }
}