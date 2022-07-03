package miragefairy2019.lib.gui

import miragefairy2019.libkt.IArgb
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawString
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.toArgb
import mirrg.kotlin.hydrogen.unit
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


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

class ComponentLabel(
    container: ContainerComponent,
    point: PointInt,
    private val alignment: Alignment,
    private val color: IArgb,
    private val textSupplier: () -> ITextComponent?
) : ComponentPointBase(container, point) {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouse: PointInt) {
        val text = textSupplier() ?: return
        when (alignment) {
            Alignment.LEFT -> gui.fontRenderer.drawString(text.formattedText, point.x, point.y, color.argb)
            Alignment.CENTER -> gui.fontRenderer.drawStringCentered(text.formattedText, point.x, point.y, color.argb)
            Alignment.RIGHT -> gui.fontRenderer.drawStringRightAligned(text.formattedText, point.x, point.y, color.argb)
        }
    }
}

fun PointContext.label(alignment: Alignment, color: IArgb = 0x404040.toArgb(), textSupplier: () -> ITextComponent?) = ComponentLabel(container, point, alignment, color, textSupplier).also { container.components += it }

class ComponentRectangleLabel(
    container: ContainerComponent,
    rectangle: RectangleInt,
    private val alignment: Alignment,
    private val color: IArgb,
    private val textSupplier: () -> ITextComponent?
) : ComponentRectangleBase(container, rectangle) {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouse: PointInt) {
        val text = textSupplier() ?: return
        when (alignment) {
            Alignment.LEFT -> gui.fontRenderer.drawString(text.formattedText, rectangle, color.argb)
            Alignment.CENTER -> gui.fontRenderer.drawStringCentered(text.formattedText, rectangle, color.argb)
            Alignment.RIGHT -> gui.fontRenderer.drawStringRightAligned(text.formattedText, rectangle, color.argb)
        }
    }
}

fun RectangleContext.label(alignment: Alignment, color: IArgb = 0x404040.toArgb(), textSupplier: () -> ITextComponent?) = ComponentRectangleLabel(container, rectangle, alignment, color, textSupplier).also { container.components += it }


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
