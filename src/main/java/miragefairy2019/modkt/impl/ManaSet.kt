package miragefairy2019.modkt.impl

import miragefairy2019.mod.api.fairy.IManaSet

data class ManaSet(
        private val shine: Double,
        private val fire: Double,
        private val wind: Double,
        private val gaia: Double,
        private val aqua: Double,
        private val dark: Double
) : IManaSet {
    companion object {
        val ZERO = ManaSet(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }

    constructor() : this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    override fun getShine() = shine
    override fun getFire() = fire
    override fun getWind() = wind
    override fun getGaia() = gaia
    override fun getAqua() = aqua
    override fun getDark() = dark
}

class MutableManaSet(
        private var shine: Double,
        private var fire: Double,
        private var wind: Double,
        private var gaia: Double,
        private var aqua: Double,
        private var dark: Double
) : IManaSet {
    constructor() : this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    override fun getShine() = shine
    override fun getFire() = fire
    override fun getWind() = wind
    override fun getGaia() = gaia
    override fun getAqua() = aqua
    override fun getDark() = dark
    fun setShine(shine: Double) = run { this.shine = shine }
    fun setFire(fire: Double) = run { this.fire = fire }
    fun setWind(wind: Double) = run { this.wind = wind }
    fun setGaia(gaia: Double) = run { this.gaia = gaia }
    fun setAqua(aqua: Double) = run { this.aqua = aqua }
    fun setDark(dark: Double) = run { this.dark = dark }

    fun set(manaSet: IManaSet) {
        this.shine = manaSet.shine
        this.fire = manaSet.fire
        this.wind = manaSet.wind
        this.gaia = manaSet.gaia
        this.aqua = manaSet.aqua
        this.dark = manaSet.dark
    }

    fun set(shine: Double, fire: Double, wind: Double, gaia: Double, aqua: Double, dark: Double) {
        this.shine = shine
        this.fire = fire
        this.wind = wind
        this.gaia = gaia
        this.aqua = aqua
        this.dark = dark
    }
}

fun IManaSet.copy() = ManaSet(this.shine, this.fire, this.wind, this.gaia, this.aqua, this.dark)
fun IManaSet.copyAsMutable() = MutableManaSet(this.shine, this.fire, this.wind, this.gaia, this.aqua, this.dark)
operator fun IManaSet.plus(other: IManaSet) = ManaSet(this.shine + other.shine, this.fire + other.fire, this.wind + other.wind, this.gaia + other.gaia, this.aqua + other.aqua, this.dark + other.dark)
