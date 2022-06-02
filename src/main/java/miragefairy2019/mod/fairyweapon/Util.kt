package miragefairy2019.mod.fairyweapon

import miragefairy2019.api.IFairyType
import miragefairy2019.lib.double
import miragefairy2019.lib.fairyType
import miragefairy2019.lib.get
import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.setDouble
import miragefairy2019.libkt.equalsItemDamageTag
import miragefairy2019.libkt.randomInt
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer

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

fun spawnDamageParticle(world: WorldServer, entity: Entity, damage: Double) {
    val count = world.rand.randomInt(damage / 2.0)
    if (count > 0) {
        world.spawnParticle(
            EnumParticleTypes.DAMAGE_INDICATOR,
            entity.posX,
            entity.posY + entity.height * 0.5,
            entity.posZ, count,
            0.1, 0.0, 0.1,
            0.2
        )
    }
}

fun spawnMagicParticle(world: WorldServer, player: EntityPlayer, target: Entity) = spawnMagicParticle(world, player, target.positionVector.addVector(0.0, (target.height / 2).toDouble(), 0.0))
fun spawnMagicParticle(world: WorldServer, player: EntityPlayer, end: Vec3d) {
    val start = Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ).add(player.lookVec.scale(2.0))
    val delta = end.subtract(start)

    val distance = start.distanceTo(end)

    repeat((distance * 4.0).toInt()) { i ->
        val pos = start.add(delta.scale(i / (distance * 4.0)))
        world.spawnParticle(
            EnumParticleTypes.ENCHANTMENT_TABLE,
            pos.x + (world.rand.nextDouble() - 0.5) * 0.2,
            pos.y + (world.rand.nextDouble() - 0.5) * 0.2,
            pos.z + (world.rand.nextDouble() - 0.5) * 0.2,
            0,
            0.0, 0.0, 0.0,
            0.0
        )
    }
}

fun spawnMagicSplashParticle(world: WorldServer, position: Vec3d) {
    repeat(5) { i ->
        world.spawnParticle(
            EnumParticleTypes.SPELL_INSTANT,
            position.x,
            position.y,
            position.z,
            10,
            (world.rand.nextDouble() - 0.5) * 2.0,
            (world.rand.nextDouble() - 0.5) * 2.0,
            (world.rand.nextDouble() - 0.5) * 2.0,
            0.0
        )
    }
}


/** メインハンド、オフハンド、最下段のインベントリスロット、最下段以外のインベントリスロットの順に所持アイテムを返します。 */
val EntityPlayer.inventoryItems get() = listOf(getHeldItem(EnumHand.MAIN_HAND), getHeldItem(EnumHand.OFF_HAND)) + inventory.itemStacks

fun findItem(player: EntityPlayer, predicate: (ItemStack) -> Boolean) = player.inventoryItems.find(predicate)

/** アイテム、メタ、タグを考慮します。 */
fun findItem(player: EntityPlayer, itemStackTarget: ItemStack) = findItem(player) { itemStackTarget.equalsItemDamageTag(it) }

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

fun getFairyAttribute(attributeName: String, itemStack: ItemStack) = itemStack.nbtProvider["Fairy"][attributeName].double ?: 0.0
fun setFairyAttribute(attributeName: String, itemStack: ItemStack, value: Double) = itemStack.nbtProvider["Fairy"][attributeName].setDouble(value)
