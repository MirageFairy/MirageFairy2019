package miragefairy2019.modkt.impl.fairy

import miragefairy2019.modkt.api.fairy.IErgEntry
import miragefairy2019.modkt.api.fairy.IErgSet
import miragefairy2019.modkt.api.fairy.IErgType

class ErgSet(private val iterable: Iterable<IErgEntry>) : IErgSet {
    private val list = iterable.toList()
    private val map = list.associateBy { it.type }
    override fun getEntries() = list
    override fun getPower(type: IErgType) = map[type]?.power ?: 0.0
}

class ErgEntry(private val type: IErgType, private val power: Double) : IErgEntry {
    override fun getPower() = power
    override fun getType() = type
}
