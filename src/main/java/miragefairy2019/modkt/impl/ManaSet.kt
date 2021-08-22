package miragefairy2019.modkt.impl

import com.google.gson.annotations.Expose
import miragefairy2019.modkt.api.IManaSet
import miragefairy2019.modkt.api.IManaType
import kotlin.math.max

data class ManaSet(
        @Expose private val shine: Double,
        @Expose private val fire: Double,
        @Expose private val wind: Double,
        @Expose private val gaia: Double,
        @Expose private val aqua: Double,
        @Expose private val dark: Double
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

data class MutableManaSet(
        @Expose private var shine: Double,
        @Expose private var fire: Double,
        @Expose private var wind: Double,
        @Expose private var gaia: Double,
        @Expose private var aqua: Double,
        @Expose private var dark: Double
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

    fun reset() = set(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    operator fun plusAssign(other: IManaSet) {
        this.shine += other.shine
        this.fire += other.fire
        this.wind += other.wind
        this.gaia += other.gaia
        this.aqua += other.aqua
        this.dark += other.dark
    }
}


fun IManaSet.copy() = ManaSet(this.shine, this.fire, this.wind, this.gaia, this.aqua, this.dark)
fun IManaSet.copyAsMutable() = MutableManaSet(this.shine, this.fire, this.wind, this.gaia, this.aqua, this.dark)
operator fun IManaSet.plus(other: IManaSet): IManaSet = ManaSet(this.shine + other.shine, this.fire + other.fire, this.wind + other.wind, this.gaia + other.gaia, this.aqua + other.aqua, this.dark + other.dark)
operator fun IManaSet.times(other: Double): IManaSet = ManaSet(this.shine * other, this.fire * other, this.wind * other, this.gaia * other, this.aqua * other, this.dark * other)

fun IManaSet.getMana(manaType: IManaType) = when (manaType) {
    ManaType.shine -> shine
    ManaType.fire -> fire
    ManaType.wind -> wind
    ManaType.gaia -> gaia
    ManaType.aqua -> aqua
    ManaType.dark -> dark
    else -> throw IllegalArgumentException("$manaType")
}

val IManaSet.max get() = max(shine, max(fire, max(wind, max(gaia, max(aqua, dark)))))
val IManaSet.sum get() = shine + fire + wind + gaia + aqua + dark
fun IManaSet.sum(rateShine: Double, rateFire: Double, rateWind: Double, rateGaia: Double, rateAqua: Double, rateDark: Double): Double {
    return shine * rateShine + fire * rateFire + wind * rateWind + gaia * rateGaia + aqua * rateAqua + dark * rateDark
}
