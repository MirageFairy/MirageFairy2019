package miragefairy2019.lib.gui

import miragefairy2019.libkt.IArgb
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawString
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.toArgb
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

val GuiContainer.x get() = (width - xSize) / 2
val GuiContainer.y get() = (height - ySize) / 2
val GuiContainer.rectangle get() = RectangleInt(x, y, xSize, ySize)
val GuiContainer.position get() = PointInt(x, y)


class EventContainer<T> {
    private val listeners = mutableListOf<T>()
    operator fun invoke(listener: T) = run { listeners += listener }
    fun fire(invoker: (T) -> Unit) = listeners.forEach { invoker(it) }
}

interface IComponent2 {

    @SideOnly(Side.CLIENT)
    fun drawScreen(gui: GuiComponentBase, mouse: PointInt, partialTicks: Float)

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerForegroundLayer(gui: GuiComponentBase, mouse: PointInt)

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerBackgroundLayer(gui: GuiComponentBase, mouse: PointInt, partialTicks: Float)

    @SideOnly(Side.CLIENT)
    fun mouseClicked(gui: GuiComponentBase, mouse: PointInt, mouseButton: Int)

}

class ComponentEventDistributor(val rectangle: RectangleInt) : IComponent2 {

    val onScreenDraw = EventContainer<(gui: GuiComponentBase, mouse: PointInt, partialTicks: Float) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun drawScreen(gui: GuiComponentBase, mouse: PointInt, partialTicks: Float) = onScreenDraw.fire { it(gui, mouse - gui.position, partialTicks) }


    val onForegroundDraw = EventContainer<(gui: GuiComponentBase, mouse: PointInt) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponentBase, mouse: PointInt) = onForegroundDraw.fire { it(gui, mouse - gui.position) }


    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponentBase, mouse: PointInt, partialTicks: Float) = Unit


    val onMouseClicked = EventContainer<(gui: GuiComponentBase, mouse: PointInt, mouseButton: Int) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun mouseClicked(gui: GuiComponentBase, mouse: PointInt, mouseButton: Int) = onMouseClicked.fire { it(gui, mouse - gui.position, mouseButton) }

}

abstract class ContainerComponentBase : Container() {
    val components = mutableListOf<IComponent2>()
}

@SideOnly(Side.CLIENT)
abstract class GuiComponentBase(val containerComponent: ContainerComponentBase) : GuiContainer(containerComponent) {

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        containerComponent.components.forEach { it.drawScreen(this, PointInt(mouseX, mouseY), partialTicks) }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()
        containerComponent.components.forEach { it.drawGuiContainerBackgroundLayer(this, PointInt(mouseX, mouseY), partialTicks) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        containerComponent.components.forEach { it.drawGuiContainerForegroundLayer(this, PointInt(mouseX, mouseY)) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        containerComponent.components.forEach { it.mouseClicked(this, PointInt(mouseX, mouseY), mouseButton) }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    // Public化
    val fontRenderer: FontRenderer get() = super.fontRenderer

}

fun ContainerComponentBase.component(rectangle: RectangleInt, block: ComponentEventDistributor.() -> Unit) {
    components += ComponentEventDistributor(rectangle).apply { block() }
}


enum class TextAlignment { LEFT, CENTER, RIGHT }


fun ComponentEventDistributor.label(color: IArgb = 0xFF000000.toArgb(), align: TextAlignment = TextAlignment.LEFT, getText: () -> String) {
    onForegroundDraw { gui, _ ->
        when (align) {
            TextAlignment.LEFT -> gui.fontRenderer.drawString(getText(), rectangle, color.argb)
            TextAlignment.CENTER -> gui.fontRenderer.drawStringCentered(getText(), rectangle, color.argb)
            TextAlignment.RIGHT -> gui.fontRenderer.drawStringRightAligned(getText(), rectangle, color.argb)
        }
    }
}

fun ComponentEventDistributor.tooltip(vararg text: String) = tooltip { listOf(*text) }

fun ComponentEventDistributor.tooltip(getText: () -> List<String>) = onScreenDraw { gui, mouse, _ ->
    if (mouse in rectangle) gui.drawHoveringText(getText(), mouse.x + gui.x, mouse.y + gui.y)
}

fun ComponentEventDistributor.button(onClick: (gui: GuiComponentBase, mouseButton: Int) -> Unit) = onMouseClicked { gui, mouse, mouseButton ->
    if (mouse in rectangle) onClick(gui, mouseButton)
} // TODO 枠
