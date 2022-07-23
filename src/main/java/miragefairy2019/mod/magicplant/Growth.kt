package miragefairy2019.mod.magicplant

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.Mana
import miragefairy2019.lib.erg
import miragefairy2019.lib.mana
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.fairy.getVariant
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.material.CompressedMaterials
import miragefairy2019.mod.material.EnumVariantMaterials1
import mirrg.kotlin.hydrogen.atLeast
import net.minecraft.block.BlockFarmland
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.EnumSkyBlock
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary

class GrowthRateModifier(val title: ITextComponent, val factor: Double)

interface IGrowthHandler {
    fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier>
}

val mirageFlowerGrowthHandlers = listOf(

    // 何もしなくても25回に1回の割合で成長する
    object : IGrowthHandler {
        override fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier> {
            return listOf(GrowthRateModifier(textComponent { "Base Rate"() }, 0.04))
        }
    },

    // 人工光が当たっているなら加点
    object : IGrowthHandler {
        override fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier> {
            return listOf(GrowthRateModifier(textComponent { "Block Light Bonus"() }, world.getLightFor(EnumSkyBlock.BLOCK, blockPos).let {
                when {
                    it >= 13 -> 1.2
                    it >= 9 -> 1.1
                    else -> 1.0
                }
            }))
        }
    },

    // 太陽光が当たっているなら加点
    object : IGrowthHandler {
        override fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier> {
            return listOf(GrowthRateModifier(textComponent { "Sky Light Bonus"() }, world.getLightFor(EnumSkyBlock.SKY, blockPos).let {
                when {
                    it >= 15 -> 1.1
                    it >= 9 -> 1.05
                    else -> 1.0
                }
            }))
        }
    },

    // 空が見えるなら加点
    object : IGrowthHandler {
        override fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier> {
            if (world.canSeeSky(blockPos)) return listOf(GrowthRateModifier(textComponent { "Sky Bonus"() }, 1.1))
            return listOf()
        }
    },

    // 地面加点
    object : IGrowthHandler {
        override fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier> {
            return listOf(GrowthRateModifier(textComponent { "Ground Bonus"() }, world.getBlockState(blockPos.down()).let { blockState ->
                var bonus = 0.5

                // 妖精による判定
                run noFairy@{

                    // 真下のブロックに紐づけられた妖精のリスト
                    val entries = FairySelector().blockState(blockState).allMatch().withoutPartiallyMatch.primaries
                    if (entries.isEmpty()) return@noFairy // 関連付けられた妖精が居ない場合は無視

                    // 最も大きな補正値
                    val growthRateInFloor = entries.map { getGrowthRateInFloor(it.fairyCard.getVariant()) }.max()!!

                    bonus = bonus atLeast growthRateInFloor
                }

                // 特定ブロックによる判定
                if (blockState.block === Blocks.GRASS) bonus = bonus atLeast 1.0
                if (blockState.block === Blocks.DIRT) bonus = bonus atLeast 1.1
                if (blockState.block === Blocks.FARMLAND) {
                    bonus = bonus atLeast 1.2
                    if (blockState.getValue(BlockFarmland.MOISTURE) > 0) bonus = bonus atLeast 1.3 // 耕土が湿っているなら加点
                }
                if (blockState === CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.APATITE_BLOCK)) bonus = bonus atLeast 1.5
                if (blockState === CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.FLUORITE_BLOCK)) bonus = bonus atLeast 2.0
                if (blockState === CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.SULFUR_BLOCK)) bonus = bonus atLeast 1.5
                if (blockState === CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.CINNABAR_BLOCK)) bonus = bonus atLeast 2.0
                if (blockState === CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MOONSTONE_BLOCK)) bonus = bonus atLeast 3.0
                if (blockState === CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MAGNETITE_BLOCK)) bonus = bonus atLeast 1.2

                bonus
            }))
        }
    },

    // バイオーム加点
    object : IGrowthHandler {
        override fun getGrowthRateModifiers(world: World, blockPos: BlockPos): List<GrowthRateModifier> {
            return listOf(GrowthRateModifier(textComponent { "Biome Bonus"() }, world.getBiome(blockPos).let { biome ->
                when {
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) -> 1.3
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.MAGICAL) -> 1.3
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) -> 1.2
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) -> 1.2
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) -> 1.1
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) -> 1.1
                    else -> 1.0
                }
            }))
        }
    }

)

fun List<IGrowthHandler>.calculateGrowthRate(world: World, blockPos: BlockPos) = this.flatMap { it.getGrowthRateModifiers(world, blockPos) }

val List<GrowthRateModifier>.growthRate get() = fold(1.0) { a, b -> a * b.factor }

fun getGrowthRateInFloor(fairySpec: IFairySpec) = fairySpec.mana(Mana.SHINE) * fairySpec.erg(Erg.CRYSTAL) / 100.0 * 3
