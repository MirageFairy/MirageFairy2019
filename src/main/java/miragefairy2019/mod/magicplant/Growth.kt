package miragefairy2019.mod.magicplant

import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

class GrowthRateModifier(val title: ITextComponent, val factor: Double)

interface IGrowthHandler {
    fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier>
}

fun IGrowthHandler(block: (world: World, blockPos: BlockPos) -> List<GrowthRateModifier>) = object : IGrowthHandler {
    override fun getGrowthRateModifiers(world: World, blockPos: BlockPos) = block(world, blockPos)
}

fun List<IGrowthHandler>.getGrowthRateModifiers(world: World, blockPos: BlockPos) = this.flatMap { it.getGrowthRateModifiers(world, blockPos) }

val List<GrowthRateModifier>.growthRate get() = fold(1.0) { a, b -> a * b.factor }
