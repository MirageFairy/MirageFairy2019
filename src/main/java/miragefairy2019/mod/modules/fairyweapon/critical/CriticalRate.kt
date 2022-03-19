package miragefairy2019.mod.modules.fairyweapon.critical

import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.getItem
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.totalWeight
import java.util.Random

class CriticalRate(
    weightRed: Double,
    weightOrange: Double,
    weightYellow: Double,
    weightGreen: Double,
    weightBlue: Double,
    weightWhite: Double,
    weightPurple: Double,
    weightCyan: Double
) {
    private val weightedItems = listOf(
        WeightedItem(EnumCriticalFactor.RED, weightRed),
        WeightedItem(EnumCriticalFactor.ORANGE, weightOrange),
        WeightedItem(EnumCriticalFactor.YELLOW, weightYellow),
        WeightedItem(EnumCriticalFactor.GREEN, weightGreen),
        WeightedItem(EnumCriticalFactor.BLUE, weightBlue),
        WeightedItem(EnumCriticalFactor.WHITE, weightWhite),
        WeightedItem(EnumCriticalFactor.PURPLE, weightPurple),
        WeightedItem(EnumCriticalFactor.CYAN, weightCyan)
    )
    private val totalWeight = weightedItems.totalWeight
    fun getBar(length: Int = 50) = (0 until length).map { weightedItems.getItem(it / 50.0)!! }
    val bar = getBar()
    val mean get() = weightedItems.sumByDouble { it.weight * it.item.coefficient } / totalWeight
    fun get(random: Random) = weightedItems.getRandomItem(random)!!
}
