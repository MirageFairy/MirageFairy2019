package miragefairy2019.libkt

data class RectangleInt(val left: Int, val top: Int, val width: Int, val height: Int)

// 位置
val RectangleInt.center get() = left + width / 2
val RectangleInt.right get() = left + width
val RectangleInt.middle get() = top + height / 2
val RectangleInt.bottom get() = top + height

// 移動
fun RectangleInt.translate(x: Int, y: Int) = RectangleInt(this.left + x, this.top + y, width, height)
fun RectangleInt.translate(point: PointInt) = translate(point.x, point.y)
operator fun RectangleInt.plus(point: PointInt) = translate(point.x, point.y)
operator fun RectangleInt.minus(point: PointInt) = translate(-point.x, -point.y)

// 成長
fun RectangleInt.grow(left: Int, top: Int, right: Int, bottom: Int) = RectangleInt(this.left - left, this.top - top, this.width + left + right, this.height + top + bottom)
fun RectangleInt.grow(vertical: Int, horizontal: Int) = grow(vertical, horizontal, vertical, horizontal)
fun RectangleInt.grow(amount: Int) = grow(amount, amount, amount, amount)
fun RectangleInt.shrink(left: Int, top: Int, right: Int, bottom: Int) = grow(-left, -top, -right, -bottom)
fun RectangleInt.shrink(vertical: Int, horizontal: Int) = shrink(vertical, horizontal, vertical, horizontal)
fun RectangleInt.shrink(amount: Int) = shrink(amount, amount, amount, amount)

// サイズ上書き
fun RectangleInt.withWidth(width: Int) = if (width > 0) RectangleInt(left, top, width, height) else RectangleInt(left + this.width + width, top, -width, height)
fun RectangleInt.withHeight(height: Int) = if (height > 0) RectangleInt(left, top, width, height) else RectangleInt(left, top + this.height + height, width, -height)

// 内包
operator fun RectangleInt.contains(point: PointInt) = point.x in left..right && point.y in top..bottom


data class PointInt(val x: Int, val y: Int)

// 移動
fun PointInt.translate(x: Int, y: Int) = PointInt(this.x + x, this.y + y)
fun PointInt.translate(point: PointInt) = translate(point.x, point.y)
operator fun PointInt.plus(point: PointInt) = translate(point.x, point.y)
operator fun PointInt.minus(point: PointInt) = translate(-point.x, -point.y)
