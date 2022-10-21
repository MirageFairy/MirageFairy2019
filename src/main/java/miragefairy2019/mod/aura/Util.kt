package miragefairy2019.mod.aura

import miragefairy2019.api.ErgSet
import miragefairy2019.api.IAuraItem
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.div
import miragefairy2019.lib.plus
import miragefairy2019.lib.times
import miragefairy2019.mod.fairy.getVariant
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import mirrg.kotlin.hydrogen.or
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack

fun ItemStack.getAuraSet(): AuraSet? {

    // 食べ物以外には反応しない
    val item = this.item
    if (item !is ItemFood) return null

    val rate = item.getHealAmount(this).toDouble() / 2.0

    return if (item is IAuraItem) {
        AuraSet(
            item.getAuraManaSet(this).or { ManaSet.ZERO } * rate,
            item.getAuraErgSet(this).or { ErgSet.ZERO } * rate
        )
    } else {

        // アイテムスタックに紐づけられた妖精のリスト
        val entries = FairySelector().itemStack(this).allMatch().withoutPartiallyMatch.primaries
        if (entries.isEmpty()) return null // 関連付けられた妖精が居ない場合は無視

        // 平均を返す
        AuraSet(
            entries.map { it.fairyCard.getVariant().manaSet }.fold(ManaSet.ZERO) { a, b -> a + b } / entries.size.toDouble() * rate,
            entries.map { it.fairyCard.getVariant().ergSet }.fold(ErgSet.ZERO) { a, b -> a + b } / entries.size.toDouble() * rate
        )
    }
}
