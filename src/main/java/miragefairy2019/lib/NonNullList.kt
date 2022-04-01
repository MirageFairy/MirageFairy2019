package miragefairy2019.lib

import net.minecraft.util.NonNullList

fun <T> Iterable<T>.toNonNullList(): NonNullList<T> = NonNullList.create<T>().also { this.forEach { item -> it.add(item) } }
