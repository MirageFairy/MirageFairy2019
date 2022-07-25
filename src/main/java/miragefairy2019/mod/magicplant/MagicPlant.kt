package miragefairy2019.mod.magicplant

import miragefairy2019.api.IFairySpec
import miragefairy2019.api.IPickExecutor
import miragefairy2019.api.IPickHandler
import miragefairy2019.api.PickHandlerRegistry
import miragefairy2019.lib.EnumFireSpreadSpeed
import miragefairy2019.lib.EnumFlammability
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.randomInt
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.BlockBush
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
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.IPlantable
import java.util.Random

val magicPlantModule = module {
    mirageFlowerModule()
    mandrakeModule()

    onRegisterBlock {
        PickHandlerRegistry.pickHandlers += IPickHandler { world, blockPos, player ->
            val blockState = world.getBlockState(blockPos)
            val block = blockState.block as? BlockMagicPlant ?: return@IPickHandler null
            if (!block.canPick(block.getAge(blockState))) return@IPickHandler null
            IPickExecutor { fortune -> block.tryPick(world, blockPos, player, fortune) }
        }
    }

}

abstract class BlockMagicPlant(val maxAge: Int) : BlockBush(Material.PLANTS), IGrowable { // Solidであるマテリアルは耕土を破壊する
    companion object {
        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 15)
    }


    init {
        soundType = SoundType.GLASS
    }

    override fun getFlammability(world: IBlockAccess, blockPos: BlockPos, face: EnumFacing) = EnumFlammability.VERY_FAST.value
    override fun getFireSpreadSpeed(world: IBlockAccess, blockPos: BlockPos, face: EnumFacing) = EnumFireSpreadSpeed.FAST.value


    // State

    init {
        defaultState = blockState.baseState.withProperty(AGE, 0)
    }

    fun getState(age: Int): IBlockState = defaultState.withProperty(AGE, age atLeast 0 atMost maxAge)
    fun getAge(blockState: IBlockState): Int = blockState.getValue(AGE) atLeast 0 atMost maxAge

    override fun createBlockState() = BlockStateContainer(this, AGE)
    override fun getMetaFromState(blockState: IBlockState) = getAge(blockState)
    override fun getStateFromMeta(meta: Int): IBlockState = getState(meta)


    // 当たり判定

    abstract val boundingBoxList: List<AxisAlignedBB>

    override fun getBoundingBox(blockState: IBlockState, source: IBlockAccess, blockPos: BlockPos) = boundingBoxList.getOrNull(getAge(blockState)) ?: boundingBoxList.last()


    // 動作

    // 任意の固体ブロックか農地の上に置ける
    override fun canSustainBush(blockState: IBlockState) = blockState.isFullBlock || blockState.block === Blocks.FARMLAND


    // 成長

    abstract fun getGrowthFactorInFloor(fairySpec: IFairySpec): Double

    abstract fun canPick(age: Int): Boolean

    abstract fun canGrow(age: Int): Boolean

    abstract fun grow(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random)

    // 自然成長
    override fun updateTick(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        super.updateTick(world, blockPos, blockState, random)
        if (!world.isAreaLoaded(blockPos, 1)) return
        grow(world, blockPos, blockState, random)
    }

    // 骨粉
    override fun grow(world: World, random: Random, blockPos: BlockPos, blockState: IBlockState) = grow(world, blockPos, blockState, random)
    override fun canGrow(world: World, blockPos: BlockPos, blockState: IBlockState, isClient: Boolean) = canGrow(getAge(blockState))
    override fun canUseBonemeal(world: World, random: Random, blockPos: BlockPos, blockState: IBlockState) = world.rand.nextFloat() < 0.05


    //ドロップ

    abstract fun getSeed(): ItemStack

    abstract fun getDrops(age: Int, random: Random, fortune: Int, isBreaking: Boolean): List<ItemStack>

    abstract fun getExpDrop(age: Int, random: Random, fortune: Int, isBreaking: Boolean): Int

    // クリエイティブピックでの取得アイテム。
    override fun getItem(world: World, blockPos: BlockPos, blockState: IBlockState) = getSeed()

    // 破壊時ドロップ
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, blockPos: BlockPos, blockState: IBlockState, fortune: Int) {
        drops += getDrops(getAge(blockState), if (world is World) world.rand else Random(), fortune, true)
    }

    // 破壊時経験値ドロップ
    override fun getExpDrop(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos, fortune: Int) = getExpDrop(getAge(blockState), if (world is World) world.rand else Random(), fortune, true)

    // シルクタッチ無効
    override fun canSilkHarvest(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer) = false

    // 使用すると収穫
    override fun onBlockActivated(world: World, blockPos: BlockPos, blockState: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, playerIn.heldItemMainhand)
        return tryPick(world, blockPos, playerIn, fortune)
    }

    // 収穫
    fun tryPick(world: World, blockPos: BlockPos, player: EntityPlayer?, fortune: Int): Boolean {
        val blockState = world.getBlockState(blockPos)
        if (!canPick(getAge(blockState))) return false

        // 収穫物計算
        val drops = getDrops(getAge(blockState), world.rand, fortune, false)

        // 収穫物生成
        drops.forEach {
            spawnAsEntity(world, blockPos, it)
        }

        // 経験値生成
        blockState.block.dropXpOnBlockBreak(world, blockPos, getExpDrop(getAge(blockState), world.rand, fortune, false))

        // エフェクト
        world.playEvent(player, 2001, blockPos, getStateId(blockState))

        // ブロックの置換
        world.setBlockState(blockPos, defaultState.withProperty(AGE, 1), 2)

        return true
    }

}

abstract class ItemMagicPlantSeed(private val block: BlockMagicPlant) : Item(), IPlantable {
    // 使われるとその場に植物を設置する。
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemStack = player.getHeldItem(hand)
        val blockState = world.getBlockState(blockPos)
        if (facing != EnumFacing.UP) return EnumActionResult.FAIL // ブロックの上面にのみ使用可能
        if (!player.canPlayerEdit(blockPos.offset(facing), facing, itemStack)) return EnumActionResult.FAIL // プレイヤーが編集不可能な場合は失敗
        if (!blockState.block.canSustainPlant(blockState, world, blockPos, EnumFacing.UP, block)) return EnumActionResult.FAIL // ブロックがその場所に滞在できないとだめ
        if (!world.isAirBlock(blockPos.up())) return EnumActionResult.FAIL // 真上が空気でないとだめ

        world.setBlockState(blockPos.up(), getPlant(world, blockPos))
        if (player is EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger(player, blockPos.up(), itemStack)
        itemStack.shrink(1)
        return EnumActionResult.SUCCESS
    }

    override fun getPlantType(world: IBlockAccess, blockPos: BlockPos) = EnumPlantType.Plains // 常に草の上に蒔ける
    override fun getPlant(world: IBlockAccess, blockPos: BlockPos): IBlockState = block.defaultState // 常にAge0のミラ花を与える
}

fun MutableList<ItemStack>.drop(random: Random, chance: Double, creator: (Int) -> ItemStack?) {
    val count = random.randomInt(chance)
    if (count >= 1) {
        val itemStack = creator(count)
        if (itemStack != null) this += itemStack
    }
}
