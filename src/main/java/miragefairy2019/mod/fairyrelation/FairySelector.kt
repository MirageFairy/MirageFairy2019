package miragefairy2019.mod.fairyrelation

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class FairySelector {
    private val entities = mutableSetOf<Entity>()
    private val blocks = mutableSetOf<Block>()
    private val blockStates = mutableSetOf<IBlockState>()
    private val items = mutableSetOf<Item>()
    private val itemStacks = mutableSetOf<ItemStack>()

    fun entity(entity: Entity): FairySelector {
        entities += entity
        return this
    }

    fun blockState(blockState: IBlockState): FairySelector {
        blockStates += blockState
        blocks += blockState.block
        val item = Item.getItemFromBlock(blockState.block)
        if (item != Items.AIR) items += item
        return this
    }

    fun itemStack(itemStack: ItemStack): FairySelector {
        itemStacks += itemStack
        items += itemStack.item
        val block = Block.getBlockFromItem(itemStack.item)
        if (block != Blocks.AIR) blocks += block
        return this
    }

    fun allMatch(): List<FairyRelationEntry<*>> = listOf(
        FairyRelationRegistries.entity.entries.filter { entities.any { entity -> it.key(entity) } },
        FairyRelationRegistries.block.entries.filter { it.key in blocks },
        FairyRelationRegistries.blockState.entries.filter { it.key in blockStates },
        FairyRelationRegistries.item.entries.filter { it.key in items },
        FairyRelationRegistries.itemStack.entries.filter { itemStacks.any { itemStack -> it.key(itemStack) } },
        FairyRelationRegistries.ingredient.entries.filter { itemStacks.any { itemStack -> it.key.test(itemStack) } }
    ).flatten()
}

/** [FairyRelationEntry.relevance]が1に満たないものを除外します。 */
val <T : FairyRelationEntry<*>> List<T>.withoutPartiallyMatch get() = filter { it.relevance >= 1.0 }

/** 最も[FairyRelationEntry.relevance]の大きなものの集合を返します。 */
val <T : FairyRelationEntry<*>> List<T>.primaries: List<T>
    get() {
        if (isEmpty()) return listOf() // 空の場合は空を返す
        val maxRelevance = map { it.relevance }.max()!!
        return filter { it.relevance == maxRelevance }
    }
