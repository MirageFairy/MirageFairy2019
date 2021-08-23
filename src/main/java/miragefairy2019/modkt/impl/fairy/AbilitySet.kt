package miragefairy2019.modkt.impl.fairy

import miragefairy2019.modkt.api.fairy.IAbilityEntry
import miragefairy2019.modkt.api.fairy.IAbilitySet
import miragefairy2019.modkt.api.fairy.IErgType

class AbilitySet(private val iterable: Iterable<IAbilityEntry>) : IAbilitySet {
    private val list = iterable.toList()
    private val map = list.associateBy { it.type }
    override fun getEntries() = list
    override fun getPower(type: IErgType) = map[type]?.power ?: 0.0
}

class AbilityEntry(private val type: IErgType, private val power: Double) : IAbilityEntry {
    override fun getPower() = power
    override fun getType() = type
}
