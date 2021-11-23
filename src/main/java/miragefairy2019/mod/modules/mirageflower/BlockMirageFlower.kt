package miragefairy2019.mod.modules.mirageflower

import miragefairy2019.mod3.erg.api.ErgTypes
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.shineEfficiency

fun getGrowRateInFloor(fairyType: IFairyType) = fairyType.shineEfficiency * fairyType.erg(ErgTypes.crystal) / 100.0 * 3

class BlockMirageFlower : BlockMirageFlowerBase()
