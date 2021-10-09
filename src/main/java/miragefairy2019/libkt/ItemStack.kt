package miragefairy2019.libkt

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

val ItemStack.orNull get(): ItemStack? = takeIf { !it.isEmpty }

fun Item.getSubItems(creativeTab: CreativeTabs): List<ItemStack> {
    val list = NonNullList.create<ItemStack>()
    this.getSubItems(creativeTab, list)
    return list
}

/**
 * @param itemStack このインスタンスはメソッド内部でcopyされるため、破壊されません。
 */
fun drop(world: World, itemStack: ItemStack, pos: Vec3d): EntityItem {
    val entityItem = EntityItem(world, pos.x, pos.y, pos.z, itemStack)
    world.spawnEntity(entityItem)
    return entityItem
}

/**
 * @param itemStack このインスタンスはメソッド内部でcopyされるため、破壊されません。
 */
fun drop(world: World, itemStack: ItemStack, blockPos: BlockPos) = drop(world, itemStack, Vec3d(blockPos).addVector(0.5, 0.5, 0.5))
