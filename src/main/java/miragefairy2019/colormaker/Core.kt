package miragefairy2019.colormaker

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class ColorConstants {
    private val table = mutableMapOf<String, () -> Color>()
    fun addConstant(colorName: String, sColor: () -> Color) = run { table[colorName] = sColor }
    fun getColor(colorIdentifier: ColorIdentifier): Color = table[colorIdentifier.string]?.invoke() ?: colorIdentifier.decode()
}

data class ColorIdentifier(val string: String) {
    fun decode(): Color = Color.decode(string)
}

class ImageLayer(val image: BufferedImage, val colorIdentifier: ColorIdentifier)

class ImageLoader(private val classLoader: ClassLoader, private val modid: String) {
    fun loadItemImage(name: String): BufferedImage = ImageIO.read(classLoader.getResource("assets/$modid/textures/items/$name.png")!!)
}
