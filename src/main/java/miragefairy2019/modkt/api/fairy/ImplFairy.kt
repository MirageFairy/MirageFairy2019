package miragefairy2019.modkt.api.fairy

import miragefairy2019.libkt.buildText
import miragefairy2019.mod.api.fairy.IFairyType
import miragefairy2019.modkt.api.erg.IErgSet
import miragefairy2019.modkt.api.erg.IErgType
import miragefairy2019.modkt.api.mana.IManaSet
import miragefairy2019.modkt.api.mana.IManaType
import miragefairy2019.modkt.impl.ManaSet
import miragefairy2019.modkt.impl.fairy.ErgSet
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
    override fun getManas() = manaSet
    override fun getAbilities() = ergSet
}

class FairyTypeEmpty : IFairyType {
    override fun isEmpty() = true
    override fun getBreed(): ResourceLocation? = null
    override fun getDisplayName() = buildText { text("Empty") }
    override fun getColor() = 0xFFFFFF
    override fun getCost() = 50.0
    override fun getManas() = ManaSet.ZERO
    override fun getAbilities() = ErgSet(emptyList())
}

open class FairyTypeAdapter(internal val parent: IFairyType) : IFairyType {
    override fun isEmpty() = parent.isEmpty
    override fun getBreed() = parent.breed
    override fun getDisplayName(): ITextComponent = parent.displayName
    override fun getColor() = parent.color
    override fun getCost() = parent.cost
    override fun getManas(): IManaSet = parent.manas
    override fun getAbilities(): IErgSet = parent.abilities
}


fun IFairyType.mana(manaType: IManaType) = manas.getMana(manaType)
fun IFairyType.erg(ergType: IErgType) = abilities.getPower(ergType)
