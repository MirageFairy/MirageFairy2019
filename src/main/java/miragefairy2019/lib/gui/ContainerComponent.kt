package miragefairy2019.lib.gui

import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.drawGuiBackground
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IContainerListener
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface GuiFactory {
    @SideOnly(Side.CLIENT)
    operator fun invoke(component: ContainerComponent): GuiComponent
}

inline fun GuiFactory(crossinline function: (component: ContainerComponent) -> GuiComponent) = object : GuiFactory {
    override fun invoke(component: ContainerComponent) = function(component)
}

class WindowProperty(var value: Int = 0, val changeListener: () -> Unit = {})

abstract class ContainerComponent2 : Container() {

}

class ContainerComponent(private val guiFactory: GuiFactory) : ContainerComponent2() {
    val components = mutableListOf<IComponent<GuiComponent>>()
    var width = 0
    var height = 0


    // Gui

    @SideOnly(Side.CLIENT)
    fun createGui() = guiFactory(this)


    // Overrides

    fun init() {
        components.forEach { it.onContainerInit() }
    }


    // Interact

    val interactInventories = mutableListOf<IInventory>()

    override fun canInteractWith(player: EntityPlayer) = interactInventories.all { it.isUsableByPlayer(player) }


    // Transfer

    class SlotGroup

    private val groupToSlots = mutableMapOf<SlotGroup, MutableList<ComponentSlot>>()
    private val slotToGroup = mutableMapOf<ComponentSlot, SlotGroup>()

    private val mapping = mutableMapOf<SlotGroup, MutableList<Pair<SlotGroup, Boolean>>>()

    fun addSlotTransferMapping(srcSlotGroup: SlotGroup, destSlotGroup: SlotGroup, isReversed: Boolean = false) {
        mapping.computeIfAbsent(srcSlotGroup) { mutableListOf() } += destSlotGroup to isReversed
    }

    fun getSlotTransferMapping(srcSlotGroup: SlotGroup): List<Pair<SlotGroup, Boolean>> = mapping[srcSlotGroup] ?: listOf()

    infix fun ComponentSlot.belongs(slotGroup: SlotGroup): ComponentSlot {
        val list = groupToSlots.computeIfAbsent(slotGroup) { mutableListOf() }
        list += this
        slotToGroup[this] = slotGroup
        return this
    }

    fun getComponentSlot(index: Int) = components.asSequence().filterIsInstance<ComponentSlot>().find { it.slot.slotNumber == index }
    fun getComponentSlots(slotGroup: SlotGroup): List<ComponentSlot> = groupToSlots[slotGroup] ?: listOf()
    fun getSlotGroup(componentSlot: ComponentSlot) = slotToGroup[componentSlot]

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val componentSlot = getComponentSlot(index) ?: return EMPTY_ITEM_STACK // スロットが存在しないなら終了
        if (!componentSlot.slot.hasStack) return EMPTY_ITEM_STACK // スロットが空なら終了

        val itemStack = componentSlot.slot.stack
        val itemStackOriginal = itemStack.copy()

        // 移動処理
        // itemStackを改変する
        val slotGroup = getSlotGroup(componentSlot)
        if (slotGroup != null) {
            val destComponentSlots = getSlotTransferMapping(slotGroup).map { (slotGroup, isReversed) ->
                val componentSlots = getComponentSlots(slotGroup)
                if (isReversed) componentSlots.reversed() else componentSlots
            }.flatten().map { it.slot }

            // 移動処理
            if (!miragefairy2019.lib.mergeItemStack(itemStack, destComponentSlots).isChanged) return ItemStack.EMPTY

        }

        if (itemStack.isEmpty) { // スタックが丸ごと移動した
            componentSlot.slot.putStack(ItemStack.EMPTY)
        } else { // 部分的に残った
            componentSlot.slot.onSlotChanged()
        }

        if (itemStack.count == itemStackOriginal.count) return ItemStack.EMPTY // アイテムが何も移動していない場合は終了

        // スロットが改変を受けた場合にここを通過する

        componentSlot.slot.onTake(playerIn, itemStack)

        return itemStackOriginal
    }


    // Window Property

    val windowProperties = mutableMapOf<Int, WindowProperty>()

    override fun addListener(listener: IContainerListener) {
        super.addListener(listener)
        windowProperties.forEach { (id, windowProperty) ->
            listener.sendWindowProperty(this, id, windowProperty.value)
        }
    }

    // TODO detectAndSendChanges()

    @SideOnly(Side.CLIENT)
    override fun updateProgressBar(id: Int, data: Int) {
        val windowProperty = windowProperties[id] ?: return
        windowProperty.value = data
        windowProperty.changeListener()
    }


    // Public化
    public override fun addSlotToContainer(slot: Slot): Slot = super.addSlotToContainer(slot)

}

fun container(guiFactory: GuiFactory, block: ContainerComponent.() -> Unit): ContainerComponent {
    val container = ContainerComponent(guiFactory)
    container.block()
    container.init()
    return container
}

@SideOnly(Side.CLIENT)
abstract class GuiComponent2(container: ContainerComponent) : GuiContainer(container) {

}

@SideOnly(Side.CLIENT)
abstract class GuiComponent(val container: ContainerComponent) : GuiComponent2(container) {

    // Overrides

    init {
        xSize = container.width
        ySize = container.height
    }


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
