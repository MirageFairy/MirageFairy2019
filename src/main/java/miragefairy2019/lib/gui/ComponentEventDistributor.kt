package miragefairy2019.lib.gui

import miragefairy2019.libkt.IArgb
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.drawString
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.toArgb
import mirrg.kotlin.hydrogen.unit
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


class EventContainer<T> {
    private val listeners = mutableListOf<T>()
    operator fun invoke(listener: T) = run { listeners += listener }
    fun fire(invoker: (T) -> Unit) = listeners.forEach { invoker(it) }
}

class ComponentEventDistributor(val rectangle: RectangleInt) : IComponent {

    val onScreenDraw = EventContainer<(gui: GuiComponent, mouse: PointInt, partialTicks: Float) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun drawScreen(gui: GuiComponent, mouse: PointInt, partialTicks: Float) = onScreenDraw.fire { it(gui, mouse - gui.position, partialTicks) }


    val onForegroundDraw = EventContainer<(gui: GuiComponent, mouse: PointInt) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouse: PointInt) = onForegroundDraw.fire { it(gui, mouse - gui.position) }


    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, mouse: PointInt, partialTicks: Float) = Unit


    val onMouseClicked = EventContainer<(gui: GuiComponent, mouse: PointInt, mouseButton: Int) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun mouseClicked(gui: GuiComponent, mouse: PointInt, mouseButton: Int) = onMouseClicked.fire { it(gui, mouse - gui.position, mouseButton) }

}

fun ContainerComponent.component(rectangle: RectangleInt, block: ComponentEventDistributor.() -> Unit) {
    components += ComponentEventDistributor(rectangle).apply { block() }
}


class RectangleContext(val container: ContainerComponent, val rectangle: RectangleInt)

fun ContainerComponent.rectangle(rectangle: RectangleInt, block: RectangleContext.() -> Unit) = block(RectangleContext(this, rectangle))

abstract class ComponentBase(val container: ContainerComponent) : IComponent

abstract class ComponentRectangleBase(container: ContainerComponent, val rectangle: RectangleInt) : ComponentBase(container)


class ComponentButton(container: ContainerComponent, rectangle: RectangleInt, val action: (gui: GuiComponent, mouse: PointInt, mouseButton: Int) -> Unit) : ComponentRectangleBase(container, rectangle) {
    @SideOnly(Side.CLIENT)
    override fun mouseClicked(gui: GuiComponent, mouse: PointInt, mouseButton: Int) {
        if (mouse - gui.position in rectangle) action(gui, mouse, mouseButton)
    }
    // TODO 枠の描画
}

fun RectangleContext.button(action: (gui: GuiComponent, mouse: PointInt, mouseButton: Int) -> Unit) = unit { container.components += ComponentButton(container, rectangle, action) }


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
