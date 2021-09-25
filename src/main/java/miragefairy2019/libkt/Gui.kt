package miragefairy2019.libkt

import net.minecraft.client.gui.inventory.GuiContainer

val GuiContainer.x get() = (width - xSize) / 2
val GuiContainer.y get() = (height - ySize) / 2
val GuiContainer.rectangle get() = RectangleInt(x, y, xSize, ySize)
val GuiContainer.position get() = PointInt(x, y)
