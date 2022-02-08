package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.block
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.totalWeight
import miragefairy2019.libkt.unique
import miragefairy2019.mod.api.fairycrystal.DropCategory
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop
import miragefairy2019.mod3.fairy.FairyTypes
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
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary

abstract class FairyCrystalDropper {
    /** このメソッドはサーバーワールドのスレッドからしか呼び出せません。 */
    abstract val dropList: List<IRightClickDrop>

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
        val entities = world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(player.positionVector, player.positionVector).grow(10.0))
        entities.forEach { classEntities += it.javaClass }


        // リスト作成
        val dropTable = dropList
            .filter { entry ->
                when {
                    entry.testUseItem(player, world, pos, hand, facing, hitX, hitY, hitZ) -> true
                    entry.testWorld(world, pos2) -> true
                    blocks.any { entry.testBlock(it) } -> true
                    blockStates.any { entry.testBlockState(world, it.second, it.first) } -> true
                    items.any { entry.testItem(it) } -> true
                    itemStacks.any { entry.testItemStack(it) } -> true
                    biomes.any { entry.testBiome(it) } -> true
                    biomeTypes.any { entry.testBiomeType(it) } -> true
                    classEntities.any { entry.testClassEntity(it) } -> true
                    entities.any { entry.testEntity(it) } -> true
                    else -> false
                }
            }
            .map { WeightedItem(it.drop.getItemStack(rank), it.drop.weight * if (it.drop.dropCategory == DropCategory.RARE) rareBoost else 1.0) }
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
        rareBoost: Double
    ) = getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ, rank, rareBoost).getRandomItem(world.rand)
}