package miragefairy2019.mod.modules.ore

import miragefairy2019.libkt.CapabilityProviderAdapter
import miragefairy2019.libkt.castOrNull
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.upperCamelCase
import miragefairy2019.mod.lib.multi.ItemMultiMaterial
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

class ItemFilledBucket : ItemMultiMaterial<ItemVariantFilledBucket>(), IPot {
    override fun hasContainerItem(itemStack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack) = ItemStack(Pot.itemPot.invoke())
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
