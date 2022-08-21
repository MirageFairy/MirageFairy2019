package miragefairy2019.mod.placeditem

import miragefairy2019.lib.TileEntityIgnoreBlockState
import miragefairy2019.libkt.orEmpty
import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class BlockPlacedItem : BlockContainer(Material.CIRCUITS) {
    companion object {
        private val AABB = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 2 / 16.0, 14 / 16.0)
    }

    init {
        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(0.5f)
        setHarvestLevel("shovel", 0)
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityPlacedItem()

    // ドロップ

    /**
     * クリエイティブピックでの取得アイテム。
     */
    override fun getItem(world: World, pos: BlockPos, state: IBlockState) = world.getTileEntity(pos)?.castOrNull<TileEntityPlacedItem>()?.itemStack.orEmpty

    override fun getDrops(drops: NonNullList<ItemStack>, blockAccess: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) {}

    /**
     * 破壊時ドロップ
     */
    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        val tileEntity = world.getTileEntity(blockPos)
        if (tileEntity is TileEntityPlacedItem) {
            InventoryHelper.spawnItemStack(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), tileEntity.itemStack)
            world.updateComparatorOutputLevel(blockPos, this)
        }
        super.breakBlock(world, blockPos, blockState)
    }

    /**
     * シルクタッチ無効。
     */
    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = false


    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.INVISIBLE
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun getBlockFaceShape(world: IBlockAccess, blockState: IBlockState, blockPos: BlockPos, facing: EnumFacing) = BlockFaceShape.UNDEFINED


    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = AABB
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = NULL_AABB


    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos) = canSustain(worldIn, pos)
    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) = checkForDrop(worldIn, pos, state)
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) = checkForDrop(worldIn, pos, state)

    private fun checkForDrop(world: World, blockPos: BlockPos, blockState: IBlockState) {
        if (blockState.block !== this) return // 呼び出されたブロックが自分でない場合は無視
        if (canSustain(world, blockPos)) return // その場に存在できる場合は何もしない
        if (world.getBlockState(blockPos).block !== this) return // 指定座標に自ブロックが居ない場合は無視
        dropBlockAsItem(world, blockPos, blockState, 0)
        world.setBlockToAir(blockPos)
    }

    private fun canSustain(world: IBlockAccess, blockPos: BlockPos): Boolean = world.getBlockState(blockPos.down()).isSideSolid(world, blockPos.down(), EnumFacing.UP)


    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        val tileEntity = worldIn.getTileEntity(pos)
        if (tileEntity !is TileEntityPlacedItem) return true
        if (playerIn.isSneaking) {
            tileEntity.standing = !tileEntity.standing
        } else {
            tileEntity.rotation += 45.0
            if (tileEntity.rotation >= 360) {
                tileEntity.rotation -= 360.0
            }
        }
        tileEntity.markDirty()
        tileEntity.sendUpdatePacket()
        return true
    }
}

class TileEntityPlacedItem : TileEntityIgnoreBlockState() {
    var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(1, ItemStack.EMPTY)
    var rotation = 0.0
    var standing = false

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        ItemStackHelper.saveAllItems(nbt, itemStacks)
        nbt.setDouble("rotation", rotation)
        nbt.setBoolean("standing", standing)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        itemStacks.fill(ItemStack.EMPTY)
        ItemStackHelper.loadAllItems(nbt, itemStacks)
        rotation = nbt.getDouble("rotation")
        standing = nbt.getBoolean("standing")
    }

    override fun getUpdatePacket() = SPacketUpdateTileEntity(pos, 9, updateTag)

    override fun getUpdateTag() = writeToNBT(NBTTagCompound())

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
        super.onDataPacket(net, pkt)
    }

    fun sendUpdatePacket() = world.playerEntities.forEach { if (it is EntityPlayerMP) it.connection.sendPacket(updatePacket) }


    var itemStack: ItemStack
        get() = itemStacks[0]
        set(itemStack) = run { itemStacks[0] = itemStack }
}

@SideOnly(Side.CLIENT)
class TileEntityRendererPlacedItem : TileEntitySpecialRenderer<TileEntityPlacedItem>() {
    override fun render(tileEntity: TileEntityPlacedItem, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GlStateManager.translate(0.5, 1 / 64.0, 0.5)
        GlStateManager.rotate((-tileEntity.rotation).toFloat(), 0f, 1f, 0f)
        if (tileEntity.standing) {
            GlStateManager.translate(0.0, 0.25, 0.0)
        } else {
            GlStateManager.rotate(90f, 1f, 0f, 0f)
        }
        val itemStack = tileEntity.itemStack
        renderItem(if (itemStack.isEmpty) ItemStack(Blocks.BARRIER) else itemStack)
        GlStateManager.popMatrix()
    }

    private fun renderItem(itemStack: ItemStack) {
        if (itemStack.isEmpty) return
        GlStateManager.disableLighting()
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.pushAttrib()
        RenderHelper.enableStandardItemLighting()
        Minecraft.getMinecraft().renderItem.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.popAttrib()
        GlStateManager.enableLighting()
    }
}
