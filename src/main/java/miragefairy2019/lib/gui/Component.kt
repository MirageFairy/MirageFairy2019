package miragefairy2019.lib.gui

import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.minus
import mirrg.kotlin.hydrogen.unit
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


// Api

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


// Implementations

abstract class ComponentBase(val container: ContainerComponent) : IComponent

abstract class ComponentRectangleBase(container: ContainerComponent, val rectangle: RectangleInt) : ComponentBase(container)

abstract class ComponentPointBase(container: ContainerComponent, val point: PointInt) : ComponentBase(container)


class ComponentSlot(container: ContainerComponent, point: PointInt, slotFactory: (x: Int, y: Int) -> Slot) : ComponentPointBase(container, point) {
    val slot = slotFactory(point.x + 1, point.y + 1)

    override fun onContainerInit() = unit { container.addSlotToContainer(slot) }

    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, mouse: PointInt, partialTicks: Float) = drawSlot(gui.x + point.x.toFloat(), gui.y + point.y.toFloat())
}

fun PointContext.slot(slotFactory: (x: Int, y: Int) -> Slot) = ComponentSlot(container, point, slotFactory).also { container.components += it }


class ComponentButton(container: ContainerComponent, rectangle: RectangleInt, private val action: (gui: GuiComponent, mouse: PointInt, mouseButton: Int) -> Unit) : ComponentRectangleBase(container, rectangle) {
    @SideOnly(Side.CLIENT)
    override fun mouseClicked(gui: GuiComponent, mouse: PointInt, mouseButton: Int) {
        if (mouse - gui.position in rectangle) action(gui, mouse, mouseButton)
    }
    // TODO 枠の描画
}

fun RectangleContext.button(action: (gui: GuiComponent, mouse: PointInt, mouseButton: Int) -> Unit) = ComponentButton(container, rectangle, action).also { container.components += it }


class ComponentTooltip(container: ContainerComponent, rectangle: RectangleInt, private val textSupplier: () -> List<String>?) : ComponentRectangleBase(container, rectangle) {
    @SideOnly(Side.CLIENT)
    override fun drawScreen(gui: GuiComponent, mouse: PointInt, partialTicks: Float) {
        if (mouse - gui.position !in rectangle) return
        val text = textSupplier() ?: return
        gui.drawHoveringText(text, mouse.x, mouse.y)
    }
}

fun RectangleContext.tooltip(textSupplier: () -> List<String>?) = ComponentTooltip(container, rectangle, textSupplier).also { container.components += it }


enum class Alignment { LEFT, CENTER, RIGHT }

class ComponentLabel(val x: Int, val y: Int, val alignment: Alignment, val color: Int = 0x404040, val textSupplier: () -> ITextComponent?) : IComponent {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouse: PointInt) {
        when (alignment) {
            Alignment.LEFT -> textSupplier()?.let { gui.fontRenderer.drawString(it.formattedText, x, y, color) }
            Alignment.CENTER -> textSupplier()?.let { gui.fontRenderer.drawStringCentered(it.formattedText, x, y, color) }
            Alignment.RIGHT -> textSupplier()?.let { gui.fontRenderer.drawStringRightAligned(it.formattedText, x, y, color) }
        }
    }
}

class ComponentBackgroundImage(val x: Int, val y: Int, val color: Int = 0xFFFFFFFF.toInt(), val textureSupplier: () -> ResourceLocation) : IComponent {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, mouse: PointInt, partialTicks: Float) {
        GlStateManager.color(
            (color shr 16 and 0xFF) / 255.0f,
            (color shr 8 and 0xFF) / 255.0f,
            (color shr 0 and 0xFF) / 255.0f,
            (color shr 24 and 0xFF) / 255.0f
        )
        GlStateManager.enableBlend()
        gui.mc.textureManager.bindTexture(textureSupplier())
        Gui.drawModalRectWithCustomSizedTexture(gui.x + x, gui.y + y, 0.0f, 0.0f, 16, 16, 16.0f, 16.0f)
        GlStateManager.disableBlend()
    }
}
