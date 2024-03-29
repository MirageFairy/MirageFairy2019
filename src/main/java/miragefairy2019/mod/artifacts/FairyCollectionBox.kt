package miragefairy2019.mod.artifacts

import miragefairy2019.lib.TileEntityIgnoreBlockState
import miragefairy2019.lib.gui.rectangle
import miragefairy2019.lib.gui.x
import miragefairy2019.lib.gui.y
import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.readFromNBT
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.lib.writeToNBT
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.GuiHandlerEvent
import miragefairy2019.libkt.ISimpleGuiHandlerTileEntity
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.fairy.fairyVariant
import miragefairy2019.mod.fairy.hasSameId
import miragefairy2019.mod.fairy.id
import miragefairy2019.mod.fairy.level
import miragefairy2019.util.InventoryTileEntity
import miragefairy2019.util.SmartSlot
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
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

lateinit var blockFairyCollectionBox: () -> BlockFairyCollectionBox
lateinit var itemFairyCollectionBox: () -> ItemBlock

val fairyCollectionBoxModule = module {

    // ブロック
    blockFairyCollectionBox = block({ BlockFairyCollectionBox() }, "fairy_collection_box") {
        setUnlocalizedName("fairyCollectionBox")
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataModelBlockDefinition(
                variants = listOf("middle", "bottom").flatMap { context ->
                    listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).map { facing ->
                        "context=$context,facing=${facing.first}" to DataSingleVariantList(DataVariant("miragefairy2019:fairy_building_$context", y = facing.second))
                    }
                }.toMap()
            )
        }
    }

    // アイテム
    itemFairyCollectionBox = item({ ItemBlock(blockFairyCollectionBox()) }, "fairy_collection_box") {
        setCustomModelResourceLocation(variant = "context=bottom,facing=north")
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "sls",
                    "PLD",
                    "sCs"
                ),
                key = mapOf(
                    "L" to DataOreIngredient(ore = "logWood"),
                    "P" to DataOreIngredient(ore = "paneGlass"),
                    "D" to DataOreIngredient(ore = "doorWood"),
                    "l" to DataOreIngredient(ore = "torch"),
                    "C" to DataSimpleIngredient(item = "minecraft:carpet", data = 14),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSpace")
                ),
                result = DataResult(item = "miragefairy2019:fairy_collection_box")
            )
        }
    }

    // タイルエンティティ
    tileEntity("fairy_collection_box", TileEntityFairyCollectionBox::class.java)

    // 翻訳生成
    lang("tile.fairyCollectionBox.name", "Fairy Collection Box", "妖精蒐集箱")

    // 最下部のブロックモデル生成
    makeBlockModel("fairy_building_bottom") {
        DataModel(
            parent = "block/block",
            textures = mapOf(
                "particle" to "blocks/log_oak",
                "top" to "miragefairy2019:blocks/fairy_building_top",
                "side_background" to "miragefairy2019:blocks/fairy_building_side_background",
                "side_light_1" to "miragefairy2019:blocks/fairy_building_side_light_1",
                "side_light_2" to "miragefairy2019:blocks/fairy_building_side_light_2",
                "front_background" to "miragefairy2019:blocks/fairy_building_front_background",
                "front_light_1" to "miragefairy2019:blocks/fairy_building_front_light_1",
                "front_light_2" to "miragefairy2019:blocks/fairy_building_front_light_2",
                "log_top" to "blocks/log_oak_top",
                "log_side" to "blocks/log_oak"
            ),
            elements = listOf(
                DataElement(
                    from = DataPoint(2.0, 0.0, 2.0),
                    to = DataPoint(14.0, 16.0, 14.0),
                    faces = DataFaces(
                        down = DataFace(texture = "#top"),
                        up = DataFace(texture = "#top"),
                        north = DataFace(texture = "#front_background"),
                        south = DataFace(texture = "#side_background"),
                        west = DataFace(texture = "#side_background"),
                        east = DataFace(texture = "#side_background")
                    )
                ),
                DataElement(
                    from = DataPoint(2.0, 0.0, 2.0),
                    to = DataPoint(14.0, 16.0, 14.0),
                    faces = DataFaces(
                        north = DataFace(texture = "#front_light_1", tintindex = 0),
                        south = DataFace(texture = "#side_light_1", tintindex = 0),
                        west = DataFace(texture = "#side_light_1", tintindex = 0),
                        east = DataFace(texture = "#side_light_1", tintindex = 0)
                    )
                ),
                DataElement(
                    from = DataPoint(2.0, 0.0, 2.0),
                    to = DataPoint(14.0, 16.0, 14.0),
                    faces = DataFaces(
                        north = DataFace(texture = "#front_light_2", tintindex = 1),
                        south = DataFace(texture = "#side_light_2", tintindex = 1),
                        west = DataFace(texture = "#side_light_2", tintindex = 1),
                        east = DataFace(texture = "#side_light_2", tintindex = 1)
                    )
                ),
                DataElement(
                    from = DataPoint(2.0, 0.0, 14.0),
                    to = DataPoint(12.0, 2.0, 16.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(6.0, 0.0, 14.0),
                    to = DataPoint(8.0, 6.0, 16.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(10.0, 0.0, 14.0),
                    to = DataPoint(12.0, 10.0, 16.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(14.0, 0.0, 8.0),
                    to = DataPoint(16.0, 2.0, 14.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(14.0, 0.0, 10.0),
                    to = DataPoint(16.0, 8.0, 12.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(14.0, 0.0, 0.0),
                    to = DataPoint(16.0, 2.0, 4.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(14.0, 0.0, 2.0),
                    to = DataPoint(16.0, 4.0, 4.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(4.0, 0.0, 0.0),
                    to = DataPoint(6.0, 4.0, 2.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(0.0, 0.0, 4.0),
                    to = DataPoint(2.0, 4.0, 6.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(0.0, 0.0, 6.0),
                    to = DataPoint(2.0, 12.0, 8.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(0.0, 0.0, 8.0),
                    to = DataPoint(2.0, 6.0, 10.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                ),
                DataElement(
                    from = DataPoint(0.0, 0.0, 10.0),
                    to = DataPoint(2.0, 2.0, 12.0),
                    shade = false,
                    faces = DataFaces(
                        down = DataFace(texture = "#log_top"),
                        up = DataFace(texture = "#log_top"),
                        north = DataFace(texture = "#log_side"),
                        south = DataFace(texture = "#log_side"),
                        west = DataFace(texture = "#log_side"),
                        east = DataFace(texture = "#log_side")
                    )
                )
            )
        )
    }

    // 上段のブロックモデル生成
    makeBlockModel("fairy_building_middle") {
        DataModel(
            parent = "block/block",
            textures = mapOf(
                "particle" to "blocks/log_oak",
                "top" to "miragefairy2019:blocks/fairy_building_top",
                "side_background" to "miragefairy2019:blocks/fairy_building_side_background",
                "side_light_1" to "miragefairy2019:blocks/fairy_building_side_light_1",
                "side_light_2" to "miragefairy2019:blocks/fairy_building_side_light_2"
            ),
            elements = listOf(
                DataElement(
                    from = DataPoint(2.0, 0.0, 2.0),
                    to = DataPoint(14.0, 16.0, 14.0),
                    faces = DataFaces(
                        down = DataFace(texture = "#top"),
                        up = DataFace(texture = "#top"),
                        north = DataFace(texture = "#side_background"),
                        south = DataFace(texture = "#side_background"),
                        west = DataFace(texture = "#side_background"),
                        east = DataFace(texture = "#side_background")
                    )
                ),
                DataElement(
                    from = DataPoint(2.0, 0.0, 2.0),
                    to = DataPoint(14.0, 16.0, 14.0),
                    faces = DataFaces(
                        north = DataFace(texture = "#side_light_1", tintindex = 0),
                        south = DataFace(texture = "#side_light_1", tintindex = 0),
                        west = DataFace(texture = "#side_light_1", tintindex = 0),
                        east = DataFace(texture = "#side_light_1", tintindex = 0)
                    )
                ),
                DataElement(
                    from = DataPoint(2.0, 0.0, 2.0),
                    to = DataPoint(14.0, 16.0, 14.0),
                    faces = DataFaces(
                        north = DataFace(texture = "#side_light_2", tintindex = 1),
                        south = DataFace(texture = "#side_light_2", tintindex = 1),
                        west = DataFace(texture = "#side_light_2", tintindex = 1),
                        east = DataFace(texture = "#side_light_2", tintindex = 1)
                    )
                )
            )
        )
    }

}

class BlockFairyCollectionBox : BlockContainer(Material.WOOD) {
    init {

        // meta
        defaultState = blockState.baseState
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(CONTEXT, EnumFairyCollectionBoxContext.BOTTOM)

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
    override fun getBlockFaceShape(world: IBlockAccess, blockState: IBlockState, blockPos: BlockPos, facing: EnumFacing) = BlockFaceShape.UNDEFINED


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
        playerIn.openGui(ModMirageFairy2019.instance, GuiId.commonTileEntity, worldIn, pos.x, pos.y, pos.z)
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

class TileEntityFairyCollectionBox : TileEntityIgnoreBlockState(), ISimpleGuiHandlerTileEntity {
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

    override fun onServer(event: GuiHandlerEvent) = ContainerFairyCollectionBox(event.player.inventory, inventory)
    override fun onClient(event: GuiHandlerEvent) = GuiFairyCollectionBox(onServer(event))
}

class InventoryFairyCollectionBox(tileEntity: TileEntityFairyCollectionBox, title: String, customName: Boolean, slotCount: Int) : InventoryTileEntity<TileEntityFairyCollectionBox>(tileEntity, title, customName, slotCount) {
    override fun getInventoryStackLimit() = 1
    override fun isItemValidForSlot(index: Int, itemStack: ItemStack): Boolean {
        val variant = itemStack.fairyVariant ?: return false // スタンダード妖精でないと受け付けない
        return itemStacks
            .filterIndexed { i, _ -> i != index } // 他スロットにおいて
            .all a@{ itemStack2 -> !(variant hasSameId (itemStack2.fairyVariant ?: return@a true)) } // 同種の妖精があってはならない
    }
}

class ContainerFairyCollectionBox(val inventoryPlayer: IInventory, val inventoryTileEntity: IInventory) : Container() {
    init {
        var yi = 17
        repeat(5) { r -> repeat(10) { c -> addSlotToContainer(SmartSlot(inventoryTileEntity, 10 * r + c, 7 + 18 * c + 1, yi + 18 * r + 1)) } }
        yi += 18 * 5 + 13
        repeat(3) { r -> repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, 9 + 9 * r + c, 7 + 9 + 18 * c + 1, yi + 18 * r + 1)) } }
        yi += 18 * 3 + 4
        repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, c, 7 + 9 + 18 * c + 1, yi + 1)) }
    }

    override fun canInteractWith(playerIn: EntityPlayer) = inventoryTileEntity.isUsableByPlayer(playerIn)

    val fairyMasterGrade get() = inventoryTileEntity.itemStacks.mapNotNull { itemStack -> itemStack.fairyVariant }.distinctBy { it.id }.sumBy { it.level }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val slot = inventorySlots[index] ?: return EMPTY_ITEM_STACK // スロットがnullなら終了
        if (!slot.hasStack) return EMPTY_ITEM_STACK // スロットが空なら終了

        val itemStack = slot.stack
        val itemStackOriginal = itemStack.copy()

        // 移動処理
        // itemStackを改変する
        if (index < 50) { // タイルエンティティ→プレイヤー
            if (!mergeItemStack(itemStack, 50, 50 + 9 * 4, true)) return EMPTY_ITEM_STACK
        } else { // プレイヤー→タイルエンティティ
            if (!mergeItemStack(itemStack, 0, 50, false)) return EMPTY_ITEM_STACK
        }

        if (itemStack.isEmpty) { // スタックが丸ごと移動した
            slot.putStack(EMPTY_ITEM_STACK)
        } else { // 部分的に残った
            slot.onSlotChanged()
        }

        if (itemStack.count == itemStackOriginal.count) return EMPTY_ITEM_STACK // アイテムが何も移動していない場合は終了

        // スロットが改変を受けた場合にここを通過する

        slot.onTake(playerIn, itemStack)

        return itemStackOriginal
    }
}

@SideOnly(Side.CLIENT)
class GuiFairyCollectionBox(val container: ContainerFairyCollectionBox) : GuiContainer(container) {
    init {
        xSize = 7 + 18 * 10 + 7
        ySize = 17 + 18 * 5 + 13 + 18 * 3 + 4 + 18 + 7
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()

        var yi = 17
        repeat(5) { r -> repeat(10) { c -> drawSlot((x + 7 + 18 * c).toFloat(), (y + yi + 18 * r).toFloat()) } }
        yi += 18 * 5 + 13
        repeat(3) { r -> repeat(9) { c -> drawSlot((x + 7 + 9 + 18 * c).toFloat(), (y + yi + 18 * r).toFloat()) } }
        yi += 18 * 3 + 4
        repeat(9) { c -> drawSlot((x + 7 + 9 + 18 * c).toFloat(), (y + yi).toFloat()) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        var yi = 7
        fontRenderer.drawString(container.inventoryTileEntity.displayName.unformattedText, yi + 1, yi - 1, 0x404040)
        fontRenderer.drawStringRightAligned("グレード: ${container.fairyMasterGrade}", xSize - 7 - 1, yi - 1, 0x000088) // TRANSLATE Grade
        yi += 10 + 18 * 5 + 3
        fontRenderer.drawString(container.inventoryPlayer.displayName.unformattedText, 7 + 1, yi - 1, 0x404040)
    }
}
