package miragefairy2019.modkt.impl.fairy

import miragefairy2019.modkt.api.fairy.IAbilityEntry
import miragefairy2019.modkt.api.fairy.IAbilitySet
import miragefairy2019.modkt.api.fairy.IAbilityType

class AbilitySet(private val list: Iterable<IAbilityEntry>) : IAbilitySet {
    private val map = list.associateBy { it.type }
    override fun getEntries() = list
    override fun getPower(type: IAbilityType) = map[type]?.power ?: 0.0
}

class AbilityEntry(private val type: IAbilityType, private val power: Double) : IAbilityEntry {
    override fun getPower() = power
    override fun getType() = type
}
