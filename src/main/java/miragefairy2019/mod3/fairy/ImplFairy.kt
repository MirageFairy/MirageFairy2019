package miragefairy2019.mod3.fairy

import miragefairy2019.api.ErgSet
import miragefairy2019.api.IFairyType
import miragefairy2019.api.ManaSet
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent

class FairyType(
    private val motif: ResourceLocation?,
    val parentFairy: () -> RankedFairyTypeBundle?,
    private val displayName: ITextComponent,
    private val color: Int,
    private val cost: Double,
    private val manaSet: ManaSet,
    private val ergSet: ErgSet
) : IFairyType {
    override fun isEmpty() = false
    override fun getMotif() = motif
    override fun getDisplayName() = displayName
    override fun getColor() = color
    override fun getCost() = cost
    override fun getManaSet() = manaSet
    override fun getErgSet() = ergSet
}

open class FairyTypeAdapter(internal val parent: IFairyType) : IFairyType {
    override fun isEmpty() = parent.isEmpty
    override fun getMotif() = parent.motif
    override fun getDisplayName(): ITextComponent = parent.displayName
    override fun getColor() = parent.color
    override fun getCost() = parent.cost
    override fun getManaSet(): ManaSet = parent.manaSet
    override fun getErgSet(): ErgSet = parent.ergSet
}

class ColorSet(val skin: Int, val bright: Int, val dark: Int, val hair: Int)
