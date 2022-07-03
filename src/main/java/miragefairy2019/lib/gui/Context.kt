package miragefairy2019.lib.gui

import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt

class RectangleContext(val container: ContainerComponent, val rectangle: RectangleInt)

fun ContainerComponent.rectangle(left: Int, top: Int, width: Int, height: Int, block: RectangleContext.() -> Unit = {}) = RectangleContext(this, RectangleInt(left, top, width, height)).also { block(it) }


class PointContext(val container: ContainerComponent, val point: PointInt)

fun ContainerComponent.point(x: Int, y: Int, block: PointContext.() -> Unit = {}) = PointContext(this, PointInt(x, y)).also { block(it) }
