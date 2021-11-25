package miragefairy2019.mod3.fairylogdrop

import miragefairy2019.mod.lib.WeightedRandom
import miragefairy2019.mod3.fairylogdrop.api.IFairyLogDropCondition
import miragefairy2019.mod3.fairylogdrop.api.IFairyLogDropRecipe
import miragefairy2019.mod3.fairylogdrop.api.IFairyLogDropRegistry
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.util.Random

class FairyLogDropRegistry : IFairyLogDropRegistry {
    private val recipes = mutableListOf<IFairyLogDropRecipe>()
    override fun addRecipe(recipe: IFairyLogDropRecipe) = run { recipes += recipe }
    override fun getRecipes() = recipes
    override fun drop(world: World, blockPos: BlockPos, random: Random): ItemStack? {
        val drop = WeightedRandom.getRandomItem(random, recipes.filter { r -> r.conditions.all { it.test(world, blockPos) } }.map { WeightedRandom.Item(it, it.rate) })
        return drop.orElse(null)?.itemStackOutput
    }
}

class FairyLogDropRecipe(private val rate: Double, private val sItemStackOutput: () -> ItemStack) : IFairyLogDropRecipe {
    private val conditions = mutableListOf<IFairyLogDropCondition>()
    override fun getRate() = rate
    override fun getItemStackOutput() = sItemStackOutput()
    fun addCondition(condition: IFairyLogDropCondition) = run { conditions += condition }
    override fun getConditions() = conditions
}

class FairyLogDropConditionCanRain : IFairyLogDropCondition {
    override fun test(world: World, blockPos: BlockPos) = world.getBiome(blockPos).canRain()
    override fun getLocalizedDescription() = "RAINY"
}

class FairyLogDropConditionDoesNotHaveBiomeType(private val biome: BiomeDictionary.Type) : IFairyLogDropCondition {
    override fun test(world: World, blockPos: BlockPos) = !BiomeDictionary.hasType(world.getBiome(blockPos), biome)
    override fun getLocalizedDescription() = "NOT(${biome.name})"
}

class FairyLogDropConditionHasBiomeType(private val biome: BiomeDictionary.Type) : IFairyLogDropCondition {
    override fun test(world: World, blockPos: BlockPos) = BiomeDictionary.hasType(world.getBiome(blockPos), biome)
    override fun getLocalizedDescription(): String = biome.name
}

class FairyLogDropConditionOverworld : IFairyLogDropCondition {
    override fun test(world: World, blockPos: BlockPos) = world.provider.isSurfaceWorld
    override fun getLocalizedDescription() = "OVERWORLD"
}
