package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.CapabilityProviderAdapter
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.castOrNull
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.upperCamelCase
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.multi.ItemMultiMaterial
import miragefairy2019.mod.lib.multi.ItemVariantMaterial
import miragefairy2019.mod.modules.ore.ModuleOre
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.FluidTankProperties
import net.minecraftforge.fluids.capability.IFluidHandlerItem
import net.minecraftforge.oredict.OreIngredient

object Pot {
    lateinit var itemPot: () -> ItemPot
    lateinit var itemFilledBucket: () -> ItemFilledBucket
    val module: Module = {

        // 中身なしポット
        itemPot = item({ ItemPot() }, "pot") {
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

        // 中身入りポット
        itemFilledBucket = item({ ItemFilledBucket() }, "filled_bucket") {
            setUnlocalizedName("filledBucket")
            setCreativeTab { ApiMain.creativeTab }

            itemVariant("miragium_water_pot", {
                ItemVariantFilledBucket(
                    it,
                    "potMiragiumWater",
                    "miragium_water",
                    true
                ) { ModuleOre.blockFluidMiragiumWater.defaultState }
            }, 0) {
                addOreName("potMiragiumWater")
                addOreName("container1000MiragiumWater")
            }
            itemVariant("mirage_flower_extract_pot", {
                ItemVariantFilledBucket(
                    it,
                    "potMirageFlowerExtract",
                    "mirage_flower_extract",
                    true
                ) { ModuleOre.blockFluidMirageFlowerExtract.defaultState }
            }, 1) {
                addOreName("potMirageFlowerExtract")
                addOreName("container1000MirageFlowerExtract")
            }
            itemVariant("mirage_flower_oil_pot", {
                ItemVariantFilledBucket(
                    it,
                    "potMirageFlowerOil",
                    "mirage_flower_oil",
                    true
                ) { ModuleOre.blockFluidMirageFlowerOil.defaultState }
            }, 2) {
                addOreName("potMirageFlowerOil")
                addOreName("container1000MirageFlowerOil")
            }

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }

    }
}

interface IPot {
    fun getFluid(itemStack: ItemStack): Fluid?
}

class ItemPot : Item(), IPot {
    override fun initCapabilities(itemStack: ItemStack, nbt: NBTTagCompound?) = object : CapabilityProviderAdapter() {
        override fun <T> getCapabilityImpl(capability: Capability<T>, facing: EnumFacing?): (() -> T)? = when (capability) {
            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY -> ({ CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(FluidHandlerPot(itemStack)) })
            else -> null
        }
    }

    override fun getFluid(itemStack: ItemStack): Fluid? = null
}

class ItemVariantFilledBucket(
    registryName: String,
    unlocalizedName: String,
    val fluidName: String,
    val vaporizable: Boolean,
    val getFluidBlockState: () -> IBlockState?
) : ItemVariantMaterial(registryName, unlocalizedName)

class ItemFilledBucket : ItemMultiMaterial<ItemVariantFilledBucket>(), IPot {
    override fun hasContainerItem(itemStack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack) = ItemStack(Pot.itemPot.invoke())
    override fun initCapabilities(itemStack: ItemStack, nbt: NBTTagCompound?) = object : CapabilityProviderAdapter() {
        override fun <T> getCapabilityImpl(capability: Capability<T>, facing: EnumFacing?): (() -> T)? = when (capability) {
            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY -> ({ CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(FluidHandlerPot(itemStack)) })
            else -> null
        }
    }

    override fun getFluid(itemStack: ItemStack): Fluid? = FluidRegistry.getFluid(getVariant(itemStack).orNull?.fluidName)
}

class FluidHandlerPot(@JvmField var container: ItemStack) : IFluidHandlerItem {
    private fun getFluid() = container.item.castOrNull<IPot>()?.getFluid(container)
    override fun getContainer() = container
    override fun getTankProperties() = getFluid()?.let { fluid -> arrayOf(FluidTankProperties(FluidStack(fluid, 1000), 1000)) }
    override fun fill(resource: FluidStack, doFill: Boolean): Int {
        if (container.count != 1) return 0 // 複数スタックされている場合は不可
        if (getFluid() != null) return 0 // 中身が入っている場合は不可
        if (resource.amount < 1000) return 0 // 入力が1000に満たない場合は不可
        val result = OreIngredient("pot${resource.fluid.name.upperCamelCase}").matchingStacks.getOrNull(0)?.copy() ?: return 0 // 対応していない場合は不可

        if (doFill) container = result
        return 1000
    }

    override fun drain(resource: FluidStack, doDrain: Boolean): FluidStack? {
        if (container.count != 1) return null // 複数スタックされている場合は不可
        if (resource.amount < 1000) return null // 一括で取り出せない場合は不可
        val fluid = getFluid() ?: return null // 中身が空の場合は不可
        if (resource.fluid != fluid || resource.tag != null) return null // 液体の種類が異なる場合は不可

        if (doDrain) container = Pot.itemPot().createItemStack()
        return FluidStack(fluid, 1000)
    }

    override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
        if (container.count != 1) return null // 複数スタックされている場合は不可
        if (maxDrain < 1000) return null // 一括で取り出せない場合は不可
        val fluid = getFluid() ?: return null // 中身が空の場合は不可

        if (doDrain) container = Pot.itemPot().createItemStack()
        return FluidStack(fluid, 1000)
    }
}
