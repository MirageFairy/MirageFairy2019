package miragefairy2019.lib

import miragefairy2019.libkt.unit
import mirrg.kotlin.castOrNull
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTPrimitive
import net.minecraft.nbt.NBTTagCompound

interface INbtCompoundProvider {
    fun getOrNull(): NBTTagCompound?
    fun getOrCreate(): NBTTagCompound
}

class NbtWrapper(private val parent: INbtCompoundProvider, private val key: String) {
    val compound get() = parent.getOrNull()?.getTag(key)?.castOrNull<NBTTagCompound>()
    val compoundOrCreate: NBTTagCompound
        get() {
            val parentTag = parent.getOrCreate()
            val tag = parentTag.getTag(key)
            return if (tag is NBTTagCompound) {
                tag
            } else {
                val newTag = NBTTagCompound()
                parentTag.setTag(key, newTag)
                newTag
            }
        }
    val int get() = parent.getOrNull()?.getTag(key)?.castOrNull<NBTPrimitive>()?.int
    fun setCompound(value: NBTTagCompound) = unit { parent.getOrCreate().setTag(key, value) }
    fun setInt(value: Int) = unit { parent.getOrCreate().setInteger(key, value) }

    operator fun get(key: String) = NbtWrapper(object : INbtCompoundProvider {
        override fun getOrNull() = compound
        override fun getOrCreate() = compoundOrCreate
    }, key)
}

operator fun ItemStack.get(key: String) = NbtWrapper(object : INbtCompoundProvider {
    override fun getOrNull() = if (hasTagCompound()) tagCompound!! else null
    override fun getOrCreate() = if (hasTagCompound()) tagCompound!! else NBTTagCompound().also { tagCompound = it }
}, key)
