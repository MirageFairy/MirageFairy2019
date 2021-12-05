package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.item
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.x
import miragefairy2019.libkt.y
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.Supplier

object FairyCollectionBox {
    lateinit var blockFairyCollectionBox: Supplier<BlockFairyCollectionBox>
    lateinit var itemDish: Supplier<ItemBlock>

    const val guiIdFairyCollectionBox = 3
    val module: Module = {
        blockFairyCollectionBox = block({ BlockFairyCollectionBox() }, "fairy_collection_box") {
            setUnlocalizedName("fairyCollectionBox")
            setCreativeTab { ApiMain.creativeTab }
        }
        itemDish = item({ ItemBlock(blockFairyCollectionBox.get()) }, "fairy_collection_box") {
            setCustomModelResourceLocation()
        }
        tileEntity("fairy_collection_box", TileEntityFairyCollectionBox::class.java)
        onInit {
            ApiMain.registerGuiHandler(guiIdFairyCollectionBox, object : ISimpleGuiHandler {
                override fun GuiHandlerContext.onServer() = (tileEntity as? TileEntityFairyCollectionBox)?.let { ContainerFairyCollectionBox(player.inventory, it) }
                override fun GuiHandlerContext.onClient() = (tileEntity as? TileEntityFairyCollectionBox)?.let { GuiFairyCollectionBox(player.inventory, it) }
            }.guiHandler)
        }
    }
}

class BlockFairyCollectionBox : BlockContainer(Material.WOOD) {
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        playerIn.openGui(ModMirageFairy2019.instance, FairyCollectionBox.guiIdFairyCollectionBox, worldIn, pos.x, pos.y, pos.z)
        return true
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyCollectionBox()
}

class TileEntityFairyCollectionBox : TileEntity() {

}

class ContainerFairyCollectionBox(private val inventoryPlayer: IInventory, private val tileEntity: TileEntityFairyCollectionBox) : Container() {
    init {
        repeat(5) { r -> repeat(10) { c -> addSlotToContainer(Slot(inventoryPlayer, 0, 8 + c * 18, 17 + r * 18 + 1)) } }
        repeat(3) { r -> repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, 9 + r * 9 + c, 9 + 8 + c * 18, 84 + 18 * 2 + r * 18 + 1)) } }
        repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, c, 9 + 8 + c * 18, 142 + 18 * 2 + 1)) } // TODO
    }

    override fun canInteractWith(playerIn: EntityPlayer) = true // TODO
}

class GuiFairyCollectionBox(private val inventoryPlayer: IInventory, private val tileEntity: TileEntityFairyCollectionBox) : GuiContainer(ContainerFairyCollectionBox(inventoryPlayer, tileEntity)) {
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
        fontRenderer.drawString("Fairy Collection Box", 8, 6, 0x404040) // TODO
        fontRenderer.drawString(inventoryPlayer.displayName.unformattedText, 8, ySize - 96 + 2 + 18 * 0, 0x404040)
    }
}
