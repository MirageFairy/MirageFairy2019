package miragefairy2019.lib

import miragefairy2019.api.IFairyItem
import miragefairy2019.libkt.orNull
import net.minecraft.item.ItemStack

val ItemStack.fairyType get() = (item as? IFairyItem)?.getMirageFairy(this)?.orNull
