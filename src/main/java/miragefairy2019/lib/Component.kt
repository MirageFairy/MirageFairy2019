package miragefairy2019.lib

import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.unit
import miragefairy2019.libkt.x
import miragefairy2019.libkt.y
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

// Api

interface IComponent {
    fun onInit() = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerBackgroundLayer(gui: GuiComponent, partialTicks: Float, mouseX: Int, mouseY: Int) = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouseX: Int, mouseY: Int) = Unit
}


// Implements

class ContainerComponent : Container() {
    val components = mutableListOf<IComponent>()
    val interactInventories = mutableListOf<IInventory>()
    var width = 0
    var height = 0


    // Overrides

    fun init() {
        components.forEach { it.onInit() }
    }

    override fun canInteractWith(player: EntityPlayer) = interactInventories.all { it.isUsableByPlayer(player) }

    // TODO
    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        return EMPTY_ITEM_STACK
    }


    // Public化
    public override fun addSlotToContainer(slot: Slot): Slot = super.addSlotToContainer(slot)

}

fun container(block: ContainerComponent.() -> Unit): ContainerComponent {
    val container = ContainerComponent()
    container.block()
    container.init()
    return container
}

@SideOnly(Side.CLIENT)
class GuiComponent(val container: ContainerComponent) : GuiContainer(container) {

    // Overrides

    init {
        xSize = container.width
        ySize = container.height
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()
        container.components.forEach { it.drawGuiContainerBackgroundLayer(this, partialTicks, mouseX, mouseY) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        container.components.forEach { it.drawGuiContainerForegroundLayer(this, mouseX, mouseY) }
    }


    // Public化
    val fontRenderer: FontRenderer get() = super.fontRenderer

}

@SideOnly(Side.CLIENT)
fun ContainerComponent.createGui() = GuiComponent(this)


// Utils

class ComponentSlot(val container: ContainerComponent, val x: Int, val y: Int, slotCreator: (x: Int, y: Int) -> Slot) : IComponent {
    val slot = slotCreator(x + 1, y + 1)

    override fun onInit() = unit { container.addSlotToContainer(slot) }

    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, partialTicks: Float, mouseX: Int, mouseY: Int) = drawSlot(gui.x + x.toFloat(), gui.y + y.toFloat())
}

enum class Alignment { LEFT, CENTER, RIGHT }

class ComponentLabel(val x: Int, val y: Int, val alignment: Alignment, val color: Int = 0x404040, val textSupplier: () -> String) : IComponent {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouseX: Int, mouseY: Int) {
        when (alignment) {
            Alignment.LEFT -> gui.fontRenderer.drawString(textSupplier(), x, y, color)
            Alignment.CENTER -> gui.fontRenderer.drawStringCentered(textSupplier(), x, y, color)
            Alignment.RIGHT -> gui.fontRenderer.drawStringRightAligned(textSupplier(), x, y, color)
        }
    }
}
