package miragefairy2019.lib

import miragefairy2019.api.Erg
import miragefairy2019.api.ErgSet
import miragefairy2019.api.IFairyItem
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.libkt.textComponent
import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

val EMPTY_FAIRY = object : IFairySpec {
    override fun isEmpty() = true
    override fun getMotif(): ResourceLocation? = null
    override fun getDisplayName() = textComponent { "Empty"() } // TODO translate
    override fun getColor() = 0xFFFFFF
    override fun getCost() = 50.0
    override fun getManaSet() = ManaSet.ZERO
    override fun getErgSet() = ErgSet.ZERO
}

val ItemStack.fairySpec get() = this.item.castOrNull<IFairyItem>()?.getMirageFairy(this)

fun IFairySpec.mana(mana: Mana) = manaSet[mana]
fun IFairySpec.erg(erg: Erg) = ergSet[erg]
