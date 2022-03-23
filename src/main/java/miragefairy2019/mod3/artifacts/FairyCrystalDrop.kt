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

    val dropHandlers = mutableListOf<DropHandler>()

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
        val blocks = mutableSetOf<Block>()
        val blockStates = mutableSetOf<IBlockState>()
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
                    blockStates += blockState
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
        world.getBiome(pos).let { biome ->
            biomes += biome
            biomeTypes += BiomeDictionary.getTypes(biome)
        }

        // エンティティ
        val entities = world.getEntitiesWithinAABB(Entity::class.java, axisAlignedBB(player.positionVector, player.positionVector).grow(10.0)).filterNotNull().toSet()
        entities.forEach { classEntities += it.javaClass }


        // リスト作成
        val dropArguments = DropArguments(
            player = player,
            world = world,
            pos = pos,
            hand = hand,
            facing = facing,
            hitX = hitX,
            hitY = hitY,
            hitZ = hitZ,
            blocks = blocks,
            blockStates = blockStates,
            items = items,
            itemStacks = itemStacks,
            biomes = biomes,
            biomeTypes = biomeTypes,
            classEntities = classEntities,
            entities = entities
        )
        val dropTable = dropHandlers
            .filter { it.predicate(dropArguments) }
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


class DropArguments(
    val player: EntityPlayer,
    val world: World,
    val pos: BlockPos,
    val hand: EnumHand,
    val facing: EnumFacing,
    val hitX: Float,
    val hitY: Float,
    val hitZ: Float,
    val blocks: Set<Block>,
    val blockStates: Set<IBlockState>,
    val items: Set<Item>,
    val itemStacks: Set<ItemStack>,
    val biomes: Set<Biome>,
    val biomeTypes: Set<BiomeDictionary.Type>,
    val classEntities: Set<Class<out Entity>>,
    val entities: Set<Entity>
)

class DropHandler(val drop: IDrop, val predicate: DropArguments.() -> Boolean)
