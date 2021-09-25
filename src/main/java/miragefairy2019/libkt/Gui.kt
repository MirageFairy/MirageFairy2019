package miragefairy2019.libkt

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer

val GuiContainer.x get() = (width - xSize) / 2
val GuiContainer.y get() = (height - ySize) / 2
val GuiContainer.rectangle get() = RectangleInt(x, y, xSize, ySize)
val GuiContainer.position get() = PointInt(x, y)


class EventContainer<T> {
    private val listeners = mutableListOf<T>()
    operator fun invoke(listener: T) = run { listeners += listener }
    fun fire(invoker: (T) -> Unit) = listeners.forEach { invoker(it) }
}

class Component(val container: GuiContainer, val rectangle: RectangleInt) {
    val onScreenDraw = EventContainer<(mouse: PointInt, partialTicks: Float) -> Unit>()
    val onForegroundDraw = EventContainer<(mouse: PointInt) -> Unit>()
}

fun GuiContainer.component(rectangle: RectangleInt, block: Component.() -> Unit) = Component(this, rectangle).apply { block() }


enum class TextAlignment { LEFT, CENTER, RIGHT }


fun Component.label(sFontRenderer: () -> FontRenderer, text: String, color: IArgb = 0xFF000000.toArgb(), align: TextAlignment = TextAlignment.LEFT) {
    onForegroundDraw {
        when (align) {
            TextAlignment.LEFT -> sFontRenderer().drawString(text, rectangle, color.argb)
            TextAlignment.CENTER -> sFontRenderer().drawStringCentered(text, rectangle, color.argb)
            TextAlignment.RIGHT -> sFontRenderer().drawStringRightAligned(text, rectangle, color.argb)
        }
    }
}

fun Component.tooltip(text: String) = onScreenDraw { mouse, _ ->
    if (mouse in rectangle) container.drawHoveringText(listOf(text), mouse.x + container.x, mouse.y + container.y)
}
