package miragefairy2019.mod.modules.fairyweapon.critical

import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.getItem
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.totalWeight
import mirrg.boron.util.suppliterator.ISuppliterator
import java.util.Random

class CriticalRate {

    private val weightedItems = mutableListOf<WeightedItem<EnumCriticalFactor>>()
    private val totalWeight: Double

    constructor(
        weightRed: Double,
        weightOrange: Double,
        weightYellow: Double,
        weightGreen: Double,
        weightBlue: Double,
        weightWhite: Double,
        weightPurple: Double,
        weightCyan: Double
    ) {
        weightedItems += WeightedItem(EnumCriticalFactor.RED, weightRed)
        weightedItems += WeightedItem(EnumCriticalFactor.ORANGE, weightOrange)
        weightedItems += WeightedItem(EnumCriticalFactor.YELLOW, weightYellow)
        weightedItems += WeightedItem(EnumCriticalFactor.GREEN, weightGreen)
        weightedItems += WeightedItem(EnumCriticalFactor.BLUE, weightBlue)
        weightedItems += WeightedItem(EnumCriticalFactor.WHITE, weightWhite)
        weightedItems += WeightedItem(EnumCriticalFactor.PURPLE, weightPurple)
        weightedItems += WeightedItem(EnumCriticalFactor.CYAN, weightCyan)
        totalWeight = weightedItems.totalWeight
    }

    fun getBar(): ISuppliterator<EnumCriticalFactor> {
        return ISuppliterator.range(0, 50)
            .map { i -> weightedItems.getItem(i / 50.0) }
    }

    fun getMean(): Double {
        return weightedItems.stream()
            .mapToDouble { i -> i.weight * i.item.coefficient }
            .sum() / totalWeight
    }

    fun get(random: Random): EnumCriticalFactor {
        return weightedItems.getRandomItem(random)!!
    }

}
