package miragefairy2019.libkt

import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider

abstract class CapabilityProviderAdapter : ICapabilityProvider {
    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?) = getCapabilityImpl(capability, facing) != null
    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?) = getCapabilityImpl(capability, facing)?.invoke()
    abstract fun <T> getCapabilityImpl(capability: Capability<T>, facing: EnumFacing?): (() -> T)?
}
