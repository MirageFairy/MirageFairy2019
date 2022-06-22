package miragefairy2019.mod.fairyweapon

import miragefairy2019.api.IFairyType
import miragefairy2019.lib.compound
import miragefairy2019.lib.double
import miragefairy2019.lib.fairyType
import miragefairy2019.lib.get
import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.setCompound
import miragefairy2019.lib.setDouble
import miragefairy2019.lib.toItemStack
import miragefairy2019.lib.toNbt
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.equalsItemDamageTag
import miragefairy2019.libkt.sq
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

fun playSound(world: World, player: EntityPlayer, soundEvent: SoundEvent, volume: Float = 1.0f, pitch: Float = 1.0f) {
    world.playSound(null, player.posX, player.posY, player.posZ, soundEvent, SoundCategory.PLAYERS, volume, pitch)
}


/** メインハンド、オフハンド、最下段のインベントリスロット、最下段以外のインベントリスロットの順に所持アイテムを返します。 */
val EntityPlayer.inventoryItems get() = listOf(getHeldItem(EnumHand.MAIN_HAND), getHeldItem(EnumHand.OFF_HAND)) + inventory.itemStacks

fun findItem(player: EntityPlayer, predicate: (ItemStack) -> Boolean) = player.inventoryItems.find(predicate)

/** アイテム、メタ、タグを考慮します。 */
fun findItem(player: EntityPlayer, itemStackTarget: ItemStack) = findItem(player) { itemStackTarget.equalsItemDamageTag(it) }

/** 搭乗中の妖精を優先します。 */
fun findFairy(fairyWeaponItemStack: ItemStack, player: EntityPlayer): Pair<ItemStack, IFairyType>? {
    val itemStacks = listOf(getCombinedFairy(fairyWeaponItemStack)) + player.inventoryItems
    itemStacks.forEach next@{ itemStack ->
        val fairyType = itemStack.fairyType ?: return@next
        return Pair(itemStack, fairyType)
    }
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

// TODO var
fun getFairyAttribute(attributeName: String, itemStack: ItemStack) = itemStack.nbtProvider["Fairy"][attributeName].double ?: 0.0
fun setFairyAttribute(attributeName: String, itemStack: ItemStack, value: Double) = itemStack.nbtProvider["Fairy"][attributeName].setDouble(value)
fun getCombinedFairy(itemStack: ItemStack) = itemStack.nbtProvider["Fairy"]["CombinedFairy"].compound?.toItemStack() ?: EMPTY_ITEM_STACK // TODO 戻り型
fun setCombinedFairy(itemStack: ItemStack, itemStackFairy: ItemStack) = itemStack.nbtProvider["Fairy"]["CombinedFairy"].setCompound(itemStackFairy.copy(1).toNbt())


fun breakBlock(world: World, player: EntityPlayer, facing: EnumFacing, itemStack: ItemStack, blockPos: BlockPos, fortune: Int, collection: Boolean): Boolean {
    if (!world.isBlockModifiable(player, blockPos)) return false
    if (!player.canPlayerEdit(blockPos, facing, itemStack)) return false

    val blockState = world.getBlockState(blockPos)
    val block = blockState.block
    block.dropBlockAsItem(world, blockPos, blockState, fortune)
    world.setBlockState(blockPos, Blocks.AIR.defaultState, 3)
    if (collection) {
        world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPos)).forEach { entityItem ->
            entityItem.setPosition(player.posX, player.posY, player.posZ)
            entityItem.setNoPickupDelay()
        }
    }

    return true
}

fun <E : Entity> getEntities(classEntity: Class<E>, world: World, positionCenter: Vec3d, radius: Double): List<E> {
    return world.getEntitiesWithinAABB(
        classEntity, AxisAlignedBB(
            positionCenter.x - radius,
            positionCenter.y - radius,
            positionCenter.z - radius,
            positionCenter.x + radius,
            positionCenter.y + radius,
            positionCenter.z + radius
        )
    ) { e ->
        if (e == null) return@getEntitiesWithinAABB false
        if (e.getDistanceSq(positionCenter.x, positionCenter.y, positionCenter.z) > radius.sq()) return@getEntitiesWithinAABB false
        true
    }
}
