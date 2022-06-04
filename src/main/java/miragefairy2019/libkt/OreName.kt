package miragefairy2019.libkt

import miragefairy2019.common.OreName
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

fun OreName.getItemStacks(): List<ItemStack> = OreDictionary.getOres(this.string)
fun OreName.getItemStack() = getItemStacks().firstOrNull()
fun OreName.copyItemStack(count: Int = 1) = getItemStack()?.copy(count)
