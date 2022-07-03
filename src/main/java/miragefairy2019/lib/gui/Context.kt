package miragefairy2019.lib.gui

import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt

class RectangleContext(val container: ContainerComponent, val rectangle: RectangleInt)

fun ContainerComponent.rectangle(rectangle: RectangleInt, block: RectangleContext.() -> Unit) = block(RectangleContext(this, rectangle))


class PointContext(val container: ContainerComponent, val point: PointInt)

fun ContainerComponent.point(x: Int, y: Int, block: PointContext.() -> Unit) = block(PointContext(this, PointInt(x, y)))
