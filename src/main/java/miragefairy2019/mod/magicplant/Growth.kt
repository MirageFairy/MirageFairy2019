package miragefairy2019.mod.magicplant

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.Mana
import miragefairy2019.lib.erg
import miragefairy2019.lib.mana
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

class GrowthRateModifier(val title: ITextComponent, val factor: Double)

interface IGrowthHandler {
    fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier>
}

fun List<IGrowthHandler>.getGrowthRateModifiers(world: World, blockPos: BlockPos) = this.flatMap { it.getGrowthRateModifiers(world, blockPos) }

val List<GrowthRateModifier>.growthRate get() = fold(1.0) { a, b -> a * b.factor }

fun getGrowthRateInFloor(fairySpec: IFairySpec) = fairySpec.mana(Mana.SHINE) * fairySpec.erg(Erg.CRYSTAL) / 100.0 * 3
