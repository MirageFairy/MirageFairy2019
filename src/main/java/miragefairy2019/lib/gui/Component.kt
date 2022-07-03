package miragefairy2019.lib.gui

import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.translate
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.hydrogen.unit
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


// Api

interface IComponent<T> {

    fun onContainerInit() = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerBackgroundLayer(gui: T, mouse: PointInt, partialTicks: Float) = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerForegroundLayer(gui: T, mouse: PointInt) = Unit

    @SideOnly(Side.CLIENT)
    fun drawScreen(gui: T, mouse: PointInt, partialTicks: Float) = Unit

    @SideOnly(Side.CLIENT)
    fun mouseClicked(gui: T, mouse: PointInt, mouseButton: Int) = Unit

}


// Implementations

class ComponentSlot(val container: ContainerComponent, val x: Int, val y: Int, slotCreator: (x: Int, y: Int) -> Slot) : IComponent<GuiComponent> {
    val slot = slotCreator(x + 1, y + 1)

    override fun onContainerInit() = unit { container.addSlotToContainer(slot) }

    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, mouse: PointInt, partialTicks: Float) = drawSlot(gui.x + x.toFloat(), gui.y + y.toFloat())
}

enum class Alignment { LEFT, CENTER, RIGHT }

class ComponentLabel(val x: Int, val y: Int, val alignment: Alignment, val color: Int = 0x404040, val textSupplier: () -> ITextComponent?) : IComponent<GuiComponent> {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouse: PointInt) {
        when (alignment) {
            Alignment.LEFT -> textSupplier()?.let { gui.fontRenderer.drawString(it.formattedText, x, y, color) }
            Alignment.CENTER -> textSupplier()?.let { gui.fontRenderer.drawStringCentered(it.formattedText, x, y, color) }
            Alignment.RIGHT -> textSupplier()?.let { gui.fontRenderer.drawStringRightAligned(it.formattedText, x, y, color) }
        }
    }
}

class ComponentBackgroundLabel(val x: Int, val y: Int, val alignment: Alignment, val color: Int = 0x404040, val textSupplier: () -> ITextComponent?) : IComponent<GuiComponent> {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, mouse: PointInt, partialTicks: Float) {
        when (alignment) {
            Alignment.LEFT -> textSupplier()?.let { gui.fontRenderer.drawString(it.formattedText, gui.x + x, gui.y + y, color) }
            Alignment.CENTER -> textSupplier()?.let { gui.fontRenderer.drawStringCentered(it.formattedText, gui.x + x, gui.y + y, color) }
            Alignment.RIGHT -> textSupplier()?.let { gui.fontRenderer.drawStringRightAligned(it.formattedText, gui.x + x, gui.y + y, color) }
        }
    }
}

class ComponentBackgroundImage(val x: Int, val y: Int, val color: Int = 0xFFFFFFFF.toInt(), val textureSupplier: () -> ResourceLocation) : IComponent<GuiComponent> {
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

class ComponentTooltip(private val rectangle: RectangleInt, private val textSupplier: () -> List<String>?) : IComponent<GuiComponent> {
    @SideOnly(Side.CLIENT)
    override fun drawScreen(gui: GuiComponent, mouse: PointInt, partialTicks: Float) {
        if (mouse !in rectangle.translate(gui.x, gui.y)) return
        val text = textSupplier() ?: return
        gui.drawHoveringText(text, mouse.x, mouse.y)
    }
}

class SlotResult(private val player: EntityPlayer, inventory: IInventory, slotIndex: Int, x: Int, y: Int) : Slot(inventory, slotIndex, x, y) {

    override fun isItemValid(stack: ItemStack) = false


    // craft

    private var removeCount = 0

    override fun decrStackSize(amount: Int): ItemStack {
        if (hasStack) removeCount += amount atMost stack.count
        return super.decrStackSize(amount)
    }

    override fun onTake(player: EntityPlayer, itemStack: ItemStack): ItemStack {
        onCrafting(itemStack)
        super.onTake(player, itemStack)
        return itemStack
    }

    override fun onCrafting(itemStack: ItemStack, amount: Int) {
        removeCount += amount
        onCrafting(itemStack)
    }

    override fun onCrafting(itemStack: ItemStack) {
        itemStack.onCrafting(player.world, player, removeCount)
        removeCount = 0
    }

}
