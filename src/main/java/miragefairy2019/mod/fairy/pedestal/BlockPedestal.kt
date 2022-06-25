package miragefairy2019.mod.fairy.pedestal

import miragefairy2019.api.IPlaceAcceptorBlock
import miragefairy2019.api.IPlaceExchanger
import miragefairy2019.libkt.notEmptyOrNull
import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

abstract class BlockPedestal<T : TileEntityPedestal>(material: Material, private val validator: (TileEntity) -> T?) : BlockContainer(material), IPlaceAcceptorBlock {
    fun getTileEntity(world: World, blockPos: BlockPos) = world.getTileEntity(blockPos)?.let { validator(it) }
    fun getItemStack(world: World, blockPos: BlockPos) = getTileEntity(world, blockPos)?.itemStack


    // ドロップ

    // クリエイティブピックでの取得アイテム
    @Deprecated("Deprecated in Java")
    override fun getItem(world: World, blockPos: BlockPos, blockState: IBlockState): ItemStack = getItemStack(world, blockPos)?.notEmptyOrNull ?: super.getItem(world, blockPos, blockState)

    // 破壊時ドロップ
    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        val tileEntity = getTileEntity(world, blockPos) ?: return super.breakBlock(world, blockPos, blockState)
        InventoryHelper.spawnItemStack(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), tileEntity.itemStack)
        world.updateComparatorOutputLevel(blockPos, this)
    }

    // シルクタッチ無効
    override fun canSilkHarvest(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer) = false


    // レンダリング

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false


    // アクション

    override fun onBlockActivated(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (world.isRemote) return true
        val tileEntity = getTileEntity(world, blockPos) ?: return true

        if (tileEntity.itemStack.isEmpty) {
            val heldItemStack = player.heldItemMainhand.notEmptyOrNull
            if (heldItemStack != null) {
                tileEntity.itemStack = heldItemStack.splitStack(1)
            }
        } else {
            onAdjust(world, blockPos, tileEntity, player)
        }

        tileEntity.markDirty()
        tileEntity.sendUpdatePacket()
        return true
    }

    override fun place(world: World, blockPos: BlockPos, player: EntityPlayer, placeExchanger: IPlaceExchanger): Boolean {
        val tileEntity = getTileEntity(world, blockPos) ?: return false // 異常なTileだった場合は中止
        if (tileEntity.itemStack.isEmpty) { // 設置

            val itemStack = placeExchanger.deploy()
            if (itemStack.isEmpty) return false

            // アイテムを設置
            tileEntity.itemStack = itemStack

            onDeploy(world, blockPos, tileEntity, player, itemStack)

            tileEntity.markDirty()
            tileEntity.sendUpdatePacket()

            return true
        } else { // 撤去

            // アイテムを回収
            val itemStackContained = tileEntity.itemStack
            tileEntity.itemStack = ItemStack.EMPTY

            onHarvest(world, blockPos, tileEntity, player)

            tileEntity.markDirty()
            tileEntity.sendUpdatePacket()

            // アイテムを増やす
            placeExchanger.harvest(itemStackContained)

            return true
        }
    }

    open fun onAdjust(world: World, blockPos: BlockPos, tileEntity: T, player: EntityPlayer) = Unit
    open fun onDeploy(world: World, blockPos: BlockPos, tileEntity: T, player: EntityPlayer, itemStack: ItemStack) = Unit
    open fun onHarvest(world: World, blockPos: BlockPos, tileEntity: T, player: EntityPlayer) = Unit
}

abstract class BlockPlacedPedestal<T : TileEntityPedestal>(material: Material, validator: (TileEntity) -> T?) : BlockPedestal<T>(material, validator) {
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos) = super.canPlaceBlockAt(worldIn, pos) && canSustain(worldIn, pos)
    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) = checkForDrop(worldIn, pos, state)
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) = checkForDrop(worldIn, pos, state)
    private fun canSustain(world: IBlockAccess, blockPos: BlockPos): Boolean = world.getBlockState(blockPos.down()).isSideSolid(world, blockPos.down(), EnumFacing.UP)
    private fun checkForDrop(world: World, blockPos: BlockPos, blockState: IBlockState) {
        if (blockState.block !== this) return // 呼び出されたブロックが自分でない場合は無視
        if (canSustain(world, blockPos)) return // その場に存在できる場合は何もしない
        if (world.getBlockState(blockPos).block !== this) return // 指定座標に自ブロックが居ない場合は無視
        dropBlockAsItem(world, blockPos, blockState, 0)
        world.setBlockToAir(blockPos)
    }
}

interface ITransformProxy {
    fun translate(x: Float, y: Float, z: Float)
    fun translate(x: Double, y: Double, z: Double)
    fun rotate(angle: Float, x: Float, y: Float, z: Float)
    fun scale(x: Float, y: Float, z: Float)
    fun scale(x: Double, y: Double, z: Double)
}

abstract class TileEntityPedestal : TileEntity() {
    var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(1, ItemStack.EMPTY)
    var itemStack: ItemStack
        get() = itemStacks[0]
        set(itemStack) = run { itemStacks[0] = itemStack }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        ItemStackHelper.saveAllItems(nbt, itemStacks)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        itemStacks.fill(ItemStack.EMPTY)
        ItemStackHelper.loadAllItems(nbt, itemStacks)
    }


    override fun getUpdatePacket() = SPacketUpdateTileEntity(pos, 9, updateTag)

    override fun getUpdateTag() = writeToNBT(NBTTagCompound())

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
        super.onDataPacket(net, pkt)
    }

    fun sendUpdatePacket() = world.playerEntities.forEach { if (it is EntityPlayerMP) it.connection.sendPacket(updatePacket) }


    abstract fun transform(transformProxy: ITransformProxy)
}

@SideOnly(Side.CLIENT)
class TileEntityRendererPedestal<T : TileEntityPedestal> : TileEntitySpecialRenderer<T>() {
    override fun render(tileEntity: T, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        val itemStack = tileEntity.itemStack.notEmptyOrNull ?: return
        matrix {
            GlStateManager.translate(x, y, z)
            tileEntity.transform(object : ITransformProxy {
                override fun translate(x: Float, y: Float, z: Float) = GlStateManager.translate(x, y, z)
                override fun translate(x: Double, y: Double, z: Double) = GlStateManager.translate(x, y, z)
                override fun rotate(angle: Float, x: Float, y: Float, z: Float) = GlStateManager.rotate(angle, x, y, z)
                override fun scale(x: Float, y: Float, z: Float) = GlStateManager.scale(x, y, z)
                override fun scale(x: Double, y: Double, z: Double) = GlStateManager.scale(x, y, z)
            })
            renderItem(itemStack)
        }
    }

    private inline fun <O> matrix(block: () -> O): O {
        GlStateManager.pushMatrix()
        try {
            return block()
        } finally {
            GlStateManager.popMatrix()
        }
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
