package miragefairy2019.libkt

data class RectangleInt(val left: Int, val top: Int, val width: Int, val height: Int) {
    val right get() = left + width
    val bottom get() = top + height
    fun grow(left: Int, top: Int, right: Int, bottom: Int) = RectangleInt(this.left - left, this.top - top, this.width + left + right, this.height + top + bottom)
    fun grow(vertical: Int, horizontal: Int) = grow(vertical, horizontal, vertical, horizontal)
    fun grow(amount: Int) = grow(amount, amount, amount, amount)
    fun shrink(left: Int, top: Int, right: Int, bottom: Int) = grow(-left, -top, -right, -bottom)
    fun shrink(vertical: Int, horizontal: Int) = shrink(vertical, horizontal, vertical, horizontal)
    fun shrink(amount: Int) = shrink(amount, amount, amount, amount)
    fun withWidth(width: Int) = if (width > 0) RectangleInt(left, top, width, height) else RectangleInt(left + this.width + width, top, -width, height)
    fun withHeight(height: Int) = if (height > 0) RectangleInt(left, top, width, height) else RectangleInt(left, top + this.height + height, width, -height)
}
