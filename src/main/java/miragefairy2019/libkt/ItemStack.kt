package miragefairy2019.libkt

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

val ItemStack.orNull get(): ItemStack? = this?.takeIf { !it.isEmpty }

fun Item.getSubItems(creativeTab: CreativeTabs): List<ItemStack> {
    val list = NonNullList.create<ItemStack>()
    this.getSubItems(creativeTab, list)
    return list
}
