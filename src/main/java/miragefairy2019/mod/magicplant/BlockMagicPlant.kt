package miragefairy2019.mod.magicplant

import miragefairy2019.lib.EnumFireSpreadSpeed
import miragefairy2019.lib.EnumFlammability
import net.minecraft.block.BlockBush
import net.minecraft.block.IGrowable
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import java.util.Random

abstract class BlockMagicPlant : BlockBush(Material.PLANTS), IGrowable { // Solidであるマテリアルは耕土を破壊する

    // 特性

    init {
        soundType = SoundType.GLASS
    }

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = EnumFlammability.VERY_FAST.value
    override fun getFireSpreadSpeed(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = EnumFireSpreadSpeed.FAST.value


    // 動作

    // 任意の固体ブロックか農地の上に置ける
    override fun canSustainBush(state: IBlockState) = state.isFullBlock || state.block === Blocks.FARMLAND


    // 成長

    abstract fun isMaxAge(state: IBlockState): Boolean

    abstract fun grow(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random)

    // 自然成長
    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        super.updateTick(worldIn, pos, state, rand)
        if (!worldIn.isAreaLoaded(pos, 1)) return
        grow(worldIn, pos, state, rand)
    }

    // 骨粉
    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) = grow(worldIn, pos, state, rand)
    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean) = !isMaxAge(state)
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) = worldIn.rand.nextFloat() < 0.05


    //ドロップ

    abstract fun getSeed(): ItemStack

    // TODO 戻り値
    abstract fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int, isBreaking: Boolean)

    abstract fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int, isBreaking: Boolean): Int

    // クリエイティブピックでの取得アイテム。
    override fun getItem(world: World, pos: BlockPos, state: IBlockState) = getSeed()

    // 破壊時ドロップ
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) = getDrops(drops, world, pos, state, fortune, true)

    // 破壊時経験値ドロップ
    override fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int) = getExpDrop(state, world, pos, fortune, true)

    // シルクタッチ無効
    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = false

    // 使用すると収穫
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, playerIn.heldItemMainhand)
        return tryPick(worldIn, pos, playerIn, fortune)
    }

    // 収穫
    fun tryPick(world: World, blockPos: BlockPos, player: EntityPlayer?, fortune: Int): Boolean {
        val blockState = world.getBlockState(blockPos)
        if (!isMaxAge(blockState)) return false

        // 収穫物計算
        val drops = NonNullList.create<ItemStack>()
        getDrops(drops, world, blockPos, blockState, fortune, false)

        // 収穫物生成
        drops.forEach {
            spawnAsEntity(world, blockPos, it)
        }

        // 経験値生成
        blockState.block.dropXpOnBlockBreak(world, blockPos, getExpDrop(blockState, world, blockPos, fortune, false))

        // エフェクト
        world.playEvent(player, 2001, blockPos, getStateId(blockState))

        // ブロックの置換
        world.setBlockState(blockPos, defaultState.withProperty(BlockMirageFlower.AGE, 1), 2)

        return true
    }

}
