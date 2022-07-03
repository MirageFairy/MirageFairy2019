package miragefairy2019.lib.gui

import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.drawGuiBackground
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface IComponent {

    fun onContainerInit() = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerBackgroundLayer(gui: GuiComponent, mouse: PointInt, partialTicks: Float) = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouse: PointInt) = Unit

    @SideOnly(Side.CLIENT)
    fun drawScreen(gui: GuiComponent, mouse: PointInt, partialTicks: Float) = Unit

    @SideOnly(Side.CLIENT)
    fun mouseClicked(gui: GuiComponent, mouse: PointInt, mouseButton: Int) = Unit

}

abstract class ContainerComponent : Container() {
    val components = mutableListOf<IComponent>()

    fun init() {
        components.forEach { it.onContainerInit() }
    }


    // Public化
    public override fun addSlotToContainer(slot: Slot): Slot = super.addSlotToContainer(slot)

}

@SideOnly(Side.CLIENT)
abstract class GuiComponent(private val container: ContainerComponent) : GuiContainer(container) {

    // イベント

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
        container.components.forEach { it.drawScreen(this, PointInt(mouseX, mouseY), partialTicks) }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()
        container.components.forEach { it.drawGuiContainerBackgroundLayer(this, PointInt(mouseX, mouseY), partialTicks) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        container.components.forEach { it.drawGuiContainerForegroundLayer(this, PointInt(mouseX, mouseY)) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        container.components.forEach { it.mouseClicked(this, PointInt(mouseX, mouseY), mouseButton) }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }


    // Public化
    val fontRenderer: FontRenderer get() = super.fontRenderer

}
