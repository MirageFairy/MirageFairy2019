package miragefairy2019.mod.systems

import miragefairy2019.lib.itemStacks
import miragefairy2019.libkt.BlockRegion
import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.axisAlignedBB
import miragefairy2019.resourcemaker.block
import miragefairy2019.libkt.forEach
import miragefairy2019.libkt.totalWeight
import miragefairy2019.libkt.unique
import miragefairy2019.mod.fairy.FairyTypes
import miragefairy2019.mod.fairy.RankedFairyTypeBundle
import mirrg.kotlin.castOrNull
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary

object FairyCrystalDrop {
    val dropHandlers = mutableListOf<DropHandler>()
}


enum class DropCategory { FIXED, COMMON, RARE }


class FairyCrystalDropEnvironment(
    val player: EntityPlayer?,
    val world: World?,
    val pos: BlockPos?,
    val facing: EnumFacing?
) {
    val blocks = mutableSetOf<Block>()
    val blockStates = mutableSetOf<IBlockState>()
    val items = mutableSetOf<Item>()
    val itemStacks = mutableSetOf<ItemStack>()
    val biomes = mutableSetOf<Biome>()
    val biomeTypes = mutableSetOf<BiomeDictionary.Type>()
    val classEntities = mutableSetOf<Class<out Entity>>()
    val entities = mutableSetOf<Entity>()
}

fun FairyCrystalDropEnvironment.insertItemStack(itemStack: ItemStack) {
    if (itemStack.isEmpty) return

    itemStacks += itemStack
    items += itemStack.item

    itemStack.item.block?.let { blocks += it }

}

fun FairyCrystalDropEnvironment.insertItemStacks(player: EntityPlayer) {
    player.inventory.mainInventory.forEach { insertItemStack(it) }
    player.inventory.armorInventory.forEach { insertItemStack(it) }
    player.inventory.offHandInventory.forEach { insertItemStack(it) }
}

fun FairyCrystalDropEnvironment.insertBlocks(world: World, blockPos: BlockPos) {
    val blockState = world.getBlockState(blockPos)

    blockStates += blockState
    blocks += blockState.block

    world.getTileEntity(blockPos)?.castOrNull<IInventory>()?.let { inventory ->
        inventory.itemStacks.forEach { insertItemStack(it) }
    }

}

fun FairyCrystalDropEnvironment.insertBlocks(world: World, blockRegion: BlockRegion) = blockRegion.forEach { x, y, z -> insertBlocks(world, BlockPos(x, y, z)) }

fun FairyCrystalDropEnvironment.insertBiome(biome: Biome) {
    biomes += biome
    biomeTypes += BiomeDictionary.getTypes(biome)
}

fun FairyCrystalDropEnvironment.insertEntity(entity: Entity) {
    entities += entity
    classEntities += entity.javaClass
}

fun FairyCrystalDropEnvironment.insertEntities(world: World, center: Vec3d, radius: Double) {
    world.getEntitiesWithinAABB(Entity::class.java, axisAlignedBB(center, center).grow(radius)).filterNotNull().forEach {
        insertEntity(it)
    }
}


/** このメソッドはサーバーワールドのスレッドからしか呼び出せません。 */
fun FairyCrystalDropEnvironment.getDropTable(rank: Int, commonBoost: Double, rareBoost: Double): List<WeightedItem<ItemStack>> {
    val dropTable = FairyCrystalDrop.dropHandlers
        .filter { it.predicate(this) }
        .map {
            val boost = when (it.drop.dropCategory) {
                DropCategory.COMMON -> commonBoost
                DropCategory.RARE -> rareBoost
                else -> 1.0
            }
            WeightedItem(it.drop.getItemStack(rank), it.drop.weight * boost)
        }
        .unique { a, b -> ItemStack.areItemStacksEqualUsingNBTShareTag(a, b) }
    val totalWeight = dropTable.totalWeight
    return if (totalWeight < 1) {// 1に満たない場合はairを入れて詰める
        dropTable + WeightedItem(FairyTypes.instance.air.main.createItemStack(), 1 - totalWeight)
    } else {
        dropTable
    }
}


interface IDrop {
    fun getItemStack(rank: Int): ItemStack
    val dropCategory: DropCategory
    val weight: Double
}

class DropFixed(
    val bundle: RankedFairyTypeBundle,
    override val dropCategory: DropCategory,
    override val weight: Double
) : IDrop {
    override fun getItemStack(rank: Int) = bundle[rank].createItemStack()
}


class DropHandler(val drop: IDrop, val predicate: FairyCrystalDropEnvironment.() -> Boolean)
