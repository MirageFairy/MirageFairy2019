package miragefairy2019.colormaker

import mirrg.boron.util.struct.ImmutableArray
import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JLabel

class LabelImage : JLabel() {
    var colorConstants = ColorConstants()
    var backgroundColor: Color = Color.black

    fun setImage(arrayList: ImmutableArray<ImageLayer>) {
        icon = ImageIcon(createImage(arrayList))
    }

    private fun createImage(imageLayers: ImmutableArray<ImageLayer>): BufferedImage {
        val image = BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB)
        repeat(64) { x ->
            repeat(64) { y ->

                // ラベルの背景色で初期化
                var r1 = backgroundColor.red
                var g1 = backgroundColor.green
                var b1 = backgroundColor.blue

                imageLayers.forEach { imageLayer ->

                    // 乗算する色
                    val colorMul = colorConstants.getColor(imageLayer.colorIdentifier)

                    // 画像の色
                    val argbOver = imageLayer.image.getRGB(x / 4, y / 4)
                    val a2 = (argbOver shr 24) and 0xff
                    var r2 = (argbOver shr 16) and 0xff
                    var g2 = (argbOver shr 8) and 0xff
                    var b2 = (argbOver shr 0) and 0xff

                    // 画像の色を乗算する色で更新
                    r2 = r2 * colorMul.red / 255
                    g2 = g2 * colorMul.green / 255
                    b2 = b2 * colorMul.blue / 255

                    // 現在の色を更新
                    r1 = (r1 * (255 - a2) + r2 * a2) / 255
                    g1 = (g1 * (255 - a2) + g2 * a2) / 255
                    b1 = (b1 * (255 - a2) + b2 * a2) / 255

                }

                // 色セット
                image.setRGB(x, y, ((r1 and 0xff) shl 16) or ((g1 and 0xff) shl 8) or ((b1 and 0xff) shl 0))

            }
        }
        return image
    }
}
