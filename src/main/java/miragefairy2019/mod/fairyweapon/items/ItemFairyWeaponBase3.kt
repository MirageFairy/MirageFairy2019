package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.api.Mana.AQUA
import miragefairy2019.api.Mana.DARK
import miragefairy2019.api.Mana.FIRE
import miragefairy2019.api.Mana.GAIA
import miragefairy2019.api.Mana.SHINE
import miragefairy2019.api.Mana.WIND
import miragefairy2019.libkt.bold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer
import miragefairy2019.mod.fairyweapon.magic4.MagicStatusBuilder
import miragefairy2019.mod.fairyweapon.magic4.float0
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.skill.Mastery

abstract class ItemFairyWeaponBase3(val weaponMana: Mana, val mastery: Mastery) : ItemFairyWeaponMagic4() {
    override val isOldMana: Boolean get() = true
}


// Magic Status

fun <T> MagicStatusBuilder<T>.setVisibility(visibility: EnumVisibility) = apply { this.visibility = visibility }

fun <T : Comparable<T>> MagicStatusBuilder<T>.setRange(range: ClosedRange<T>) = apply {
    formula = Formula { formula.calculate(it).coerceIn(range.start, range.endInclusive) }
    renderer = FormulaRenderer { arguments, function ->
        val value = function.calculate(arguments)
        val displayValue = renderer.render(arguments, function)
        when (value) {
            range.start -> textComponent { displayValue().bold }
            range.endInclusive -> textComponent { displayValue().bold }
            else -> displayValue
        }
    }
}


// Statuses

fun ItemFairyWeaponBase3.createStrengthStatus(weaponStrength: Double, strengthErg: Erg) = status("strength", {
    (weaponStrength + !strengthErg + !mastery * 0.5) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !SHINE
        FIRE -> !FIRE
        WIND -> !WIND
        GAIA -> !GAIA
        AQUA -> !AQUA
        DARK -> !DARK
    }
}, { float0 })

fun ItemFairyWeaponBase3.createExtentStatus(weaponExtent: Double, extentErg: Erg) = status("extent", {
    (weaponExtent + !extentErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !GAIA + !WIND
        FIRE -> !GAIA + !WIND
        WIND -> !GAIA * 2
        GAIA -> !WIND * 2
        AQUA -> !GAIA + !WIND
        DARK -> !GAIA + !WIND
    }
}, { float0 })

fun ItemFairyWeaponBase3.createEnduranceStatus(weaponEndurance: Double, enduranceErg: Erg) = status("endurance", {
    (weaponEndurance + !enduranceErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !FIRE + !AQUA
        FIRE -> !AQUA * 2
        WIND -> !FIRE + !AQUA
        GAIA -> !FIRE + !AQUA
        AQUA -> !FIRE * 2
        DARK -> !FIRE + !AQUA
    }
}, { float0 })

fun ItemFairyWeaponBase3.createProductionStatus(weaponProduction: Double, productionErg: Erg) = status("production", {
    (weaponProduction + !productionErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !DARK * 2
        FIRE -> !SHINE + !DARK
        WIND -> !SHINE + !DARK
        GAIA -> !SHINE + !DARK
        AQUA -> !SHINE + !DARK
        DARK -> !SHINE * 2
    }
}, { float0 })

fun ItemFairyWeaponBase3.createCostStatus() = status("cost", { cost / (1.0 + !mastery * 0.002) }, { float0 })
