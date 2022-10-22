package miragefairy2019.mod.systems

import miragefairy2019.api.Erg
import miragefairy2019.lib.get
import miragefairy2019.libkt.notEmptyOrNull
import miragefairy2019.mod.fairy.fairyVariant
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.fairyweapon.combinedFairy
import miragefairy2019.mod.fairyweapon.items.ItemFairyWeapon
import mirrg.kotlin.hydrogen.max
import mirrg.kotlin.hydrogen.min
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack

fun Entity.getErgFactor(erg: Erg): Double {

    // 特殊防御
    val specialFactor = when (erg) {
        Erg.FLAME -> if (this.isImmuneToFire) 0.0 else 1.0 // ゾンビピッグマン等はこれが無いと貫通する
        else -> 1.0
    }

    return 1.0 - this.getRawErg(erg) / 10.0 min specialFactor // 10ごとに100%軽減
}

fun Entity.getRawErg(erg: Erg): Double {

    // エンティティに関連付いた妖精のエルグ
    val relationErg = FairySelector().entity(this).allMatch().withoutPartiallyMatch.primaries.map { it.fairyCard.rawErgSet[erg] }.max() ?: 0.0 // 未登録時0.0扱い

    // 装備に関連付いた妖精のエルグ
    val equipmentErg = this.equipmentAndArmor.mapNotNull { it.getRawErg(erg) }.max()?.let { it / 2.0 } // 半減適用

    return relationErg max equipmentErg
}

fun ItemStack.getRawErg(erg: Erg): Double? {

    // アイテムに関連付いた妖精のエルグ
    val relationErg = FairySelector().itemStack(this).allMatch().withoutPartiallyMatch.primaries.map { it.fairyCard.rawErgSet[erg] }.max()?.let { it / 2.0 } // 半減適用

    // アイテムが妖精だった場合のエルグ
    val fairyErg = this.fairyVariant?.fairyCard?.rawErgSet?.get(erg)

    // 妖精武器に搭乗した妖精のエルグ
    val combinedErg = if (this.item is ItemFairyWeapon) this.combinedFairy.notEmptyOrNull?.fairyVariant?.fairyCard?.rawErgSet?.get(erg) else null

    return relationErg max fairyErg max combinedErg // 最大のものしか乗らない
}

private infix fun Double.max(other: Double?): Double {
    if (other == null) return this
    return this max other
}

private infix fun Double?.max(other: Double?): Double? {
    if (this == null) return other
    if (other == null) return this
    return this max other
}
