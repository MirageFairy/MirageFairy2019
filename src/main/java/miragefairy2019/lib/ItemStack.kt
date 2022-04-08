package miragefairy2019.lib

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

fun NBTTagCompound.toItemStack() = ItemStack(this)
fun ItemStack.toNbt() = NBTTagCompound().also { writeToNBT(it) }
