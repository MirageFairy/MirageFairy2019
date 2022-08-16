package miragefairy2019.lib

import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.IItemHandlerModifiable

fun <T> TileEntity.getCapabilityIfHas(capability: Capability<T>, facing: EnumFacing?) = if (this.hasCapability(capability, facing)) this.getCapability(capability, facing) else null

operator fun IItemHandler.get(index: Int) = this.getStackInSlot(index)
operator fun IItemHandlerModifiable.set(index: Int, itemStack: ItemStack) = this.setStackInSlot(index, itemStack)
val IItemHandler.size get() = this.slots
val IItemHandler.indices get() = 0 until this.size
val IItemHandler.itemStacks get() = (0 until this.size).map { this[it] }
