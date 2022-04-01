package miragefairy2019.lib

import miragefairy2019.api.ErgSet
import miragefairy2019.api.IFairyItem
import miragefairy2019.api.IFairyType
import miragefairy2019.api.ManaSet
import miragefairy2019.libkt.textComponent
import mirrg.kotlin.castOrNull
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

val EMPTY_FAIRY = object : IFairyType {
    override fun isEmpty() = true
    override fun getMotif(): ResourceLocation? = null
    override fun getDisplayName() = textComponent { "Empty"() } // TODO translate
    override fun getColor() = 0xFFFFFF
    override fun getCost() = 50.0
    override fun getManaSet() = ManaSet.ZERO
    override fun getErgSet() = ErgSet.ZERO
}

val ItemStack.fairyType get() = item.castOrNull<IFairyItem>()?.getMirageFairy(this)
