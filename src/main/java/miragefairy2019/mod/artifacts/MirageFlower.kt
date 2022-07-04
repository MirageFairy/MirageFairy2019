package miragefairy2019.mod.artifacts

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyType
import miragefairy2019.api.IPickExecutor
import miragefairy2019.api.IPickHandler
import miragefairy2019.api.PickHandlerRegistry
import miragefairy2019.common.toOreName
import miragefairy2019.lib.erg
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
import miragefairy2019.lib.shineEfficiency
import miragefairy2019.libkt.BiomeDecoratorFlowers
import miragefairy2019.libkt.WorldGenBush
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.material.CompressedMaterials
import miragefairy2019.mod.material.EnumVariantMaterials1
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.or
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Block
import net.minecraft.block.BlockBush
import net.minecraft.block.BlockFarmland
import net.minecraft.block.IGrowable
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
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

object MirageFlower {
    lateinit var blockMirageFlower: () -> BlockMirageFlower
    lateinit var itemMirageFlowerSeeds: () -> ItemMirageFlowerSeeds<BlockMirageFlower>
    val mirageFlowerModule = module {

        blockMirageFlower = block({ BlockMirageFlower() }, "mirage_flower") {
            setUnlocalizedName("mirageFlower")
            setCreativeTab { Main.creativeTab }
            onRegisterBlock {
                PickHandlerRegistry.pickHandlers += IPickHandler { world, blockPos, player ->
                    val blockState = world.getBlockState(blockPos)
                    val block = blockState.block as? BlockMirageFlower ?: return@IPickHandler null
                    if (!block.isMaxAge(blockState)) return@IPickHandler null
                    IPickExecutor { fortune -> block.tryPick(world, blockPos, player, fortune) }
                }
            }
            makeBlockStates(resourceName.path) {
                DataBlockStates(variants = (0..3).associate { age -> "age=$age" to DataBlockState("miragefairy2019:mirage_flower_age$age") })
            }
        }
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
        itemMirageFlowerSeeds = item({ ItemMirageFlowerSeeds(blockMirageFlower()) }, "mirage_flower_seeds") {
            setUnlocalizedName("mirageFlowerSeeds")
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation()
        }
        makeItemModel("mirage_flower_seeds") { generated }

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

        onAddRecipe { MinecraftForge.addGrassSeed(ItemStack(itemMirageFlowerSeeds()), 1) } // 雑草が種をドロップ

    }
}

fun calculateGrowthRate(world: World, blockPos: BlockPos): List<Pair<String, Double>> {
    val list = mutableListOf<Pair<String, Double>>()

    list += "Base Rate" to 0.04 // 何もしなくても25回に1回の割合で成長する

    // 人工光が当たっているなら加点
    list += "Block Light Bonus" to world.getLightFor(EnumSkyBlock.BLOCK, blockPos).let {
        when {
            it >= 13 -> 1.2
            it >= 9 -> 1.1
            else -> 1.0
        }
    }

    // 太陽光が当たっているなら加点
    list += "Sky Light Bonus" to world.getLightFor(EnumSkyBlock.SKY, blockPos).let {
        when {
            it >= 15 -> 1.1
            it >= 9 -> 1.05
            else -> 1.0
        }
    }

    if (world.canSeeSky(blockPos)) list += "Sky Bonus" to 1.1 // 空が見えるなら加点

    // 地面加点
    list += "Ground Bonus" to world.getBlockState(blockPos.down()).let { blockState ->
        var bonus = 0.5

        // 妖精による判定
        run noFairy@{

            // 真下のブロックに紐づけられた妖精のリスト
            val entries = FairySelector().blockState(blockState).allMatch().withoutPartiallyMatch.primaries
            if (entries.isEmpty()) return@noFairy // 関連付けられた妖精が居ない場合は無視

            // 最も大きな補正値
            val growthRateInFloor = entries.map { getGrowthRateInFloor(it.fairy.main.type) }.max()!!

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
    }

    // バイオーム加点
    list += "Biome Bonus" to world.getBiome(blockPos).let { biome ->
        when {
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) -> 1.3
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.MAGICAL) -> 1.3
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) -> 1.2
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) -> 1.2
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) -> 1.1
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) -> 1.1
            else -> 1.0
        }
    }

    return list
}

val List<Pair<String, Double>>.growthRate get() = fold(1.0) { a, b -> a * b.second }

fun getGrowthRateInFloor(fairyType: IFairyType) = fairyType.shineEfficiency * fairyType.erg(Erg.CRYSTAL) / 100.0 * 3

class BlockMirageFlower : BlockBush(Material.PLANTS), IGrowable {  // Solidであるマテリアルは耕土を破壊する
    init {
        // meta
        defaultState = blockState.baseState.withProperty(AGE, 0)
        // style
        soundType = SoundType.GLASS
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

    override fun canSustainBush(state: IBlockState) = state.isFullBlock || state.block === Blocks.FARMLAND
    fun getAge(state: IBlockState): Int = state.getValue(AGE)
    fun isMaxAge(state: IBlockState) = getAge(state) == 3
    fun grow(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random, rate: Double) {
        repeat(rand.randomInt(rate)) {
            if (!isMaxAge(state)) worldIn.setBlockState(pos, defaultState.withProperty(AGE, getAge(state) + 1), 2)
        }
    }

    // UpdateTickごとにAgeが1ずつ最大3まで増える。
    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        super.updateTick(worldIn, pos, state, rand)
        if (!worldIn.isAreaLoaded(pos, 1)) return
        grow(worldIn, pos, state, rand, calculateGrowthRate(worldIn, pos).growthRate)
    }

    // 骨粉をやると低確率で成長する。
    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) = grow(worldIn, pos, state, rand, calculateGrowthRate(worldIn, pos).growthRate)
    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean) = !isMaxAge(state)
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) = worldIn.rand.nextFloat() < 0.05


    // ドロップ

    // クリエイティブピックでの取得アイテム。
    override fun getItem(world: World, pos: BlockPos, state: IBlockState) = ItemStack(MirageFlower.itemMirageFlowerSeeds())

    /*
     * Ageが最大のとき、種を1個ドロップする。
     * 幸運Lv1につき種のドロップ数が1%増える。
     * 地面破壊ドロップでも適用される。
     */
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) = getDrops(drops, world, pos, state, fortune, true)

    private fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int, isBreaking: Boolean) {
        val random = if (world is World) world.rand else Random()

        // 種1個は確定でドロップ
        if (isBreaking) drops += ItemStack(MirageFlower.itemMirageFlowerSeeds())
        // サイズが2以上なら確定で茎をドロップ
        if (isBreaking && getAge(state) >= 2) repeat(random.randomInt(1 + fortune * 0.2)) { drops += FairyMaterials.itemFairyMaterials[EnumFairyMaterial.leafMirageFlower].createItemStack() }
        // 追加の種
        if (getAge(state) >= 3) repeat(random.randomInt(fortune * 0.01)) { drops += ItemStack(MirageFlower.itemMirageFlowerSeeds()) }
        // クリスタル
        if (getAge(state) >= 3) repeat(random.randomInt(1 + fortune * 0.5)) { drops += FairyCrystal.variantFairyCrystal().createItemStack() }
        // ミラジウム
        if (getAge(state) >= 3) repeat(random.randomInt(1 + fortune * 0.5)) a@{ drops += "dustTinyMiragium".toOreName().copyItemStack().or { return@a }.copy() }
    }

    // シルクタッチ無効。
    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = false


    // 経験値ドロップ
    override fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int) = getExpDrop(state, world, pos, fortune, true)
    private fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int, isBreaking: Boolean) = if (isBreaking) when {
        getAge(state) >= 3 -> 2
        getAge(state) >= 2 -> 1
        else -> 0
    } else when {
        getAge(state) >= 3 -> 1
        else -> 0
    }


    // PickHandler関連

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, playerIn.heldItemMainhand)
        return tryPick(worldIn, pos, playerIn, fortune)
    }

    fun tryPick(world: World, blockPos: BlockPos, player: EntityPlayer?, fortune: Int): Boolean {
        val blockState = world.getBlockState(blockPos)
        if (!isMaxAge(blockState)) return false

        // 収穫物計算
        val drops = NonNullList.create<ItemStack>()
        getDrops(drops, world, blockPos, blockState, fortune, false)

        // 収穫物生成
        drops.forEach { spawnAsEntity(world, blockPos, it) }

        // 経験値生成
        blockState.block.dropXpOnBlockBreak(world, blockPos, getExpDrop(blockState, world, blockPos, fortune, false))

        // エフェクト
        world.playEvent(player, 2001, blockPos, getStateId(blockState))

        // ブロックの置換
        world.setBlockState(blockPos, defaultState.withProperty(AGE, 1), 2)

        return true
    }


    companion object {
        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)
        private val AABB_STAGE0 = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0)
        private val AABB_STAGE1 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0)
        private val AABB_STAGE2 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
        private val AABB_STAGE3 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
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
    override fun getPlant(world: IBlockAccess, pos: BlockPos): IBlockState = MirageFlower.blockMirageFlower().defaultState // 常にAge0のミラ花を与える
}
