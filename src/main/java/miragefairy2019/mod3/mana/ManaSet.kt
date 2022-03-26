package miragefairy2019.mod3.mana

import com.google.gson.annotations.Expose
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mana.api.IManaSet
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

    override fun getShine() = shine
    override fun getFire() = fire
    override fun getWind() = wind
    override fun getGaia() = gaia
    override fun getAqua() = aqua
    override fun getDark() = dark
}


fun IManaSet.getMana(manaType: EnumManaType) = when (manaType) {
    EnumManaType.SHINE -> shine
    EnumManaType.FIRE -> fire
    EnumManaType.WIND -> wind
    EnumManaType.GAIA -> gaia
    EnumManaType.AQUA -> aqua
    EnumManaType.DARK -> dark
}

fun IManaSet.copy() = ManaSet(shine, fire, wind, gaia, aqua, dark)
operator fun IManaSet.plus(a: IManaSet): IManaSet = ManaSet(shine + a.shine, fire + a.fire, wind + a.wind, gaia + a.gaia, aqua + a.aqua, dark + a.dark)
operator fun IManaSet.times(a: Double): IManaSet = ManaSet(shine * a, fire * a, wind * a, gaia * a, aqua * a, dark * a)
operator fun IManaSet.div(a: Double): IManaSet = ManaSet(shine / a, fire / a, wind / a, gaia / a, aqua / a, dark / a)
val IManaSet.max get() = max(shine, max(fire, max(wind, max(gaia, max(aqua, dark)))))
val IManaSet.sum get() = shine + fire + wind + gaia + aqua + dark
fun IManaSet.sum(rateShine: Double, rateFire: Double, rateWind: Double, rateGaia: Double, rateAqua: Double, rateDark: Double) = shine * rateShine + fire * rateFire + wind * rateWind + gaia * rateGaia + aqua * rateAqua + dark * rateDark
