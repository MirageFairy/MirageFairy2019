package miragefairy2019.lib

import miragefairy2019.api.Erg
import miragefairy2019.api.ErgSet
import miragefairy2019.api.IFairyItem
import miragefairy2019.api.IFairyType
import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.libkt.textComponent
import mirrg.kotlin.hydrogen.castOrNull
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

val ItemStack.fairyType get() = this.item.castOrNull<IFairyItem>()?.getMirageFairy(this)

fun IFairyType.mana(mana: Mana) = manaSet[mana]
fun IFairyType.erg(erg: Erg) = ergSet[erg]

val IFairyType.shineEfficiency get() = manaSet.shine / (cost / 50.0)
val IFairyType.fireEfficiency get() = manaSet.fire / (cost / 50.0)
val IFairyType.windEfficiency get() = manaSet.wind / (cost / 50.0)
val IFairyType.gaiaEfficiency get() = manaSet.gaia / (cost / 50.0)
val IFairyType.aquaEfficiency get() = manaSet.aqua / (cost / 50.0)
val IFairyType.darkEfficiency get() = manaSet.dark / (cost / 50.0)
