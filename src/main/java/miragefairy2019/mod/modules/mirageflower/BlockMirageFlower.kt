package miragefairy2019.mod.modules.mirageflower

import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.api.ApiMirageFlower
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry
import miragefairy2019.mod.lib.UtilsMinecraft
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal
import miragefairy2019.mod.modules.materialsfairy.ModuleMaterialsFairy
import miragefairy2019.mod.modules.ore.ModuleOre
import miragefairy2019.mod.modules.ore.material.EnumVariantMaterials1
import miragefairy2019.mod3.erg.api.ErgTypes
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.shineEfficiency
import mirrg.boron.util.UtilsMath
import net.minecraft.block.BlockFarmland
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.EnumSkyBlock
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.util.Random

fun getGrowRate(world: World, blockPos: BlockPos): Double {
    var rate = 0.04 // 何もしなくても25回に1回の割合で成長する

    // 人工光が当たっているなら加点
    rate *= world.getLightFor(EnumSkyBlock.BLOCK, blockPos).let {
        when {
            it >= 13 -> 1.2
            it >= 9 -> 1.1
            else -> 1.0
        }
    }

    // 太陽光が当たっているなら加点
    rate *= world.getLightFor(EnumSkyBlock.SKY, blockPos).let {
        when {
            it >= 15 -> 1.1
            it >= 9 -> 1.05
            else -> 1.0
        }
    }

    if (world.canSeeSky(blockPos)) rate *= 1.1 // 空が見えるなら加点

    // 地面加点
    rate *= world.getBlockState(blockPos.down()).let { blockState ->
        var bonus = 0.5

        // 妖精による判定
        run {
            val itemStack = blockState.block.getItem(world, blockPos, blockState)
            val list: List<ResourceLocation> = ApiFairyRegistry.getFairyRelationRegistry().fairySelector()
                .add(blockState)
                .add(itemStack)
                .select().toList()
            val value = list
                .mapNotNull { ApiFairyRegistry.getFairyRegistry().getFairy(it).orElse(null) }
                .map { getGrowRateInFloor(it.fairyType) }
                .max()
            if (value != null) bonus = bonus.coerceAtLeast(value)
        }

        // 特定ブロックによる判定
        if (blockState.block === Blocks.GRASS) bonus = bonus.coerceAtLeast(1.0)
        if (blockState.block === Blocks.DIRT) bonus = bonus.coerceAtLeast(1.1)
        if (blockState.block === Blocks.FARMLAND) {
            bonus = bonus.coerceAtLeast(1.2)
            if (blockState.getValue(BlockFarmland.MOISTURE) > 0) bonus = bonus.coerceAtLeast(1.3) // 耕土が湿っているなら加点
        }
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.APATITE_BLOCK)) bonus = bonus.coerceAtLeast(1.5)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.FLUORITE_BLOCK)) bonus = bonus.coerceAtLeast(2.0)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.SULFUR_BLOCK)) bonus = bonus.coerceAtLeast(1.5)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.CINNABAR_BLOCK)) bonus = bonus.coerceAtLeast(2.0)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MOONSTONE_BLOCK)) bonus = bonus.coerceAtLeast(3.0)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MAGNETITE_BLOCK)) bonus = bonus.coerceAtLeast(1.2)

        bonus
    }

    // バイオーム加点
    rate *= world.getBiome(blockPos).let { biome ->
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

    return rate
}

fun getGrowRateInFloor(fairyType: IFairyType) = fairyType.shineEfficiency * fairyType.erg(ErgTypes.crystal) / 100.0 * 3

fun getGrowRateTableMessage() = textComponent {
    listOf(
        !"===== Mirage Flower Grow Rate Table =====\n",
        ApiFairyRegistry.getFairyRegistry().fairies.toList()
            .map { Pair(it.fairyType, getGrowRateInFloor(it.fairyType)) }
            .filter { it.second > 1 }
            .sortedBy { it.second }
            .flatMap { format("%7.2f%%  ", it.second * 100) + !it.first!!.displayName + !"\n" },
        !"===================="
    ).flatten()
}

fun getGrowRateMessage(world: World, pos: BlockPos) = textComponent {
    !"===== Mirage Flower Grow Rate =====\n"
    !"Pos: ${pos.x} ${pos.y} ${pos.z}\n"
    !"Block: ${world.getBlockState(pos)}\n"
    !"Floor: ${world.getBlockState(pos.down())}\n"
    format("%.2f%%\n", getGrowRate(world, pos) * 100)
    !"===================="
}


class BlockMirageFlower : BlockMirageFlowerBase(Material.PLANTS) {  // Solidであるマテリアルは耕土を破壊する
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
    fun getAge(state: IBlockState) = state.getValue(AGE)
    fun isMaxAge(state: IBlockState) = getAge(state) == 3
    fun grow(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random, rate: Double) {
        repeat(UtilsMath.randomInt(rand, rate)) {
            if (!isMaxAge(state)) worldIn.setBlockState(pos, defaultState.withProperty(AGE, getAge(state) + 1), 2)
        }
    }

    // UpdateTickごとにAgeが1ずつ最大3まで増える。
    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        super.updateTick(worldIn, pos, state, rand)
        if (!worldIn.isAreaLoaded(pos, 1)) return
        grow(worldIn, pos, state, rand, getGrowRate(worldIn, pos))
    }

    // 骨粉をやると低確率で成長する。
    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) = grow(worldIn, pos, state, rand, getGrowRate(worldIn, pos))
    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean) = !isMaxAge(state)
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) = worldIn.rand.nextFloat() < 0.05


    // ドロップ

    // クリエイティブピックでの取得アイテム。
    override fun getItem(world: World, pos: BlockPos, state: IBlockState) = ItemStack(ApiMirageFlower.itemMirageFlowerSeeds)

    /*
     * Ageが最大のとき、種を1個ドロップする。
     * 幸運Lv1につき種のドロップ数が1%増える。
     * 地面破壊ドロップでも適用される。
     */
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) = getDrops(drops, world, pos, state, fortune, true)

    private fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int, isBreaking: Boolean) {
        val random = if (world is World) world.rand else Random()

        // 種1個は確定でドロップ
        if (isBreaking) {
            drops.add(ItemStack(ApiMirageFlower.itemMirageFlowerSeeds))
        }

        // サイズが2以上なら確定で茎をドロップ
        if (isBreaking) {
            if (getAge(state) >= 2) {
                val count = UtilsMath.randomInt(random, 1 + fortune * 0.2)
                repeat(count) { drops.add(ModuleMaterialsFairy.itemStackLeafMirageFlower.copy()) }
            }
        }

        // 追加の種
        if (getAge(state) >= 3) {
            val count = UtilsMath.randomInt(random, fortune * 0.01)
            repeat(count) { drops.add(ItemStack(ApiMirageFlower.itemMirageFlowerSeeds)) }
        }

        // クリスタル
        if (getAge(state) >= 3) {
            val count = UtilsMath.randomInt(random, 1 + fortune * 0.5)
            repeat(count) { drops.add(ModuleFairyCrystal.variantFairyCrystal.createItemStack()) }
        }

        // ミラジウム
        if (getAge(state) >= 3) {
            val count = UtilsMath.randomInt(random, 1 + fortune * 0.5)
            repeat(count) { drops.add(UtilsMinecraft.getItemStack("dustTinyMiragium").copy()) }
        }
    }

    // シルクタッチ無効。
    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = false


    companion object {
        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)
        private val AABB_STAGE0 = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0)
        private val AABB_STAGE1 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0)
        private val AABB_STAGE2 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
        private val AABB_STAGE3 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
    }
}
