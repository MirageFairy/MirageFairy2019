package miragefairy2019.libkt

import mirrg.kotlin.castOrNull
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemWritableBook
import net.minecraft.item.ItemWrittenBook
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

val ItemStack.orNull get() = takeIf { !it.isEmpty }
val ItemStack?.orEmpty: ItemStack get() = this ?: ItemStack.EMPTY

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun createEmptyItemStack() = ItemStack(null as Item?)
val EMPTY_ITEM_STACK get() = ItemStack.EMPTY!!

fun ItemStack.copy(count: Int): ItemStack = copy().also { it.count = count }


fun Item.getSubItems(creativeTab: CreativeTabs): List<ItemStack> = NonNullList.create<ItemStack>().also { getSubItems(creativeTab, it) }

val ItemStack.containerItem get() = if (item.hasContainerItem(this)) item.getContainerItem(this).orNull else null

/** このアイテムにカスタム名が存在する場合のみ、それを返します。 */
val ItemStack.customName get() = if (hasDisplayName()) displayName else null


/**
 * このアイテムスタックに記入された文字列を返します。
 * デフォルトでは紙に付けられた名前および本の最初のページに書かれた文字列を返します。
 */
val ItemStack.string
    get() = when (item) {
        Items.PAPER -> customName
        is ItemWritableBook,
        is ItemWrittenBook -> tagCompound?.getTag("pages")?.castOrNull<NBTTagList>()?.get(0)?.castOrNull<NBTTagString>()?.string
        else -> null
    }


// ワールドドロップ

/**
 * @receiver このインスタンスはメソッド内部でcopyされるため、破壊されません。
 */
fun ItemStack.drop(world: World, pos: Vec3d, motionless: Boolean = false, noPickupDelay: Boolean = false): EntityItem {
    val entityItem = EntityItem(world, pos.x, pos.y, pos.z, copy())
    if (motionless) {
        entityItem.motionX = 0.0
        entityItem.motionY = 0.0
        entityItem.motionZ = 0.0
    }
    if (noPickupDelay) entityItem.setNoPickupDelay()
    world.spawnEntity(entityItem)
    return entityItem
}

/**
 * @receiver このインスタンスはメソッド内部でcopyされるため、破壊されません。
 */
fun ItemStack.drop(world: World, blockPos: BlockPos, motionless: Boolean = false) = drop(world, Vec3d(blockPos).addVector(0.5, 0.5, 0.5), motionless)


// 同値判定
infix fun ItemStack.equalsItem(other: ItemStack) = item == other.item
infix fun ItemStack.equalsItemDamage(other: ItemStack) = equalsItem(other) && itemDamage == other.itemDamage
infix fun ItemStack.equalsItemDamageTag(other: ItemStack) = equalsItemDamage(other) && ItemStack.areItemStackShareTagsEqual(this, other)
infix fun ItemStack.equalsItemDamageTagCount(other: ItemStack) = equalsItemDamageTag(other) && count == other.count


// 相互変換
val Item.block get() = Block.getBlockFromItem(this).takeIf { it != Blocks.AIR }
val Block.item get() = Item.getItemFromBlock(this).takeIf { it != Items.AIR }
fun Item.createItemStack(count: Int = 1, metadata: Int = 0) = ItemStack(this, count, metadata)
fun Block.createItemStack(count: Int = 1, metadata: Int = 0) = ItemStack(this, count, metadata)
