package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.equalsItemDamageTag
import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.orNull
import miragefairy2019.mod.api.fairy.IItemFairy
import miragefairy2019.mod3.fairy.api.IFairyType
import mirrg.kotlin.castOrNull
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

fun playSound(world: World, player: EntityPlayer, soundEvent: SoundEvent, volume: Float = 1.0f, pitch: Float = 1.0f) {
    world.playSound(null, player.posX, player.posY, player.posZ, soundEvent, SoundCategory.PLAYERS, volume, pitch)
}

fun <T> spawnParticleTargets(
    world: World,
    targets: List<T>,
    fPosition: (T) -> Vec3d,
    fColor: (T) -> Int
) {
    val rate = 5 / targets.size.coerceAtLeast(5).toDouble()
    targets.forEach { target ->
        if (Math.random() < 0.2 * rate) {
            val position = fPosition(target)
            val color = fColor(target)
            world.spawnParticle(
                EnumParticleTypes.SPELL_MOB,
                position.x,
                position.y,
                position.z,
                (color shr 16 and 0xFF) / 255.0,
                (color shr 8 and 0xFF) / 255.0,
                (color shr 0 and 0xFF) / 255.0
            )
        }
    }
}


/** メインハンド、オフハンド、最下段のインベントリスロット、最下段以外のインベントリスロットの順に所持アイテムを返します。 */
val EntityPlayer.inventoryItems get() = listOf(getHeldItem(EnumHand.MAIN_HAND), getHeldItem(EnumHand.OFF_HAND)) + inventory.itemStacks

fun findItem(player: EntityPlayer, predicate: (ItemStack) -> Boolean) = player.inventoryItems.find(predicate)

/** アイテム、メタ、タグを考慮します。 */
fun findItem(player: EntityPlayer, itemStackTarget: ItemStack) = findItem(player) { itemStackTarget.equalsItemDamageTag(it) }

val ItemStack.fairyType get() = item.castOrNull<IItemFairy>()?.getMirageFairy2019Fairy(this)?.orNull

/** 搭乗中の妖精を優先します。 */
fun findFairy(fairyWeaponItemStack: ItemStack, player: EntityPlayer): Pair<ItemStack, IFairyType>? {
    val items = listOf(FairyWeaponUtils.getCombinedFairy(fairyWeaponItemStack)) + player.inventoryItems
    items.forEach { fairyItemStack -> fairyItemStack.fairyType?.let { fairyType -> return Pair(fairyItemStack, fairyType) } }
    return null
}


// ブロック探索

/** [predicate]にマッチするような[next]の要素のリストを返しつつ、マッチしたものを[result]に追加します。 */
fun gain(result: MutableList<BlockPos>, next: List<BlockPos>, predicate: (BlockPos) -> Boolean): List<BlockPos> {
    val next2 = mutableListOf<BlockPos>()
    next.forEach { blockPos2 ->
        if (predicate(blockPos2)) {
            next2 += blockPos2
            result += blockPos2
        }
    }
    return next2
}

/** [next]の各要素に隣接する未訪問のブロックのリスト返しつつ、訪問済みにします。 */
fun extend(visited: MutableList<BlockPos>, next: List<BlockPos>): List<BlockPos> {
    val next2 = mutableListOf<BlockPos>()
    next.forEach { blockPos2 ->
        (-1..1).forEach { x ->
            (-1..1).forEach { y ->
                (-1..1).forEach { z ->
                    val blockPos3 = blockPos2.add(x, y, z)
                    if (blockPos3 !in visited) {
                        next2 += blockPos3
                        visited += blockPos3
                    }
                }
            }
        }
    }
    return next2
}

/** 最初に[zero]に回収判定を行い、[range]の分だけ幅優先探索を行います。 */
fun search(range: Int, visited: MutableList<BlockPos>, zero: List<BlockPos>, predicate: (BlockPos) -> Boolean): List<BlockPos> {
    val result = mutableListOf<BlockPos>()
    var next = zero

    if (next.isEmpty()) return result
    next = gain(result, next, predicate)
    repeat(range) {
        if (next.isEmpty()) return result
        next = extend(visited, next)
        if (next.isEmpty()) return result
        next = gain(result, next, predicate)
    }
    return result
}

/** 最初に[zero]に回収判定を行わず、[range]の分だけ幅優先探索を行います。 */
fun extendSearch(range: Int, visited: MutableList<BlockPos>, zero: List<BlockPos>, predicate: (BlockPos) -> Boolean): List<BlockPos> {
    val result = mutableListOf<BlockPos>()
    var next = zero

    repeat(range) {
        if (next.isEmpty()) return result
        next = extend(visited, next)
        if (next.isEmpty()) return result
        next = gain(result, next, predicate)
    }
    return result
}
