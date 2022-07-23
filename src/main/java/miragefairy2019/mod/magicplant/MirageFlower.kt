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
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.FairyCrystal
import miragefairy2019.mod.artifacts.FairyMaterialCard
import miragefairy2019.mod.artifacts.get
import miragefairy2019.mod.artifacts.itemFairyMaterials
import miragefairy2019.mod.fairy.getVariant
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.material.CompressedMaterials
import miragefairy2019.mod.material.EnumVariantMaterials1
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.or
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Block
import net.minecraft.block.BlockFarmland
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.EnumSkyBlock
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.terraingen.DecorateBiomeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.Random

lateinit var blockMirageFlower: () -> BlockMirageFlower
lateinit var itemMirageFlowerSeeds: () -> ItemMirageFlowerSeeds<BlockMirageFlower>

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
    itemMirageFlowerSeeds = item({ ItemMirageFlowerSeeds(blockMirageFlower()) }, "mirage_flower_seeds") {
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

val IFairySpec.mirageFlowerGrowthFactorInFloor get() = mana(Mana.SHINE) * erg(Erg.CRYSTAL) / 100.0 * 3

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
            return listOf(GrowthRateModifier(textComponent { "Sky Bonus"() }, if (world.canSeeSky(blockPos)) 1.1 else 1.0))
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
                    val growthRateInFloor = entries.map { it.fairyCard.getVariant().mirageFlowerGrowthFactorInFloor }.max()!!

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

class BlockMirageFlower : BlockMagicPlant() {
    companion object {
        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)
        private val AABB_STAGE0 = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0)
        private val AABB_STAGE1 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0)
        private val AABB_STAGE2 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
        private val AABB_STAGE3 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
    }


    init {
        defaultState = blockState.baseState.withProperty(AGE, 0)
    }


    // state

    override fun getMetaFromState(state: IBlockState) = state.getValue(AGE).coerceIn(0, 3)
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(AGE, meta.coerceIn(0, 3))
    override fun createBlockState() = BlockStateContainer(this, AGE)
    fun getState(age: Int): IBlockState = defaultState.withProperty(AGE, age)


    // 当たり判定

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = when (getAge(state)) {
        0 -> AABB_STAGE0
        1 -> AABB_STAGE1
        2 -> AABB_STAGE2
        3 -> AABB_STAGE3
        else -> AABB_STAGE3
    }


    // 動作

    fun getAge(state: IBlockState): Int = state.getValue(AGE)

    override fun isMaxAge(state: IBlockState) = getAge(state) == 3

    override fun grow(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        val rate = mirageFlowerGrowthHandlers.getGrowthRateModifiers(worldIn, pos).growthRate
        repeat(rand.randomInt(rate)) {
            if (!isMaxAge(state)) worldIn.setBlockState(pos, defaultState.withProperty(AGE, getAge(state) + 1), 2)
        }
    }


    // ドロップ

    override fun getSeed() = ItemStack(itemMirageFlowerSeeds())

    /*
     * Ageが最大のとき、種を1個ドロップする。
     * 幸運Lv1につき種のドロップ数が1%増える。
     * 地面破壊ドロップでも適用される。
     */
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int, isBreaking: Boolean) {
        val random = if (world is World) world.rand else Random()

        // 種1個は確定でドロップ
        if (isBreaking) drops += ItemStack(itemMirageFlowerSeeds())
        // サイズが2以上なら確定で茎をドロップ
        if (isBreaking && getAge(state) >= 2) repeat(random.randomInt(1 + fortune * 0.2)) { drops += itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_LEAF].createItemStack() }
        // 追加の種
        if (getAge(state) >= 3) repeat(random.randomInt(fortune * 0.01)) { drops += ItemStack(itemMirageFlowerSeeds()) }
        // クリスタル
        if (getAge(state) >= 3) repeat(random.randomInt(1 + fortune * 0.5)) { drops += FairyCrystal.variantFairyCrystal().createItemStack() }
        // ミラジウム
        if (getAge(state) >= 3) repeat(random.randomInt(1 + fortune * 0.5)) a@{ drops += "dustTinyMiragium".toOreName().copyItemStack().or { return@a }.copy() }
    }

    override fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int, isBreaking: Boolean) = if (isBreaking) when {
        getAge(state) >= 3 -> 2
        getAge(state) >= 2 -> 1
        else -> 0
    } else when {
        getAge(state) >= 3 -> 1
        else -> 0
    }

}

class ItemMirageFlowerSeeds<T>(private val block: T) : Item(), IPlantable where T : Block, T : IPlantable {
    // 使われるとその場に植物を設置する。
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemStack = player.getHeldItem(hand)
        val blockState = world.getBlockState(pos)
        if (facing != EnumFacing.UP) return EnumActionResult.FAIL // ブロックの上面にのみ使用可能
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemStack)) return EnumActionResult.FAIL // プレイヤーが編集不可能な場合は失敗
        if (!blockState.block.canSustainPlant(blockState, world, pos, EnumFacing.UP, block)) return EnumActionResult.FAIL // ブロックがその場所に滞在できないとだめ
        if (!world.isAirBlock(pos.up())) return EnumActionResult.FAIL // 真上が空気出ないとだめ

        world.setBlockState(pos.up(), getPlant(world, pos))
        if (player is EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger(player, pos.up(), itemStack)
        itemStack.shrink(1)
        return EnumActionResult.SUCCESS
    }

    override fun getPlantType(world: IBlockAccess, pos: BlockPos) = EnumPlantType.Plains // 常に草の上に蒔ける
    override fun getPlant(world: IBlockAccess, pos: BlockPos): IBlockState = blockMirageFlower().defaultState // 常にAge0のミラ花を与える
}
