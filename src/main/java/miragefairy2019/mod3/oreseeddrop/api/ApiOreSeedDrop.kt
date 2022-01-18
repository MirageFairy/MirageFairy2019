package miragefairy2019.mod3.oreseeddrop.api

import miragefairy2019.mod.lib.WeightedRandom
import miragefairy2019.mod.lib.getRandomItem
import miragefairy2019.mod.lib.totalWeight
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.Random

object ApiOreSeedDrop {
    lateinit var oreSeedDropRegistry: IOreSeedDropRegistry
}

interface IOreSeedDropRegistry {
    fun register(handler: IOreSeedDropHandler)
    val recipes: Iterable<IOreSeedDropHandler>
    fun getDropList(environment: OreSeedDropEnvironment): List<OreSeedDrop>
    fun drop(environment: OreSeedDropEnvironment, random: Random): IBlockState? {
        val dropList = getDropList(environment)
        if (random.nextDouble() < (1.0 - dropList.totalWeight).coerceAtLeast(0.0)) return null
        return dropList.getRandomItem(random)?.invoke()
    }
}

interface IOreSeedDropHandler {
    fun getDrop(environment: OreSeedDropEnvironment): OreSeedDrop?
    fun getType(): EnumOreSeedType
    fun getShape(): EnumOreSeedShape
    fun getWeight(): Double
    fun getRequirements(): Iterable<String>
    fun getOutputItemStacks(): Iterable<ItemStack>
}

data class OreSeedDropEnvironment(val type: EnumOreSeedType, val shape: EnumOreSeedShape, val world: World, val blockPos: BlockPos)

enum class EnumOreSeedType(
    private val sBlockState: () -> IBlockState,
    private val sItemStack: () -> ItemStack
) {
    STONE({ Blocks.STONE.defaultState }, { ItemStack(Blocks.STONE) }),
    NETHERRACK({ Blocks.NETHERRACK.defaultState }, { ItemStack(Blocks.NETHERRACK) }),
    END_STONE({ Blocks.END_STONE.defaultState }, { ItemStack(Blocks.END_STONE) }),
    ;

    fun getBlockState() = sBlockState()
    fun getItemStack() = sItemStack()
}

enum class EnumOreSeedShape {
    TINY, LAPIS, DIAMOND, IRON, MEDIUM, LARGE, COAL, HUGE,
    STRING, HORIZONTAL, VERTICAL, POINT, STAR, RING, PYRAMID, CUBE,
}

typealias OreSeedDrop = WeightedRandom.Item<() -> IBlockState>

interface IOreSeedDropRequirement {
    fun test(environment: OreSeedDropEnvironment): Boolean
    fun getDescriptions(): Iterable<String>
}
