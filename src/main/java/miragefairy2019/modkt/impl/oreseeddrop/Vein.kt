package miragefairy2019.modkt.impl.oreseeddrop

import miragefairy2019.modkt.api.oreseeddrop.IOreSeedDropRequirement
import miragefairy2019.modkt.api.oreseeddrop.OreSeedDropEnvironment

class Vein(val seed: Long, val horizontalSize: Int, val verticalSize: Int, val chance: Double, vararg val elements: Element) : IOreSeedDropRequirement {
    override fun test(environment: OreSeedDropEnvironment) = VeinHelper.test(seed, horizontalSize, verticalSize, chance, elements, environment)
    override fun getDescriptions() = listOf(
            "Vein: #$seed ${horizontalSize}H x ${verticalSize}V ${String.format("%.1f%%", chance * 100)}",
            "Elements: ${elements.joinToString(" ") { it.shortName }}"
    )
}

class Element(val shortName: String, val seed: Int, val size: Int)

object Elements {
    @JvmStatic // TODO jvm
    val ALUMINIUM = Element("Al", 67100587, 16 * 2)

    @JvmStatic // TODO jvm
    val FERRUM = Element("Fe", 12827461, 16 * 2)

    @JvmStatic // TODO jvm
    val CALCIUM = Element("Ca", 30190627, 16 * 4)

    @JvmStatic // TODO jvm
    val NATRIUM = Element("Na", 36621469, 16 * 4)

    @JvmStatic // TODO jvm
    val KALIUM = Element("K", 93091841, 16 * 4)

    @JvmStatic // TODO jvm
    val MAGNESIUM = Element("Mg", 22693171, 16 * 4)

    @JvmStatic // TODO jvm
    val PHOSPHORUS = Element("P", 33822127, 16 * 8)

    @JvmStatic // TODO jvm
    val FLUORINE = Element("F", 70792441, 16 * 8)

    @JvmStatic // TODO jvm
    val CARBON = Element("C", 14082053, 16 * 8)

    @JvmStatic // TODO jvm
    val SULFUR = Element("S", 52516049, 16 * 16)

    @JvmStatic // TODO jvm
    val LITHIUM = Element("Li", 47876821, 16 * 32)

    @JvmStatic // TODO jvm
    val ZINC = Element("Zn", 89828281, 16 * 32)

    @JvmStatic // TODO jvm
    val BORON = Element("B", 51617299, 16 * 32)

    @JvmStatic // TODO jvm
    val MERCURY = Element("Hg", 45121997, 16 * 64)
}
