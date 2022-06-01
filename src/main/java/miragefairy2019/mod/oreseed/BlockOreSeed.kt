package miragefairy2019.mod.oreseed

import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.createItemStack
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import java.util.ArrayDeque
import java.util.Deque
import java.util.Random

class BlockOreSeed(private val type: EnumOreSeedType) : Block(Material.ROCK) {
    companion object {
        val VARIANT: PropertyEnum<EnumVariantOreSeed> = PropertyEnum.create("variant", EnumVariantOreSeed::class.java)
    }

    init {

        // meta
        defaultState = blockState.baseState.withProperty(VARIANT, EnumVariantOreSeed.TINY)

        // style
        soundType = SoundType.STONE

        // 挙動
        setHardness(1.5f)
        setResistance(10.0f)
        tickRandomly = true
        setHarvestLevel("pickaxe", 0)

    }


    // BlockState

    override fun createBlockState() = BlockStateContainer(this, VARIANT)
    fun getState(variant: EnumVariantOreSeed): IBlockState = defaultState.withProperty(VARIANT, variant)
    fun getVariant(blockState: IBlockState): EnumVariantOreSeed = blockState.getValue(VARIANT)
    override fun getStateFromMeta(meta: Int) = getState(EnumVariantOreSeed.byMetadata(meta))
    override fun getMetaFromState(blockState: IBlockState) = getVariant(blockState).metadata

    override fun getSubBlocks(creativeTab: CreativeTabs, itemStacks: NonNullList<ItemStack>) {
        EnumVariantOreSeed.values().forEach { variant ->
            itemStacks += this.createItemStack(metadata = variant.metadata)
        }
    }


    // Drop

    override fun getItem(world: World, blockPos: BlockPos, blockState: IBlockState) = EMPTY_ITEM_STACK
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, blockState: IBlockState, fortune: Int) = Unit
    override fun canSilkHarvest(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer) = false


    // Event

    override fun updateTick(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        if (!world.isRemote) update(world, blockPos)
    }

    override fun neighborChanged(blockState: IBlockState, world: World, blockPos: BlockPos, block: Block, fromBlockPos: BlockPos) {
        if (!world.isRemote) update(world, blockPos)
    }

    override fun onBlockClicked(world: World, blockPos: BlockPos, player: EntityPlayer) {
        super.onBlockClicked(world, blockPos, player)
        if (!world.isRemote) update(world, blockPos)
    }

    override fun onBlockActivated(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!world.isRemote) update(world, blockPos)
        return super.onBlockActivated(world, blockPos, blockState, player, hand, facing, hitX, hitY, hitZ)
    }


    // Mutate

    private fun update(world: World, pos: BlockPos) {
        if (shouldMutate(world, pos)) mutate(world, pos)
    }

    private fun shouldMutate(world: IBlockAccess, blockPos: BlockPos) = when {
        !world.getBlockState(blockPos.up()).isSideSolid(world, blockPos.up(), EnumFacing.DOWN) -> true
        !world.getBlockState(blockPos.down()).isSideSolid(world, blockPos.down(), EnumFacing.UP) -> true
        !world.getBlockState(blockPos.west()).isSideSolid(world, blockPos.west(), EnumFacing.EAST) -> true
        !world.getBlockState(blockPos.east()).isSideSolid(world, blockPos.east(), EnumFacing.WEST) -> true
        !world.getBlockState(blockPos.north()).isSideSolid(world, blockPos.north(), EnumFacing.SOUTH) -> true
        !world.getBlockState(blockPos.south()).isSideSolid(world, blockPos.south(), EnumFacing.NORTH) -> true
        else -> false
    }

    private fun mutate(world: World, blockPos: BlockPos) {
        val blockState = world.getBlockState(blockPos)
        val random = Random(blockPos.x * 15946848L + blockPos.y * 29135678L + blockPos.z * 65726816L)
        val blockStateAfter = ApiOreSeedDrop.oreSeedDropRegistry.drop(OreSeedDropEnvironment(type, getVariant(blockState).shape, world, blockPos), random) ?: type.getBlockState()

        val waitingBlockPoses: Deque<BlockPos> = ArrayDeque()
        waitingBlockPoses += blockPos
        var t = 4096
        while (waitingBlockPoses.isNotEmpty()) {
            val currentBlockPos2 = waitingBlockPoses.removeFirst()

            world.setBlockState(currentBlockPos2, blockStateAfter, 2)

            if (world.getBlockState(currentBlockPos2.up()).equals(blockState)) waitingBlockPoses += currentBlockPos2.up()
            if (world.getBlockState(currentBlockPos2.down()).equals(blockState)) waitingBlockPoses += currentBlockPos2.down()
            if (world.getBlockState(currentBlockPos2.west()).equals(blockState)) waitingBlockPoses += currentBlockPos2.west()
            if (world.getBlockState(currentBlockPos2.east()).equals(blockState)) waitingBlockPoses += currentBlockPos2.east()
            if (world.getBlockState(currentBlockPos2.north()).equals(blockState)) waitingBlockPoses += currentBlockPos2.north()
            if (world.getBlockState(currentBlockPos2.south()).equals(blockState)) waitingBlockPoses += currentBlockPos2.south()

            t--
            if (t <= 0) break
        }
    }
}
