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
    override fun getBaseManaSet() = ManaSet.ZERO
    override fun getManaSet() = ManaSet.ZERO
    override fun getErgSet() = ErgSet.ZERO
}

val ItemStack.fairySpec get() = this.item.castOrNull<IFairyItem>()?.getMirageFairy(this)

fun IFairySpec.mana(mana: Mana) = manaSet[mana]
fun IFairySpec.erg(erg: Erg) = ergSet[erg]

val IFairySpec.shineEfficiency get() = baseManaSet.shine
val IFairySpec.fireEfficiency get() = baseManaSet.fire
val IFairySpec.windEfficiency get() = baseManaSet.wind
val IFairySpec.gaiaEfficiency get() = baseManaSet.gaia
val IFairySpec.aquaEfficiency get() = baseManaSet.aqua
val IFairySpec.darkEfficiency get() = baseManaSet.dark
