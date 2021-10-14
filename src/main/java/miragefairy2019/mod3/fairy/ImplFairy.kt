package miragefairy2019.mod3.fairy

import miragefairy2019.mod.lib.WeightedRandom
import miragefairy2019.mod3.fairy.api.FairyDropEntry
import miragefairy2019.mod3.fairy.api.IFairyRelationManager
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import java.util.Random

fun IFairyRelationManager.getDropList(entity: Entity): List<FairyDropEntry> {
    return entityClassRelationRegistry.filter { it.key.isInstance(entity) }.map { it.drop } + entityRelationRegistry.mapNotNull { it.getDrop(entity) }
}

/**
 * 返されるインスタンスは呼び出し側が複製する必要があります。
 */
fun List<FairyDropEntry>.drop(random: Random): ItemStack? = WeightedRandom.getRandomItem(random, map { WeightedRandom.Item(it.fairy, it.relevance) }).orElse(null)
