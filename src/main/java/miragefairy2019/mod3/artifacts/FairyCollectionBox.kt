package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.readFromNBT
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.writeToNBT
import miragefairy2019.libkt.x
import miragefairy2019.libkt.y
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.fairy.fairyVariant
import miragefairy2019.mod3.fairy.hasSameId
import miragefairy2019.mod3.fairy.level
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryBasic
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.IStringSerializable
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object FairyCollectionBox {
    lateinit var blockFairyCollectionBox: () -> BlockFairyCollectionBox
    lateinit var itemFairyCollectionBox: () -> ItemBlock

    const val guiIdFairyCollectionBox = 3
    val module: Module = {
        blockFairyCollectionBox = block({ BlockFairyCollectionBox() }, "fairy_collection_box") {
            setUnlocalizedName("fairyCollectionBox")
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf("middle", "bottom").flatMap { context ->
                        listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).map { facing ->
                            "context=$context,facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_building_$context", y = facing.second)
                        }
                    }.toMap()
                )
            }
        }
        itemFairyCollectionBox = item({ ItemBlock(blockFairyCollectionBox()) }, "fairy_collection_box") {
            setCustomModelResourceLocation(variant = "context=bottom,facing=north")
        }
        tileEntity("fairy_collection_box", TileEntityFairyCollectionBox::class.java)
        onInit {
            ApiMain.registerGuiHandler(guiIdFairyCollectionBox, object : ISimpleGuiHandler {
                override fun GuiHandlerContext.onServer() = (tileEntity as? TileEntityFairyCollectionBox)?.let { ContainerFairyCollectionBox(player.inventory, it.inventory) }
                override fun GuiHandlerContext.onClient() = (tileEntity as? TileEntityFairyCollectionBox)?.let { GuiFairyCollectionBox(ContainerFairyCollectionBox(player.inventory, it.inventory)) }
            }.guiHandler)
        }
    }
}

class BlockFairyCollectionBox : BlockContainer(Material.WOOD) {
    init {

        // meta
        defaultState = blockState.baseState.withProperty(FACING, EnumFacing.NORTH).withProperty(CONTEXT, EnumFairyCollectionBoxContext.BOTTOM)

        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(1.0f)
        setHarvestLevel("axe", 0)

    }


    // Variant

    override fun getMetaFromState(state: IBlockState) = when (state.getValue(FACING)) {
        EnumFacing.NORTH -> 0
        EnumFacing.SOUTH -> 1
        EnumFacing.WEST -> 2
        EnumFacing.EAST -> 3
        else -> 0
    }

    override fun getStateFromMeta(meta: Int): IBlockState = when (meta) {
        0 -> defaultState.withProperty(FACING, EnumFacing.NORTH)
        1 -> defaultState.withProperty(FACING, EnumFacing.SOUTH)
        2 -> defaultState.withProperty(FACING, EnumFacing.WEST)
        3 -> defaultState.withProperty(FACING, EnumFacing.EAST)
        else -> defaultState
    }

    override fun createBlockState() = BlockStateContainer(this, FACING, CONTEXT)

    override fun getActualState(blockState: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = if (world.getBlockState(pos.down()).block is BlockFairyCollectionBox) {
        blockState.withProperty(CONTEXT, EnumFairyCollectionBoxContext.MIDDLE)
    } else {
        blockState
    }

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)))

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.horizontalFacing.opposite), 2)
    }


    // TileEntity

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyCollectionBox()


    // Graphics

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false


    // Drop

    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        val tileEntity = world.getTileEntity(blockPos)
        if (tileEntity is TileEntityFairyCollectionBox) {
            tileEntity.inventory.itemStacks.forEach { InventoryHelper.spawnItemStack(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), it) }
            world.updateComparatorOutputLevel(blockPos, this)
        }
        super.breakBlock(world, blockPos, blockState)
    }


    // Action

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        playerIn.openGui(ModMirageFairy2019.instance, FairyCollectionBox.guiIdFairyCollectionBox, worldIn, pos.x, pos.y, pos.z)
        return true
    }


    companion object {
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)
        val CONTEXT: PropertyEnum<EnumFairyCollectionBoxContext> = PropertyEnum.create("context", EnumFairyCollectionBoxContext::class.java)

        enum class EnumFairyCollectionBoxContext : IStringSerializable {
            MIDDLE, BOTTOM, ;

            override fun getName(): String = name.toLowerCase()
            override fun toString(): String = name.toLowerCase()
        }
    }
}

class TileEntityFairyCollectionBox : TileEntity() {
    val inventory = InventoryFairyCollectionBox(this, "tile.fairyCollectionBox.name", false, 50)

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        inventory.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        inventory.writeToNBT(nbt)
        return nbt
    }
}

class InventoryFairyCollectionBox(val tileEntity: TileEntity, title: String, customName: Boolean, slotCount: Int) : InventoryBasic(title, customName, slotCount) {
    init {
        addInventoryChangeListener { tileEntity.markDirty() }
    }

    override fun getInventoryStackLimit() = 1
    override fun isItemValidForSlot(index: Int, itemStack: ItemStack): Boolean {
        val variant = itemStack.fairyVariant ?: return false // スタンダード妖精でないと受け付けない
        return itemStacks
            .filterIndexed { i, _ -> i != index } // 他スロットにおいて
            .all a@{ itemStack2 -> !hasSameId(variant, itemStack2.fairyVariant ?: return@a true) } // 同種の妖精があってはならない
    }

    override fun isUsableByPlayer(player: EntityPlayer): Boolean {
        if (tileEntity.world.getTileEntity(tileEntity.pos) != tileEntity) return false
        return player.getDistanceSq(tileEntity.pos.x.toDouble() + 0.5, tileEntity.pos.y.toDouble() + 0.5, tileEntity.pos.z.toDouble() + 0.5) <= 64.0
    }
}

class SlotFairyCollectionBox(inventory: IInventory, index: Int, xPosition: Int, yPosition: Int) : Slot(inventory, index, xPosition, yPosition) {
    override fun isItemValid(stack: ItemStack) = inventory.isItemValidForSlot(slotIndex, stack)
}

class ContainerFairyCollectionBox(val inventoryPlayer: IInventory, val inventoryTileEntity: IInventory) : Container() {
    init {
        repeat(5) { r -> repeat(10) { c -> addSlotToContainer(SlotFairyCollectionBox(inventoryTileEntity, r * 10 + c, 8 + c * 18, 17 + r * 18 + 1)) } }
        repeat(3) { r -> repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, 9 + r * 9 + c, 9 + 8 + c * 18, 84 + 18 * 2 + r * 18 + 1)) } }
        repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, c, 9 + 8 + c * 18, 142 + 18 * 2 + 1)) }
    }

    override fun canInteractWith(playerIn: EntityPlayer) = inventoryTileEntity.isUsableByPlayer(playerIn)

    val fairyMasterGrade get() = inventoryTileEntity.itemStacks.mapNotNull { itemStack -> itemStack.fairyVariant }.distinctBy { it.id }.sumBy { it.level }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val slot = inventorySlots[index] ?: return ItemStack.EMPTY // スロットがnullなら終了
        if (!slot.hasStack) return ItemStack.EMPTY // スロットが空なら終了

        val itemStack = slot.stack
        val itemStackOriginal = itemStack.copy()

        // 移動処理
        // itemStackを改変する
        if (index < 50) { // タイルエンティティ→プレイヤー
            if (!mergeItemStack(itemStack, 50, 50 + 9 * 4, true)) return ItemStack.EMPTY
        } else { // プレイヤー→タイルエンティティ
            if (!mergeItemStack(itemStack, 0, 50, false)) return ItemStack.EMPTY
        }

        if (itemStack.isEmpty) { // スタックが丸ごと移動した
            slot.putStack(ItemStack.EMPTY)
        } else { // 部分的に残った
            slot.onSlotChanged()
        }

        if (itemStack.count == itemStackOriginal.count) return ItemStack.EMPTY // アイテムが何も移動していない場合は終了

        // スロットが改変を受けた場合にここを通過する

        slot.onTake(playerIn, itemStack)

        return itemStackOriginal
    }
}

class GuiFairyCollectionBox(val container: ContainerFairyCollectionBox) : GuiContainer(container) {
    init {
        xSize = 14 + 18 * 10
        ySize = 114 + 18 * 5 - 1
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()

        repeat(5) { r -> repeat(10) { c -> drawSlot(x + 8f + c * 18f - 1f, y + 17f + r * 18f - 1f + 1f) } }
        repeat(3) { r -> repeat(9) { c -> drawSlot(x + 9f + 8f + c * 18f - 1f, y + 84f + 18f * 2 + r * 18f - 1f + 1f) } }
        repeat(9) { c -> drawSlot(x + 9f + 8f + c * 18f - 1f, y + 142f + 18f * 2 - 1f + 1f) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(container.inventoryTileEntity.displayName.unformattedText, 8, 6, 0x404040)
        fontRenderer.drawStringRightAligned("Grade: ${container.fairyMasterGrade}", xSize - 8, 6, 0x000088) // TODO translate
        fontRenderer.drawString(container.inventoryPlayer.displayName.unformattedText, 8, ySize - 96 + 2 + 18 * 0, 0x404040)
    }
}
