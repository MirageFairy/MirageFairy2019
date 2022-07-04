package miragefairy2019.mod.oreseed

import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.totalWeight
import mirrg.kotlin.hydrogen.atLeast
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
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
        if (random.nextDouble() < (1.0 - dropList.totalWeight) atLeast 0.0) return null
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

typealias OreSeedDrop = WeightedItem<() -> IBlockState>

interface IOreSeedDropRequirement {
    fun test(environment: OreSeedDropEnvironment): Boolean
    fun getDescriptions(): Iterable<String>
}

class OreSeedDropRegistry : IOreSeedDropRegistry {
    override val recipes = mutableListOf<IOreSeedDropHandler>()
    override fun register(handler: IOreSeedDropHandler) = run<Unit> { recipes.add(handler) }
    override fun getDropList(environment: OreSeedDropEnvironment) = recipes.mapNotNull { it.getDrop(environment) }
}

fun IOreSeedDropRegistry.register(
    type: EnumOreSeedType,
    shape: EnumOreSeedShape,
    weight: Double,
    output: Pair<() -> IBlockState, () -> ItemStack>,
    vararg requirements: IOreSeedDropRequirement
) {
    register(object : IOreSeedDropHandler {
        override fun getDrop(environment: OreSeedDropEnvironment): OreSeedDrop? {
            if (environment.type != type) return null
            if (environment.shape != shape) return null
            requirements.forEach {
                if (!it.test(environment)) return null
            }
            return WeightedItem(output.first, weight)
        }

        override fun getType() = type
        override fun getShape() = shape
        override fun getWeight() = weight
        override fun getRequirements() = requirements.flatMap { it.getDescriptions() }
        override fun getOutputItemStacks() = listOf(output.second())
    })
}

class OreSeedDropRegistryScope(val registry: IOreSeedDropRegistry) {
    operator fun EnumOreSeedType.invoke(block: TypedOreSeedDropRegistryScope.() -> Unit) = TypedOreSeedDropRegistryScope(this).block()
    inner class TypedOreSeedDropRegistryScope(val type: EnumOreSeedType) {
        fun register(
            shape: EnumOreSeedShape,
            weight: Double,
            output: Pair<() -> IBlockState, () -> ItemStack>,
            vararg generationConditions: IOreSeedDropRequirement
        ) {
            registry.register(type, shape, weight, output, *generationConditions)
        }
    }
}

operator fun IOreSeedDropRegistry.invoke(block: OreSeedDropRegistryScope.() -> Unit) = OreSeedDropRegistryScope(this).block()


object OreSeedDropRequirements {
    fun minY(minY: Int): IOreSeedDropRequirement = object : IOreSeedDropRequirement {
        override fun test(environment: OreSeedDropEnvironment) = environment.blockPos.y >= minY
        override fun getDescriptions() = listOf("Min Y: $minY")
    }

    fun maxY(maxY: Int): IOreSeedDropRequirement = object : IOreSeedDropRequirement {
        override fun test(environment: OreSeedDropEnvironment) = environment.blockPos.y <= maxY
        override fun getDescriptions() = listOf("Max Y: $maxY")
    }

    fun since(offsetDateTime: OffsetDateTime): IOreSeedDropRequirement = object : IOreSeedDropRequirement {
        override fun test(environment: OreSeedDropEnvironment) = OffsetDateTime.now(offsetDateTime.offset) >= offsetDateTime
        override fun getDescriptions() = listOf("Since: ${offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)}")
    }
}
