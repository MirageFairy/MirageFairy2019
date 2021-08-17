package miragefairy2019.libkt

import net.minecraft.item.ItemStack

val ItemStack?.orNull get(): ItemStack? = this?.takeIf { !it.isEmpty }
