package miragefairy2019.modkt.impl.oreseeddrop

import miragefairy2019.modkt.api.oreseeddrop.IOreSeedDropRequirement
import miragefairy2019.modkt.api.oreseeddrop.OreSeedDropEnvironment

class Vein(val seed: Long, val horizontalSize: Int, val verticalSize: Int, val rate: Double, vararg val elements: Element) : IOreSeedDropRequirement {
    override fun test(environment: OreSeedDropEnvironment): Boolean {
        return VeinHelper.test(seed, horizontalSize, verticalSize, rate, elements, environment)
    }
}

class Element(val seed: Int, val size: Int)

object Elements {
    @JvmStatic // TODO jvm
    val ALUMINIUM = Element(67100587, 16 * 2)

    @JvmStatic // TODO jvm
    val FERRUM = Element(12827461, 16 * 2)

    @JvmStatic // TODO jvm
    val CALCIUM = Element(30190627, 16 * 4)

    @JvmStatic // TODO jvm
    val NATRIUM = Element(36621469, 16 * 4)

    @JvmStatic // TODO jvm
    val KALIUM = Element(93091841, 16 * 4)

    @JvmStatic // TODO jvm
    val MAGNESIUM = Element(22693171, 16 * 4)

    @JvmStatic // TODO jvm
    val PHOSPHORUS = Element(33822127, 16 * 8)

    @JvmStatic // TODO jvm
    val FLUORINE = Element(70792441, 16 * 8)

    @JvmStatic // TODO jvm
    val CARBON = Element(14082053, 16 * 8)

    @JvmStatic // TODO jvm
    val SULFUR = Element(52516049, 16 * 16)

    @JvmStatic // TODO jvm
    val LITHIUM = Element(47876821, 16 * 32)

    @JvmStatic // TODO jvm
    val ZINC = Element(89828281, 16 * 32)

    @JvmStatic // TODO jvm
    val BORON = Element(51617299, 16 * 32)

    @JvmStatic // TODO jvm
    val MERCURY = Element(45121997, 16 * 64)
}
