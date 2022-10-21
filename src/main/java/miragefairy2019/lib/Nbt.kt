package miragefairy2019.lib

import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTPrimitive
import net.minecraft.nbt.NBTTagByte
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagDouble
import net.minecraft.nbt.NBTTagFloat
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagLong
import net.minecraft.nbt.NBTTagShort

// Api

interface INbtProvider<out T : NBTBase> {
    fun getTagOrNull(): T?
    fun getTagOrCreate(): T
}

interface INbtPath {
    fun getTag(): NBTBase?
    fun setTag(tag: NBTBase)
}


// Helper

operator fun INbtProvider<NBTTagCompound>.get(key: String) = CompoundNbtPath(this, key)

class CompoundNbtPath(private val parent: INbtProvider<NBTTagCompound>, private val key: String) : INbtPath {
    override fun getTag() = parent.getTagOrNull()?.getTag(key)
    override fun setTag(tag: NBTBase) = parent.getTagOrCreate().setTag(key, tag)
    fun removeTag() = parent.getTagOrCreate().removeTag(key)
}

operator fun INbtProvider<NBTTagList>.get(index: Int) = ListNbtPath(this, index)

class ListNbtPath(private val parent: INbtProvider<NBTTagList>, private val index: Int) : INbtPath {
    override fun getTag() = parent.getTagOrNull()?.get(index)
    override fun setTag(tag: NBTBase) = parent.getTagOrCreate().set(index, tag)
}

val INbtPath.compound get() = getTag()?.castOrNull<NBTTagCompound>()
val INbtPath.compoundOrCreate get() = compound ?: NBTTagCompound().also { setCompound(it) }
val INbtPath.list get() = getTag()?.castOrNull<NBTTagList>()
val INbtPath.listOrCreate get() = list ?: NBTTagList().also { setList(it) }
val INbtPath.long get() = getTag()?.castOrNull<NBTPrimitive>()?.long
val INbtPath.int get() = getTag()?.castOrNull<NBTPrimitive>()?.int
val INbtPath.short get() = getTag()?.castOrNull<NBTPrimitive>()?.short
val INbtPath.byte get() = getTag()?.castOrNull<NBTPrimitive>()?.byte
val INbtPath.double get() = getTag()?.castOrNull<NBTPrimitive>()?.double
val INbtPath.float get() = getTag()?.castOrNull<NBTPrimitive>()?.float

fun INbtPath.setCompound(value: NBTTagCompound) = setTag(value)
fun INbtPath.setList(value: NBTTagList) = setTag(value)
fun INbtPath.setLong(value: Long) = setTag(NBTTagLong(value))
fun INbtPath.setInt(value: Int) = setTag(NBTTagInt(value))
fun INbtPath.setShort(value: Short) = setTag(NBTTagShort(value))
fun INbtPath.setByte(value: Byte) = setTag(NBTTagByte(value))
fun INbtPath.setDouble(value: Double) = setTag(NBTTagDouble(value))
fun INbtPath.setFloat(value: Float) = setTag(NBTTagFloat(value))


val INbtPath.compoundNbtProvider: INbtProvider<NBTTagCompound>
    get() = object : INbtProvider<NBTTagCompound> {
        override fun getTagOrNull() = compound
        override fun getTagOrCreate() = compoundOrCreate
    }

val INbtPath.listNbtProvider: INbtProvider<NBTTagList>
    get() = object : INbtProvider<NBTTagList> {
        override fun getTagOrNull() = list
        override fun getTagOrCreate() = listOrCreate
    }

val ItemStack.nbtProvider: INbtProvider<NBTTagCompound>
    get() = object : INbtProvider<NBTTagCompound> {
        override fun getTagOrNull() = if (hasTagCompound()) tagCompound!! else null
        override fun getTagOrCreate() = if (hasTagCompound()) tagCompound!! else NBTTagCompound().also { tagCompound = it }
    }

val NBTTagCompound.nbtProvider: INbtProvider<NBTTagCompound>
    get() = this.let { parent ->
        object : INbtProvider<NBTTagCompound> {
            override fun getTagOrNull() = parent
            override fun getTagOrCreate() = parent
        }
    }

val NBTTagList.nbtProvider: INbtProvider<NBTTagList>
    get() = this.let { parent ->
        object : INbtProvider<NBTTagList> {
            override fun getTagOrNull() = parent
            override fun getTagOrCreate() = parent
        }
    }

operator fun INbtPath.get(key: String) = compoundNbtProvider[key]
operator fun INbtPath.get(index: Int) = listNbtProvider[index]
val INbtPath.tags get() = list?.let { it.indices.map { i -> this[i] } }
operator fun ItemStack.get(key: String) = nbtProvider[key]

val NBTTagList.size get() = tagCount()
val NBTTagList.indices get() = 0 until size
