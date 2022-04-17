package miragefairy2019.lib

import mirrg.kotlin.castOrNull
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTPrimitive
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagInt

interface INbtCompoundProvider {
    fun getOrNull(): NBTTagCompound?
    fun getOrCreate(): NBTTagCompound
}

class NbtWrapper(private val parent: INbtCompoundProvider, private val key: String) {
    val tag get() = parent.getOrNull()?.getTag(key)
    fun setTag(tag: NBTBase) = parent.getOrCreate().setTag(key, tag)

    val compound get() = tag?.castOrNull<NBTTagCompound>()
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
    val int get() = tag?.castOrNull<NBTPrimitive>()?.int
    fun setCompound(value: NBTTagCompound) = setTag(value)
    fun setInt(value: Int) = setTag(NBTTagInt(value))
}

operator fun NbtWrapper.get(key: String) = NbtWrapper(object : INbtCompoundProvider {
    override fun getOrNull() = compound
    override fun getOrCreate() = compoundOrCreate
}, key)

operator fun ItemStack.get(key: String) = NbtWrapper(object : INbtCompoundProvider {
    override fun getOrNull() = if (hasTagCompound()) tagCompound!! else null
    override fun getOrCreate() = if (hasTagCompound()) tagCompound!! else NBTTagCompound().also { tagCompound = it }
}, key)

operator fun NBTTagCompound.get(key: String) = NbtWrapper(object : INbtCompoundProvider {
    override fun getOrNull() = this@get
    override fun getOrCreate() = this@get
}, key)
