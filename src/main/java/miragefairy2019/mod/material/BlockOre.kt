package miragefairy2019.mod.material

import miragefairy2019.libkt.BlockMulti
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.IBlockVariant
import miragefairy2019.libkt.ItemBlockMulti
import miragefairy2019.libkt.createItemStack
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random

interface IBlockVariantOre : IBlockVariant {
    val harvestTool: String get() = "pickaxe"
    val harvestLevel: Int
    fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> = listOf(block.createItemStack(metadata = metadata))

    /**
     * 非シルクタッチでの破壊時に得られる経験値の量です。
     * 破壊時に鉱石ブロックがそのまま得られる場合、原則として0を返します。
     *
     * バニラでの設定は以下の通りです。
     *
     * ```
     * coal   : [0, 2)
     * diamond: [3, 7)
     * emerald: [3, 7)
     * lapis  : [2, 5)
     * quartz : [2, 5)
     * ```
     */
    fun getExpDrop(random: Random, fortune: Int): Int = 0

    val hardness: Float
    val resistance: Float
}

class BlockOre<V : IBlockVariantOre>(variantList: BlockVariantList<V>) : BlockMulti<V>(Material.ROCK, variantList) {
    init {

        // style
        soundType = SoundType.STONE

        // 挙動
        setHardness(3.0f)
        setResistance(5.0f)
        variantList.blockVariants.forEach { setHarvestLevel(it.harvestTool, it.harvestLevel, getState(it)) }

    }


    override fun getBlockHardness(blockState: IBlockState, worldIn: World, pos: BlockPos) = getVariant(blockState).hardness
    override fun getExplosionResistance(world: World, pos: BlockPos, exploder: Entity?, explosion: Explosion) = getVariant(world.getBlockState(pos)).resistance


    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, blockPos: BlockPos, blockState: IBlockState, fortune: Int) {
        val random = if (world is World) world.rand else RANDOM
        drops += getVariant(blockState).getDrops(random, this, getMetaFromState(blockState), fortune)
    }

    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = true
    override fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int): Int {
        val random = if (world is World) world.rand else Random()
        return getVariant(state).getExpDrop(random, fortune)
    }


    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
}

class ItemBlockOre<V : IBlockVariantOre>(block: BlockOre<V>) : ItemBlockMulti<BlockOre<V>, V>(block)
