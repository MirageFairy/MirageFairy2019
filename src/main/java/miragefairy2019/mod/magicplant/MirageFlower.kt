package miragefairy2019.mod.magicplant

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.Mana
import miragefairy2019.common.toOreName
import miragefairy2019.lib.erg
import miragefairy2019.lib.mana
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.libkt.BiomeDecoratorFlowers
import miragefairy2019.libkt.WorldGenBush
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.FairyCrystal
import miragefairy2019.mod.artifacts.FairyMaterialCard
import miragefairy2019.mod.artifacts.createItemStack
import miragefairy2019.mod.fairy.getVariant
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.material.CompressedMaterials
import miragefairy2019.mod.material.EnumVariantMaterials1
import mirrg.kotlin.hydrogen.atLeast
import net.minecraft.block.BlockFarmland
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.EnumSkyBlock
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.terraingen.DecorateBiomeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.Random

lateinit var blockMirageFlower: () -> BlockMirageFlower
lateinit var itemMirageFlowerSeeds: () -> ItemMagicPlantSeed

val mirageFlowerModule = module {

    // ブロック登録
    blockMirageFlower = block({ BlockMirageFlower() }, "mirage_flower") {
        setUnlocalizedName("mirageFlower")
        makeBlockStates {
            DataBlockStates(variants = (0..3).associate { age -> "age=$age" to DataBlockState("miragefairy2019:mirage_flower_age$age") })
        }
    }

    // ブロックモデル生成
    run {
        fun makeBlockModel(name: String) = makeBlockModel(name) {
            DataModel(
                parent = "block/cross",
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/$name",
                    "cross" to "miragefairy2019:blocks/$name"
                )
            )
        }
        makeBlockModel("mirage_flower_age0")
        makeBlockModel("mirage_flower_age1")
        makeBlockModel("mirage_flower_age2")
        makeBlockModel("mirage_flower_age3")
    }

    // 種アイテム登録
    itemMirageFlowerSeeds = item({ ItemMagicPlantSeed(blockMirageFlower()) }, "mirage_flower_seeds") {
        setUnlocalizedName("mirageFlowerSeeds")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        makeItemModel { generated }
    }

    // 地形生成
    onHookDecorator {
        val biomeDecorators = listOf(

            // どこでも湧く
            BiomeDecoratorFlowers(WorldGenBush(blockMirageFlower(), blockMirageFlower().getState(3)).apply {
                blockCountMin = 1
                blockCountMax = 3
            }, 0.01) { true },

            // 山岳のみ
            BiomeDecoratorFlowers(WorldGenBush(blockMirageFlower(), blockMirageFlower().getState(3)).apply {
                blockCountMin = 1
                blockCountMax = 10
            }, 0.1) { BiomeDictionary.hasType(it, BiomeDictionary.Type.MOUNTAIN); },

            // 森林のみ
            BiomeDecoratorFlowers(WorldGenBush(blockMirageFlower(), blockMirageFlower().getState(3)).apply {
                blockCountMin = 1
                blockCountMax = 10
            }, 0.5) { BiomeDictionary.hasType(it, BiomeDictionary.Type.FOREST); }

        )
        MinecraftForge.EVENT_BUS.register(object {
            @SubscribeEvent
            fun handle(event: DecorateBiomeEvent.Post) = biomeDecorators.forEach { it.decorate(event) }
        })
    }

    // 雑草が種をドロップ
    onAddRecipe { MinecraftForge.addGrassSeed(ItemStack(itemMirageFlowerSeeds()), 1) }

}

val mirageFlowerGrowthHandlers = listOf(

    // 何もしなくても25回に1回の割合で成長する
    IGrowthHandler { world, blockPos ->
        listOf(GrowthRateModifier(textComponent { "Base Rate"() }, 0.04))
    },

    // 人工光が当たっているなら加点
    IGrowthHandler { world, blockPos ->
        listOf(GrowthRateModifier(textComponent { "Block Light Bonus"() }, world.getLightFor(EnumSkyBlock.BLOCK, blockPos).let {
            when {
                it >= 13 -> 1.2
                it >= 9 -> 1.1
                else -> 1.0
            }
        }))
    },

    // 太陽光が当たっているなら加点
    IGrowthHandler { world, blockPos ->
        listOf(GrowthRateModifier(textComponent { "Sky Light Bonus"() }, world.getLightFor(EnumSkyBlock.SKY, blockPos).let {
            when {
                it >= 15 -> 1.1
                it >= 9 -> 1.05
                else -> 1.0
            }
        }))
    },

    // 空が見えるなら加点
    IGrowthHandler { world, blockPos ->
        listOf(GrowthRateModifier(textComponent { "Sky Bonus"() }, if (world.canSeeSky(blockPos)) 1.1 else 1.0))
    },

    // 地面加点
    IGrowthHandler { world, blockPos ->
        listOf(GrowthRateModifier(textComponent { "Ground Bonus"() }, world.getBlockState(blockPos.down()).let { blockState ->
            var bonus = 0.5

            // 妖精による判定
            run noFairy@{
                val block = blockState.block
                if (block !is BlockMagicPlant) return@noFairy

                // 真下のブロックに紐づけられた妖精のリスト
                val entries = FairySelector().blockState(blockState).allMatch().withoutPartiallyMatch.primaries
                if (entries.isEmpty()) return@noFairy // 関連付けられた妖精が居ない場合は無視

                // 最も大きな補正値
                val growthRateInFloor = entries.map { block.getGrowthFactorInFloor(it.fairyCard.getVariant()) }.max()!!

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
    },

    // バイオーム加点
    IGrowthHandler { world, blockPos ->
        listOf(GrowthRateModifier(textComponent { "Biome Bonus"() }, world.getBiome(blockPos).let { biome ->
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

)

class BlockMirageFlower : BlockMagicPlant(3) {

    override val boundingBoxList = listOf(
        AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
    )

    override fun getGrowthFactorInFloor(fairySpec: IFairySpec) = fairySpec.mana(Mana.SHINE) * fairySpec.erg(Erg.CRYSTAL) / 100.0 * 3

    override fun getAgeAfterPick(age: Int) = if (age == maxAge) 1 else null

    override fun canGrow(age: Int) = age != maxAge

    override fun grow(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        repeat(random.randomInt(mirageFlowerGrowthHandlers.getGrowthRateModifiers(world, blockPos).growthRate)) {
            val blockState2 = world.getBlockState(blockPos)
            if (!canGrow(getAge(blockState2))) return
            world.setBlockState(blockPos, defaultState.withProperty(AGE, getAge(blockState2) + 1), 2)
        }
    }

    override fun getSeed() = itemMirageFlowerSeeds().createItemStack()

    override fun getDrops(age: Int, random: Random, fortune: Int, isBreaking: Boolean) = mutableListOf<ItemStack>().also { drops ->
        if (isBreaking) drops.drop(random, 1.0) { itemMirageFlowerSeeds().createItemStack(it) } // 破壊時、確定で種1個ドロップ
        if (isBreaking && age >= 2) drops.drop(random, 1 + fortune * 0.2) { FairyMaterialCard.MIRAGE_FLOWER_LEAF.createItemStack(it) } // 破壊時、サイズ2以上で茎
        if (age >= 3) drops.drop(random, fortune * 0.01) { itemMirageFlowerSeeds().createItemStack(it) } // 完全成長時、低確率で追加の種
        if (age >= 3) drops.drop(random, 1 + fortune * 0.5) { FairyCrystal.variantFairyCrystal().createItemStack(it) } // 完全成長時、フェアリークリスタル
        if (age >= 3) drops.drop(random, 1 + fortune * 0.5) { "dustTinyMiragium".toOreName().copyItemStack(it) } // 完全成長時、ミラジウムの微粉
    }

    override fun getExpDrop(age: Int, random: Random, fortune: Int, isBreaking: Boolean) = when (age) {
        3 -> if (isBreaking) 2 else 1
        2 -> if (isBreaking) 1 else 0
        else -> 0
    }

}
