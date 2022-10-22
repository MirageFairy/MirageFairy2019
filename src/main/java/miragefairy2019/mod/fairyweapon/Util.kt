package miragefairy2019.mod.fairyweapon

import miragefairy2019.api.IFairySpec
import miragefairy2019.lib.compound
import miragefairy2019.lib.double
import miragefairy2019.lib.fairySpec
import miragefairy2019.lib.get
import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.setCompound
import miragefairy2019.lib.setDouble
import miragefairy2019.lib.toItemStack
import miragefairy2019.lib.toNbt
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.equalsItemDamageTag
import miragefairy2019.libkt.notEmptyOrNull
import miragefairy2019.libkt.orEmpty
import miragefairy2019.libkt.sq
import miragefairy2019.mod.fairyweapon.items.ItemFairyWeaponMagic4
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.IShearable

/** メインハンド、オフハンド、最下段のインベントリスロット、最下段以外のインベントリスロットの順に所持アイテムを返します。 */
val EntityPlayer.inventoryItems: List<ItemStack> get() = listOf(getHeldItem(EnumHand.MAIN_HAND), getHeldItem(EnumHand.OFF_HAND)) + inventory.itemStacks

fun findAllItems(player: EntityPlayer, predicate: (ItemStack) -> Boolean): List<ItemStack> = player.inventoryItems.filter(predicate)

/** アイテム、メタ、タグを考慮します。 */
fun findAllItems(player: EntityPlayer, itemStackTarget: ItemStack) = findAllItems(player) { itemStackTarget equalsItemDamageTag it }

fun findItem(player: EntityPlayer, predicate: (ItemStack) -> Boolean): ItemStack? = player.inventoryItems.find(predicate)

/** アイテム、メタ、タグを考慮します。 */
fun findItem(player: EntityPlayer, itemStackTarget: ItemStack) = findItem(player) { itemStackTarget equalsItemDamageTag it }

/** 搭乗中の妖精を優先します。 */
fun findFairy(fairyWeaponItemStack: ItemStack, player: EntityPlayer): Pair<ItemStack, IFairySpec>? {
    val itemStacks = listOf(fairyWeaponItemStack.combinedFairy) + player.inventoryItems
    itemStacks.forEach next@{ itemStack ->
        val fairySpec = itemStack.fairySpec ?: return@next
        return Pair(itemStack, fairySpec)
    }
    return null
}


fun getFairyAttribute(attributeName: String, itemStack: ItemStack) = itemStack.nbtProvider["Fairy"][attributeName].double ?: 0.0
fun setFairyAttribute(attributeName: String, itemStack: ItemStack, value: Double) = itemStack.nbtProvider["Fairy"][attributeName].setDouble(value)
var ItemStack.combinedFairy: ItemStack
    get() = this.nbtProvider["Fairy"]["CombinedFairy"].compound?.toItemStack()?.notEmptyOrNull ?: EMPTY_ITEM_STACK
    set(it) = this.nbtProvider["Fairy"]["CombinedFairy"].setCompound(it.orEmpty.copy(1).toNbt())


fun breakBlock(
    world: World,
    player: EntityPlayer,
    itemStack: ItemStack,
    blockPos: BlockPos,
    facing: EnumFacing = EnumFacing.UP,
    fortune: Int = 0,
    silkTouch: Boolean = false,
    collection: Boolean = false,
    canShear: Boolean = false
): Boolean {

    // 安全装置
    if (!world.isBlockModifiable(player, blockPos)) return false
    if (!player.canPlayerEdit(blockPos, facing, itemStack)) return false

    // 破壊成立

    // ブロックの特定
    val blockState = world.getBlockState(blockPos)
    val block = blockState.block

    // 破壊
    run finish@{

        // はさみ
        if (canShear) {
            if (block is IShearable) {
                if (block.isShearable(itemStack, world, blockPos)) {

                    val drops = block.onSheared(itemStack, world, blockPos, fortune)
                    drops.forEach { drop ->
                        drop.drop(world, blockPos)
                    }

                    world.setBlockState(blockPos, Blocks.AIR.defaultState, 3)

                    return@finish
                }
            }
        }

        // 通常の破壊

        val dummyItemStack = Items.STICK.createItemStack()
        if (fortune > 0) EnchantmentHelper.setEnchantments(mapOf(Enchantments.FORTUNE to fortune), dummyItemStack)
        if (silkTouch) EnchantmentHelper.setEnchantments(mapOf(Enchantments.SILK_TOUCH to 1), dummyItemStack)

        block.harvestBlock(world, player, blockPos, blockState, world.getTileEntity(blockPos), dummyItemStack)

        world.setBlockState(blockPos, Blocks.AIR.defaultState, 3)

    }

    // 破壊完了

    // 収集
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


fun addCoolTimeToFairyWeapon(item: ItemFairyWeaponMagic4, player: EntityPlayer, coolTime: Int) {
    Item.REGISTRY.forEach { otherItem: Item/* 省略すると謎のビルドエラーになる */ ->
        val matched = when {
            otherItem === item -> true
            otherItem is ItemFairyWeaponMagic4 && item.getCoolTimeCategories().any { it in otherItem.getCoolTimeCategories() } -> true
            else -> false
        }
        if (matched) {
            if (player.cooldownTracker.hasCooldown(otherItem)) {
                val oldCoolTime = player.cooldownTracker.getCooldown(otherItem, 0.0f)
                if (coolTime > oldCoolTime) {
                    player.cooldownTracker.setCooldown(otherItem, coolTime)
                }
            } else {
                player.cooldownTracker.setCooldown(otherItem, coolTime)
            }
        }
    }
}
