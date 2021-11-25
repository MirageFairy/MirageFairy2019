package miragefairy2019.modkt.impl.fairy

import miragefairy2019.libkt.buildText
import miragefairy2019.mod3.erg.ErgSet
import miragefairy2019.mod3.erg.api.IErgSet
import miragefairy2019.mod3.erg.api.IErgType
import miragefairy2019.mod3.fairy.api.IFairyType
import miragefairy2019.mod3.mana.ManaSet
import miragefairy2019.mod3.mana.api.IManaSet
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mana.getMana
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent

class FairyType(
    private val breed: ResourceLocation?,
    private val displayName: ITextComponent,
    private val color: Int,
    private val cost: Double,
    private val manaSet: IManaSet,
    private val ergSet: IErgSet
) : IFairyType {
    override fun isEmpty() = false
    override fun getBreed() = breed
    override fun getDisplayName() = displayName
    override fun getColor() = color
    override fun getCost() = cost
    override fun getManaSet() = manaSet
    override fun getErgSet() = ergSet
}

class FairyTypeEmpty : IFairyType {
    override fun isEmpty() = true
    override fun getBreed(): ResourceLocation? = null
    override fun getDisplayName() = buildText { text("Empty") }
    override fun getColor() = 0xFFFFFF
    override fun getCost() = 50.0
    override fun getManaSet() = ManaSet.ZERO
    override fun getErgSet() = ErgSet(emptyList())
}

open class FairyTypeAdapter(internal val parent: IFairyType) : IFairyType {
    override fun isEmpty() = parent.isEmpty
    override fun getBreed() = parent.breed
    override fun getDisplayName(): ITextComponent = parent.displayName
    override fun getColor() = parent.color
    override fun getCost() = parent.cost
    override fun getManaSet(): IManaSet = parent.manaSet
    override fun getErgSet(): IErgSet = parent.ergSet
}


fun IFairyType.mana(manaType: EnumManaType) = manaSet.getMana(manaType)
fun IFairyType.erg(ergType: IErgType) = ergSet.getPower(ergType)

val IFairyType.shineEfficiency get() = manaSet.shine / (cost / 50.0)
val IFairyType.fireEfficiency get() = manaSet.fire / (cost / 50.0)
val IFairyType.windEfficiency get() = manaSet.wind / (cost / 50.0)
val IFairyType.gaiaEfficiency get() = manaSet.gaia / (cost / 50.0)
val IFairyType.aquaEfficiency get() = manaSet.aqua / (cost / 50.0)
val IFairyType.darkEfficiency get() = manaSet.dark / (cost / 50.0)


class ColorSet(val skin: Int, val bright: Int, val dark: Int, val hair: Int)
