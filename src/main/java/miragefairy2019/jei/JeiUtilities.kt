package miragefairy2019.jei

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager

class JeiUtilities {
    companion object {
        fun FontRenderer.drawStringRightAligned(text: String, x: Int, y: Int, color: Int) = drawString(text, x - getStringWidth(text), y, color)
        fun FontRenderer.drawStringCentered(text: String, x: Int, y: Int, color: Int) = drawString(text, x - getStringWidth(text) / 2, y, color)
        fun drawSlot(x: Float, y: Float) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(x, y, 0f)
            Gui.drawRect(0, 0, 18, 18, 0x4c000000.toInt())
            Gui.drawRect(1, 0, 1 + 16, 1, 0x9a000000.toInt())
            Gui.drawRect(0, 0, 1, 17, 0x9a000000.toInt())
            Gui.drawRect(1, 17, 18, 18, 0xFFFFFFFF.toInt())
            Gui.drawRect(17, 1, 18, 17, 0xFFFFFFFF.toInt())
            GlStateManager.popMatrix()
        }
    }
}
