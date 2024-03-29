package miragefairy2019.libkt

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11

fun FontRenderer.drawString(text: String, rectangle: RectangleInt, color: Int) = drawString(text, rectangle.left, rectangle.middle - FONT_HEIGHT / 2, color)
fun FontRenderer.drawStringRightAligned(text: String, x: Int, y: Int, color: Int) = drawString(text, x - getStringWidth(text), y, color)
fun FontRenderer.drawStringRightAligned(text: String, rectangle: RectangleInt, color: Int) = drawStringRightAligned(text, rectangle.right, rectangle.middle - FONT_HEIGHT / 2, color)
fun FontRenderer.drawStringCentered(text: String, x: Int, y: Int, color: Int) = drawString(text, x - getStringWidth(text) / 2, y, color)
fun FontRenderer.drawStringCentered(text: String, rectangle: RectangleInt, color: Int) = drawStringCentered(text, rectangle.center, rectangle.middle - FONT_HEIGHT / 2, color)

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


fun drawTriangle(p1: Complex, p2: Complex, p3: Complex, color: IRgb) {
    val tessellator = Tessellator.getInstance()
    val bufferbuilder = tessellator.buffer
    GlStateManager.enableBlend()
    GlStateManager.disableTexture2D()
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
    GlStateManager.color(color.rf, color.gf, color.bf, 1f)
    bufferbuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION)
    bufferbuilder.pos(p1.red, p1.imd, 0.0).endVertex()
    bufferbuilder.pos(p2.red, p2.imd, 0.0).endVertex()
    bufferbuilder.pos(p3.red, p3.imd, 0.0).endVertex()
    tessellator.draw()
    GlStateManager.enableTexture2D()
    GlStateManager.disableBlend()
}


fun RectangleInt.draw(color: IArgb) = Gui.drawRect(left, top, right, bottom, color.argb)
fun RectangleInt.drawGuiBackground() {

    // 外枠

    // 枠線
    shrink(2, 0, 3, 0).withHeight(1).draw(0xFF000000.toInt().toArgb())
    shrink(0, 2, 0, 3).withWidth(1).draw(0xFF000000.toInt().toArgb())
    shrink(3, 0, 2, 0).withHeight(-1).draw(0xFF000000.toInt().toArgb())
    shrink(0, 3, 0, 2).withWidth(-1).draw(0xFF000000.toInt().toArgb())

    // 左上
    shrink(1, 1, 0, 0).withSize(1, 1).draw(0xFF000000.toInt().toArgb())

    // 左下
    shrink(1, 0, 0, 2).withSize(1, -1).draw(0xFF000000.toInt().toArgb())
    shrink(2, 0, 0, 1).withSize(1, -1).draw(0xFF000000.toInt().toArgb())

    // 右上
    shrink(0, 1, 2, 0).withSize(-1, 1).draw(0xFF000000.toInt().toArgb())
    shrink(0, 2, 1, 0).withSize(-1, 1).draw(0xFF000000.toInt().toArgb())

    // 右下
    shrink(0, 0, 1, 1).withSize(-1, -1).draw(0xFF000000.toInt().toArgb())


    // 背景

    shrink(2).draw(0xFFC6C6C6.toInt().toArgb())


    // 左上白帯

    shrink(2, 1, 3, 0).withHeight(2).draw(0xFFFFFFFF.toInt().toArgb())
    shrink(1, 2, 0, 3).withWidth(2).draw(0xFFFFFFFF.toInt().toArgb())

    shrink(3, 3, 0, 0).withSize(1, 1).draw(0xFFFFFFFF.toInt().toArgb())


    // 右下灰帯
    shrink(3, 0, 2, 1).withHeight(-2).draw(0xFF555555.toInt().toArgb())
    shrink(0, 3, 1, 2).withWidth(-2).draw(0xFF555555.toInt().toArgb())

    shrink(0, 0, 3, 3).withSize(-1, -1).draw(0xFF555555.toInt().toArgb())

}
