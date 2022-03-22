package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.axisAlignedBB
import miragefairy2019.libkt.block
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.totalWeight
import miragefairy2019.libkt.unique
import miragefairy2019.mod3.fairy.FairyTypes
import miragefairy2019.mod3.fairy.RankedFairyTypeBundle
import mirrg.kotlin.castOrNull
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary

object FairyCrystalDrop {

    val useItemDropHandlers = mutableListOf<UseItemDropHandler>()
    val worldDropHandlers = mutableListOf<WorldDropHandler>()
    val blockDropHandlers = mutableListOf<BlockDropHandler>()
    val blockStateDropHandlers = mutableListOf<BlockStateDropHandler>()
    val itemDropHandlers = mutableListOf<ItemDropHandler>()
    val itemStackDropHandlers = mutableListOf<ItemStackDropHandler>()
    val biomeDropHandlers = mutableListOf<BiomeDropHandler>()
    val biomeTypeDropHandlers = mutableListOf<BiomeTypeDropHandler>()
    val classEntityDropHandlers = mutableListOf<ClassEntityDropHandler>()
    val entityDropHandlers = mutableListOf<EntityDropHandler>()

    /** このメソッドはサーバーワールドのスレッドからしか呼び出せません。 */
    fun getDropTable(
        player: EntityPlayer,
        world: World,
        pos: BlockPos,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        rank: Int,
        commonBoost: Double,
        rareBoost: Double
    ): List<WeightedItem<ItemStack>> {
        val pos2 = if (world.getBlockState(pos).isFullBlock) pos.offset(facing) else pos

        val blocks = mutableSetOf<Block>()
        val blockStates = mutableSetOf<Pair<IBlockState, BlockPos>>()
        val items = mutableSetOf<Item>()
        val itemStacks = mutableSetOf<ItemStack>()
        val biomes = mutableSetOf<Biome>()
        val biomeTypes = mutableSetOf<BiomeDictionary.Type>()
        val classEntities = mutableSetOf<Class<out Entity>>()

        fun insertItemStack(itemStack: ItemStack) {
            if (itemStack.isEmpty) return
            itemStacks += itemStack
            items += itemStack.item
            itemStack.item.block?.let { blocks += it }
        }


        // ワールドブロック
        (-2..2).forEach { xi ->
            (-2..2).forEach { yi ->
                (-2..2).forEach { zi ->
                    val pos3 = pos.add(xi, yi, zi)
                    val blockState = world.getBlockState(pos3)
                    blockStates += Pair(blockState, pos3)
                    blocks += blockState.block
                    world.getTileEntity(pos3)?.castOrNull<IInventory>()?.let { inventory ->
                        inventory.itemStacks.forEach {
                            insertItemStack(it)
                        }
                    }
                }
            }
        }

        // インベントリ
        listOf(player.inventory.mainInventory, player.inventory.armorInventory, player.inventory.offHandInventory).flatten().forEach {
            insertItemStack(it)
        }

        // バイオーム
        world.getBiome(pos2).let { biome ->
            biomes += biome
            biomeTypes += BiomeDictionary.getTypes(biome)
        }

        // エンティティ
        val entities = world.getEntitiesWithinAABB(Entity::class.java, axisAlignedBB(player.positionVector, player.positionVector).grow(10.0))
        entities.forEach { classEntities += it.javaClass }


        // リスト作成
        val drops = listOf(
            useItemDropHandlers.filter { dropHandler -> dropHandler.testUseItem(player, world, pos, hand, facing, hitX, hitY, hitZ) }.map { it.drop },
            worldDropHandlers.filter { dropHandler -> dropHandler.testWorld(world, pos2) }.map { it.drop },
            blockDropHandlers.filter { dropHandler -> blocks.any { dropHandler.testBlock(it) } }.map { it.drop },
            blockStateDropHandlers.filter { dropHandler -> blockStates.any { dropHandler.testBlockState(world, it.second, it.first) } }.map { it.drop },
            itemDropHandlers.filter { dropHandler -> items.any { dropHandler.testItem(it) } }.map { it.drop },
            itemStackDropHandlers.filter { dropHandler -> itemStacks.any { dropHandler.testItemStack(it) } }.map { it.drop },
            biomeDropHandlers.filter { dropHandler -> biomes.any { dropHandler.testBiome(it) } }.map { it.drop },
            biomeTypeDropHandlers.filter { dropHandler -> biomeTypes.any { dropHandler.testBiomeType(it) } }.map { it.drop },
            classEntityDropHandlers.filter { dropHandler -> classEntities.any { dropHandler.testClassEntity(it) } }.map { it.drop },
            entityDropHandlers.filter { dropHandler -> entities.any { dropHandler.testEntity(it) } }.map { it.drop }
        ).flatten()
        val dropTable = drops
            .map {
                val boost = when (it.dropCategory) {
                    DropCategory.COMMON -> commonBoost
                    DropCategory.RARE -> rareBoost
                    else -> 1.0
                }
                WeightedItem(it.getItemStack(rank), it.weight * boost)
            }
            .unique { a, b -> ItemStack.areItemStacksEqualUsingNBTShareTag(a, b) }


        val totalWeight = dropTable.totalWeight
        return if (totalWeight < 1) {// 1に満たない場合はairを入れて詰める
            dropTable + WeightedItem(FairyTypes.instance.air.main.createItemStack(), 1 - totalWeight)
        } else {
            dropTable
        }
    }

    /** このメソッドはサーバーワールドのスレッドからしか呼び出せません。 */
    fun drop(
        player: EntityPlayer,
        world: World,
        pos: BlockPos,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        rank: Int,
        commonBoost: Double,
        rareBoost: Double
    ) = getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ, rank, commonBoost, rareBoost).getRandomItem(world.rand)

}


enum class DropCategory { FIXED, COMMON, RARE }


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


interface UseItemDropHandler {
    val drop: IDrop
    fun testUseItem(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = false
}

interface WorldDropHandler {
    val drop: IDrop
    fun testWorld(world: World, pos: BlockPos) = false
}

interface BlockDropHandler {
    val drop: IDrop
    fun testBlock(block: Block) = false
}

interface BlockStateDropHandler {
    val drop: IDrop
    fun testBlockState(world: World, blockPos: BlockPos, blockState: IBlockState) = false
}

interface ItemDropHandler {
    val drop: IDrop
    fun testItem(item: Item) = false
}

interface ItemStackDropHandler {
    val drop: IDrop
    fun testItemStack(itemStack: ItemStack) = false
}

interface BiomeDropHandler {
    val drop: IDrop
    fun testBiome(biome: Biome) = false
}

interface BiomeTypeDropHandler {
    val drop: IDrop
    fun testBiomeType(biomeType: BiomeDictionary.Type) = false
}

interface ClassEntityDropHandler {
    val drop: IDrop
    fun testClassEntity(classEntity: Class<out Entity>) = false
}

interface EntityDropHandler {
    val drop: IDrop
    fun testEntity(entity: Entity) = false
}
