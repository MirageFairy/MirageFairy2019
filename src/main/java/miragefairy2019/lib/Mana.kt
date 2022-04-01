package miragefairy2019.lib

import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.libkt.setColor
import miragefairy2019.libkt.textComponent
import net.minecraft.util.text.TextFormatting
import kotlin.math.max


// Mana

val Mana.displayName get() = let { mana -> textComponent { translate("mirageFairy2019.mana.$mana.name").setColor(textColor) } }

val Mana.color
    get() = when (this) {
        Mana.SHINE -> 0x007068
        Mana.FIRE -> 0xFF0000
        Mana.WIND -> 0x107A00
        Mana.GAIA -> 0x664E00
        Mana.AQUA -> 0x3535FF
        Mana.DARK -> 0xB000D3
    }

val Mana.textColor
    get() = when (this) {
        Mana.SHINE -> TextFormatting.WHITE
        Mana.FIRE -> TextFormatting.RED
        Mana.WIND -> TextFormatting.GREEN
        Mana.GAIA -> TextFormatting.YELLOW
        Mana.AQUA -> TextFormatting.BLUE
        Mana.DARK -> TextFormatting.DARK_GRAY
    }


// ManaSet

operator fun ManaSet.get(mana: Mana) = when (mana) {
    Mana.SHINE -> shine
    Mana.FIRE -> fire
    Mana.WIND -> wind
    Mana.GAIA -> gaia
    Mana.AQUA -> aqua
    Mana.DARK -> dark
}

operator fun ManaSet.plus(a: ManaSet) = ManaSet(shine + a.shine, fire + a.fire, wind + a.wind, gaia + a.gaia, aqua + a.aqua, dark + a.dark)
operator fun ManaSet.minus(a: ManaSet) = ManaSet(shine - a.shine, fire - a.fire, wind - a.wind, gaia - a.gaia, aqua - a.aqua, dark - a.dark)
operator fun ManaSet.times(a: Double) = ManaSet(shine * a, fire * a, wind * a, gaia * a, aqua * a, dark * a)
operator fun ManaSet.div(a: Double) = ManaSet(shine / a, fire / a, wind / a, gaia / a, aqua / a, dark / a)

val ManaSet.max get() = max(shine, max(fire, max(wind, max(gaia, max(aqua, dark)))))
val ManaSet.sum get() = shine + fire + wind + gaia + aqua + dark
fun ManaSet.sum(rateShine: Double, rateFire: Double, rateWind: Double, rateGaia: Double, rateAqua: Double, rateDark: Double) = shine * rateShine + fire * rateFire + wind * rateWind + gaia * rateGaia + aqua * rateAqua + dark * rateDark
