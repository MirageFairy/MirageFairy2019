package miragefairy2019.mod3.skill

import miragefairy2019.libkt.Component
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.TextAlignment
import miragefairy2019.libkt.argb
import miragefairy2019.libkt.component
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.label
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.position
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.toArgb
import miragefairy2019.libkt.tooltip
import miragefairy2019.mod3.skill.api.ApiSkill
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container

const val guiIdSkill = 2

class ContainerSkill : Container() {
    override fun canInteractWith(playerIn: EntityPlayer) = true
}

class GuiSkill : GuiContainer(ContainerSkill()) {

    private val components = mutableListOf<Component>()

    init {
        components += component(RectangleInt(4, 4, xSize - 4 - 4 - 20 - 20, 10)) {
            label(::fontRenderer, "マスタリ名", color = 0xFF404040.toArgb())
        }
        components += component(RectangleInt(xSize - 44, 4, 20, 10)) {
            label(::fontRenderer, "MLv", color = argb(0xFF404040.toInt()), align = TextAlignment.RIGHT)
            tooltip("マスタリレベルはある領域に関する理解の深さです。")
        }
        components += component(RectangleInt(xSize - 24, 4, 20, 10)) {
            label(::fontRenderer, "SLv", color = argb(0xFF404040.toInt()), align = TextAlignment.RIGHT)
            tooltip("スキルレベルは個々のアクションの強さです。")
        }
        EnumMastery.values().forEachIndexed { i, it ->
            components += component(RectangleInt(4 + 8 * it.layer, 14 + 10 * i, xSize - 4 - 4 - 20 - 20 - 8 * it.layer, 10)) {
                label(::fontRenderer, it.displayName.unformattedText, color = 0xFF000000.toArgb())
                tooltip(it.displayPoem.unformattedText)
            }
            components += component(RectangleInt(xSize - 44, 14 + 10 * i, 20, 10)) {
                label(::fontRenderer, "${ApiSkill.skillManager.clientSkillContainer.getMasteryLevel(it)}", color = 0xFF000000.toArgb(), align = TextAlignment.RIGHT)
            }
            components += component(RectangleInt(xSize - 24, 14 + 10 * i, 20, 10)) {
                label(::fontRenderer, "${ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it)}", color = 0xFF000000.toArgb(), align = TextAlignment.RIGHT)
            }
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        components.forEach { it.onScreenDraw.fire { it(PointInt(mouseX, mouseY) - position, partialTicks) } }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) = rectangle.drawGuiBackground()

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        components.forEach { it.onForegroundDraw.fire { it(PointInt(mouseX, mouseY) - position) } }
    }
}
