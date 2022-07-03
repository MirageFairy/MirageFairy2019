package miragefairy2019.lib.gui

import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.drawGuiBackground
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

val GuiContainer.x get() = (width - xSize) / 2
val GuiContainer.y get() = (height - ySize) / 2
val GuiContainer.rectangle get() = RectangleInt(x, y, xSize, ySize)
val GuiContainer.position get() = PointInt(x, y)


abstract class ContainerComponentBase : Container() {
    val components = mutableListOf<IComponent<GuiComponentBase>>()

    fun init() {
        components.forEach { it.onContainerInit() }
    }
}

@SideOnly(Side.CLIENT)
abstract class GuiComponentBase(private val container: ContainerComponentBase) : GuiContainer(container) {

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

fun ContainerComponentBase.component(rectangle: RectangleInt, block: ComponentEventDistributor.() -> Unit) {
    components += ComponentEventDistributor(rectangle).apply { block() }
}
