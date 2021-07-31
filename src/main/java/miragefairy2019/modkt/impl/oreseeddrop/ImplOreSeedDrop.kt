package miragefairy2019.modkt.impl.oreseeddrop

import miragefairy2019.mod.lib.WeightedRandom
import miragefairy2019.modkt.api.oreseeddrop.*
import net.minecraft.block.state.IBlockState

class OreSeedDropRegistry : IOreSeedDropRegistry {
    private val registry = mutableListOf<IOreSeedDropHandler>()
    override fun register(handler: IOreSeedDropHandler) = run<Unit> { registry.add(handler) }
    override fun getDropList(environment: OreSeedDropEnvironment) = registry.mapNotNull { it(environment) }
}

fun IOreSeedDropRegistry.register(type: EnumOreSeedType, shape: EnumOreSeedShape, weight: Double, block: () -> IBlockState, vararg generationConditions: IOreSeedDropRequirement) {
    register(fun(environment: OreSeedDropEnvironment): OreSeedDrop? {
        if (environment.type != type) return null
        if (environment.shape != shape) return null
        generationConditions.forEach {
            if (!it.test(environment)) return null
        }
        return WeightedRandom.Item(block, weight)
    })
}

class OreSeedDropRegistryScope(val registry: IOreSeedDropRegistry) {
    operator fun EnumOreSeedType.invoke(block: TypedOreSeedDropRegistryScope.() -> Unit) = TypedOreSeedDropRegistryScope(this).block()
    inner class TypedOreSeedDropRegistryScope(val type: EnumOreSeedType) {
        fun register(shape: EnumOreSeedShape, weight: Double, block: () -> IBlockState, vararg generationConditions: IOreSeedDropRequirement) {
            registry.register(type, shape, weight, block, *generationConditions)
        }
    }
}

operator fun IOreSeedDropRegistry.invoke(block: OreSeedDropRegistryScope.() -> Unit) = OreSeedDropRegistryScope(this).block()


object OreSeedDropRequirements {
    @JvmStatic // todo delete jvm
    fun minY(minY: Int): IOreSeedDropRequirement = object : IOreSeedDropRequirement {
        override fun test(environment: OreSeedDropEnvironment) = environment.blockPos.y >= minY
    }

    @JvmStatic // todo delete jvm
    fun maxY(maxY: Int): IOreSeedDropRequirement = object : IOreSeedDropRequirement {
        override fun test(environment: OreSeedDropEnvironment) = environment.blockPos.y <= maxY
    }
}
