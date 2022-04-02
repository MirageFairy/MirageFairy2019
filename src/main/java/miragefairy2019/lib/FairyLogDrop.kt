package miragefairy2019.lib

import miragefairy2019.api.FairyLogDropRegistry
import miragefairy2019.api.IFairyLogDropRecipe
import miragefairy2019.api.IFairyLogDropRequirement
import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.textComponent
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.util.Random

// レシピ

class FairyLogDropRecipe(private val rate: Double, private val outputSupplier: () -> ItemStack) : IFairyLogDropRecipe {
    val requirements = mutableListOf<IFairyLogDropRequirement>()
    override fun getRequirements() = requirements.toNonNullList()
    override fun getOutput() = outputSupplier()
    override fun getRate() = rate
}


// 条件

class FairyLogDropRequirementCanRain : IFairyLogDropRequirement {
    override fun test(world: World, blockPos: BlockPos) = world.getBiome(blockPos).canRain()
    override fun getDescription() = textComponent { "RAINY"() }
}

class FairyLogDropRequirementDoesNotHaveBiomeType(private val biome: BiomeDictionary.Type) : IFairyLogDropRequirement {
    override fun test(world: World, blockPos: BlockPos) = !BiomeDictionary.hasType(world.getBiome(blockPos), biome)
    override fun getDescription() = textComponent { "NOT(${biome.name})"() }
}

class FairyLogDropRequirementHasBiomeType(private val biome: BiomeDictionary.Type) : IFairyLogDropRequirement {
    override fun test(world: World, blockPos: BlockPos) = BiomeDictionary.hasType(world.getBiome(blockPos), biome)
    override fun getDescription() = textComponent { biome.name() }
}

class FairyLogDropRequirementOverworld : IFairyLogDropRequirement {
    override fun test(world: World, blockPos: BlockPos) = world.provider.isSurfaceWorld
    override fun getDescription() = textComponent { "OVERWORLD"() }
}


// ユーティリティ

fun selectFairyLogDrop(world: World, blockPos: BlockPos, random: Random) = FairyLogDropRegistry.fairyLogDropRecipes
    .filter { recipe -> recipe.requirements.all { it.test(world, blockPos) } }
    .map { WeightedItem(it, it.rate) }
    .getRandomItem(random)?.output
