package miragefairy2019.modkt.api.oreseeddrop

import miragefairy2019.mod.lib.WeightedRandom
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

object ApiOreSeedDrop {
    lateinit var oreSeedDropRegistry: IOreSeedDropRegistry
}

interface IOreSeedDropRegistry {
    fun register(handler: IOreSeedDropHandler)
    fun getDropList(environment: OreSeedDropEnvironment): List<OreSeedDrop>
    fun drop(environment: OreSeedDropEnvironment, random: Random): IBlockState? {
        val dropList = getDropList(environment)
        if (random.nextDouble() < (1.0 - WeightedRandom.getTotalWeight(dropList)).coerceAtLeast(0.0)) return null
        return WeightedRandom.getRandomItem(random, dropList).orElse(null)?.let { it() }
    }
}

typealias IOreSeedDropHandler = (OreSeedDropEnvironment) -> OreSeedDrop?

data class OreSeedDropEnvironment(val type: EnumOreSeedType, val shape: EnumOreSeedShape, val world: World, val blockPos: BlockPos)

enum class EnumOreSeedType {
    STONE, NETHERRACK, END_STONE,
}

enum class EnumOreSeedShape {
    TINY, LAPIS, DIAMOND, IRON, MEDIUM, LARGE, COAL, HUGE,
    STRING, HORIZONTAL, VERTICAL, POINT, STAR, RING, PYRAMID, CUBE,
}

typealias OreSeedDrop = WeightedRandom.Item<() -> IBlockState>

// TODO to typealias
interface IOreSeedDropRequirement {
    fun test(environment: OreSeedDropEnvironment): Boolean
}
