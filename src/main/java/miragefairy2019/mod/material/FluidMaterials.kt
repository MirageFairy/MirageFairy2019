package miragefairy2019.mod.material

import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setFluidStateMapper
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.fluid
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRecipe
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreIngredient

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

    lateinit var fluidMirageFairyBlood: () -> Fluid
    lateinit var blockFluidMirageFairyBlood: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMirageFairyBlood: () -> ItemBlock

    val fluidMaterialsModule = module {

        // ユニバーサルバケツ
        onConstruction { FluidRegistry.enableUniversalBucket() }
        lang("item.forge.bucketFilled.name", null, "%s入りバケツ")

        // ミラジウムウォーター
        run {
            fluidMiragiumWater = fluid("miragium_water") {
                viscosity = 600
            }
            blockFluidMiragiumWater = block({ BlockFluidMiragiumWater(fluidMiragiumWater()) }, "miragium_water") {
                setUnlocalizedName("miragiumWater")
                setCreativeTab { Main.creativeTab }
                setFluidStateMapper()
                makeBlockStates { fluid }
            }
            itemFluidMiragiumWater = item({ ItemBlock(blockFluidMiragiumWater()) }, "miragium_water") {
                setCustomModelResourceLocation()
                makeItemModel { fluid }
            }
            onMakeLang { enJa("fluid.miragium_water", "Miragium Water", "ミラジウムウォーター") }
            onMakeLang { enJa("tile.miragiumWater.name", "Miragium Water", "ミラジウムウォーター") }

            // 作業台クラフト
            makeRecipe("miragium_water_pot") {
                DataShapelessRecipe(
                    ingredients = listOf(
                        DataOreIngredient(ore = "mirageFairyPot"),
                        DataOreIngredient(ore = "container1000Water"),
                        DataOreIngredient(ore = "dustMiragium")
                    ),
                    result = DataResult(item = "miragefairy2019:filled_bucket", data = 0)
                )
            }

            // フェアリーステッキクラフト
            onAddRecipe {
                ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                    it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                    it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.WATER.defaultState }, { blockFluidMiragiumWater().defaultState })
                    it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustMiragium"))
                })
            }

        }

        // ミラージュエキス
        run {
            fluidMirageFlowerExtract = fluid("mirage_flower_extract") {
                viscosity = 1000
            }
            blockFluidMirageFlowerExtract = block({ BlockFluidMiragiumWater(fluidMirageFlowerExtract()) }, "mirage_flower_extract") {
                setUnlocalizedName("mirageFlowerExtract")
                setCreativeTab { Main.creativeTab }
                setFluidStateMapper()
                makeBlockStates { fluid }
            }
            itemFluidMirageFlowerExtract = item({ ItemBlock(blockFluidMirageFlowerExtract()) }, "mirage_flower_extract") {
                setCustomModelResourceLocation()
                makeItemModel { fluid }
            }
            onMakeLang { enJa("fluid.mirage_flower_extract", "Mirage Extract", "ミラージュエキス") }
            onMakeLang { enJa("tile.mirageFlowerExtract.name", "Mirage Extract", "ミラージュエキス") }
        }

        // ミラージュオイル
        run {
            fluidMirageFlowerOil = fluid("mirage_flower_oil") {
                viscosity = 1500
            }
            blockFluidMirageFlowerOil = block({ BlockFluidMiragiumWater(fluidMirageFlowerOil()) }, "mirage_flower_oil") {
                setUnlocalizedName("mirageFlowerOil")
                setCreativeTab { Main.creativeTab }
                setFluidStateMapper()
                makeBlockStates { fluid }
            }
            itemFluidMirageFlowerOil = item({ ItemBlock(blockFluidMirageFlowerOil()) }, "mirage_flower_oil") {
                setCustomModelResourceLocation()
                makeItemModel { fluid }
            }
            onMakeLang { enJa("fluid.mirage_flower_oil", "Mirage Oil", "ミラージュオイル") }
            onMakeLang { enJa("tile.mirageFlowerOil.name", "Mirage Oil", "ミラージュオイル") }
        }

        // 妖精の血
        run {
            fluidMirageFairyBlood = fluid("mirage_fairy_blood") {
                viscosity = 1000
            }
            blockFluidMirageFairyBlood = block({ BlockFluidMiragiumWater(fluidMirageFairyBlood()) }, "mirage_fairy_blood") {
                setUnlocalizedName("mirageFairyBlood")
                setCreativeTab { Main.creativeTab }
                setFluidStateMapper()
                makeBlockStates { fluid }
            }
            itemFluidMirageFairyBlood = item({ ItemBlock(blockFluidMirageFairyBlood()) }, "mirage_fairy_blood") {
                setCustomModelResourceLocation()
                makeItemModel { fluid }
            }
            onMakeLang { enJa("fluid.mirage_fairy_blood", "Mirage Fairy Blood", "妖精の血") }
            onMakeLang { enJa("tile.mirageFairyBlood.name", "Mirage Fairy Blood", "妖精の血") }
        }

    }
}


fun ModScope.fluid(name: String, initializer: Fluid.() -> Unit = {}): () -> Fluid {
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


class BlockFluidMiragiumWater(fluid: Fluid) : BlockFluidClassic(fluid, Material.WATER) {
    init {
        setHardness(100.0f)
        setLightOpacity(3)
    }
}
