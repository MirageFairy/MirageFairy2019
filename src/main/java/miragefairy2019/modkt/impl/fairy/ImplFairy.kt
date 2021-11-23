package miragefairy2019.modkt.impl.fairy

import miragefairy2019.libkt.buildText
import miragefairy2019.mod3.erg.ErgSet
import miragefairy2019.mod3.erg.api.IErgSet
import miragefairy2019.mod3.erg.api.IErgType
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.api.mana.IManaSet
import miragefairy2019.modkt.api.mana.IManaType
import miragefairy2019.modkt.impl.ManaSet
import miragefairy2019.modkt.impl.getMana
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


fun IFairyType.mana(manaType: IManaType) = manaSet.getMana(manaType)
fun IFairyType.erg(ergType: IErgType) = ergSet.getPower(ergType)


class ColorSet(val skin: Int, val bright: Int, val dark: Int, val hair: Int)
