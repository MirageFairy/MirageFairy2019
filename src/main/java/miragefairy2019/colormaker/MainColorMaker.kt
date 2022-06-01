package miragefairy2019.colormaker

import miragefairy2019.mod.ModMirageFairy2019
import javax.imageio.ImageIO

private fun loadImage(name: String) = ImageIO.read(ModMirageFairy2019::class.java.classLoader.getResource("assets/${ModMirageFairy2019.MODID}/textures/items/$name.png")!!)

object MainColorMaker {
    @JvmStatic
    fun main(args: Array<String>) {
        WindowColorMaker(
            { loadImage(it) },
            listOf(
                layeredImageSettingOf(
                    LayerSetting("fairy_layer0", ColorExpression("@skin")),
                    LayerSetting("fairy_layer1", ColorExpression("#00BE00")),
                    LayerSetting("fairy_layer2", ColorExpression("@darker")),
                    LayerSetting("fairy_layer3", ColorExpression("@brighter")),
                    LayerSetting("fairy_layer4", ColorExpression("@hair"))
                ),
                layeredImageSettingOf(
                    LayerSetting("mirage_wisp_layer0", ColorExpression("@darker")),
                    LayerSetting("mirage_wisp_layer1", ColorExpression("@skin")),
                    LayerSetting("mirage_wisp_layer2", ColorExpression("@brighter")),
                    LayerSetting("mirage_wisp_layer3", ColorExpression("@hair"))
                ),
                layeredImageSettingOf(
                    LayerSetting("sphere_layer0", ColorExpression("@darker")),
                    LayerSetting("sphere_layer1", ColorExpression("@hair")),
                    LayerSetting("sphere_layer2", ColorExpression("@skin")),
                    LayerSetting("sphere_layer3", ColorExpression("@brighter"))
                )
            ),
            listOf("skin", "darker", "brighter", "hair")
        ).isVisible = true
    }
}

object MainColorMakerFairy {
    @JvmStatic
    fun main(args: Array<String>) {
        WindowColorMaker(
            { loadImage(it) },
            listOf(
                layeredImageSettingOf(
                    LayerSetting("fairy_layer0", ColorExpression("@skin")),
                    LayerSetting("fairy_layer1", ColorExpression("@dress")),
                    LayerSetting("fairy_layer2", ColorExpression("@dark")),
                    LayerSetting("fairy_layer3", ColorExpression("@bright")),
                    LayerSetting("fairy_layer4", ColorExpression("@hair"))
                )
            ),
            listOf("dress", "skin", "bright", "dark", "hair")
        ).isVisible = true
    }
}

object MainColorMakerSphere {
    @JvmStatic
    fun main(args: Array<String>) {
        WindowColorMaker(
            { loadImage(it) },
            listOf(
                layeredImageSettingOf(
                    LayerSetting("sphere_layer0", ColorExpression("@background")),
                    LayerSetting("sphere_layer1", ColorExpression("@plasma")),
                    LayerSetting("sphere_layer2", ColorExpression("@core")),
                    LayerSetting("sphere_layer3", ColorExpression("@highlight"))
                )
            ),
            listOf("core", "highlight", "background", "plasma")
        ).isVisible = true
    }
}
