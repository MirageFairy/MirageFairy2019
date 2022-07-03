package miragefairy2019.lib.gui

import miragefairy2019.libkt.EMPTY_ITEM_STACK
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IContainerListener
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface GuiFactory {
    @SideOnly(Side.CLIENT)
    operator fun invoke(component: ContainerIntegrated): GuiIntegrated
}

inline fun GuiFactory(crossinline function: (component: ContainerIntegrated) -> GuiIntegrated) = object : GuiFactory {
    override fun invoke(component: ContainerIntegrated) = function(component)
}

class WindowProperty(var value: Int = 0, val changeListener: () -> Unit = {})

class ContainerIntegrated(private val guiFactory: GuiFactory) : ContainerComponent() {
    var width = 0
    var height = 0


    // Gui

    @SideOnly(Side.CLIENT)
    fun createGui() = guiFactory(this)


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

}

fun container(guiFactory: GuiFactory, block: ContainerIntegrated.() -> Unit): ContainerIntegrated {
    val container = ContainerIntegrated(guiFactory)
    container.block()
    container.init()
    return container
}

@SideOnly(Side.CLIENT)
abstract class GuiIntegrated(container: ContainerIntegrated) : GuiComponent(container) {
    init {
        xSize = container.width
        ySize = container.height
    }
}
