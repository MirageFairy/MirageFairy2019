package miragefairy2019.lib

import miragefairy2019.api.IFairyItem
import mirrg.kotlin.castOrNull
import net.minecraft.item.ItemStack

val ItemStack.fairyType get() = item.castOrNull<IFairyItem>()?.getMirageFairy(this)
