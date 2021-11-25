package miragefairy2019.mod3.oreseeddrop

import miragefairy2019.mod3.oreseeddrop.api.IOreSeedDropRequirement
import miragefairy2019.mod3.oreseeddrop.api.OreSeedDropEnvironment

class Vein(val seed: Long, val horizontalSize: Int, val verticalSize: Int, val chance: Double, vararg val elements: Element) : IOreSeedDropRequirement {
    override fun test(environment: OreSeedDropEnvironment) = VeinHelper.test(seed, horizontalSize, verticalSize, chance, elements, environment)
    override fun getDescriptions() = listOf(
        "Vein: #$seed ${horizontalSize}H x ${verticalSize}V ${String.format("%.1f%%", chance * 100)}",
        "Elements: ${elements.joinToString(" ") { it.shortName }}"
    )
}

class Element(val shortName: String, val seed: Int, val size: Int)

object Elements {
    val ALUMINIUM = Element("Al", 67100587, 16 * 2)
    val FERRUM = Element("Fe", 12827461, 16 * 2)
    val CALCIUM = Element("Ca", 30190627, 16 * 4)
    val NATRIUM = Element("Na", 36621469, 16 * 4)
    val KALIUM = Element("K", 93091841, 16 * 4)
    val MAGNESIUM = Element("Mg", 22693171, 16 * 4)
    val PHOSPHORUS = Element("P", 33822127, 16 * 8)
    val FLUORINE = Element("F", 70792441, 16 * 8)
    val CARBON = Element("C", 14082053, 16 * 8)
    val SULFUR = Element("S", 52516049, 16 * 16)
    val LITHIUM = Element("Li", 47876821, 16 * 32)
    val ZINC = Element("Zn", 89828281, 16 * 32)
    val CUPRUM = Element("Cu", 75013643, 16 * 32)
    val BORON = Element("B", 51617299, 16 * 32)
    val BERYLLIUM = Element("Be", 52884671, 16 * 32)
    val MERCURY = Element("Hg", 45121997, 16 * 64)
    val AURUM = Element("Au", 39500908, 16 * 64)
}
