package miragefairy2019.colormaker

import mirrg.boron.swing.UtilsComponent
import mirrg.boron.swing.UtilsComponent.createPanelTitledBorder
import mirrg.kotlin.hydrogen.formatAs
import mirrg.kotlin.hydrogen.join
import java.awt.Color
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.Insets
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

private fun String.decodeColor() = try {
    Color.decode(this)
} catch (_: NumberFormatException) {
    null
}

private fun String.decodeColorsOrNull(): List<Color>? = this.split(",").map { it.trim().decodeColor() ?: return null }

private fun List<Color>.encode() = this.map { it.rgb and 0xffffff formatAs "0x%06X" }.join(", ")

private class ExclusiveController {
    private var isInProcessing = false
    operator fun invoke(block: () -> Unit) {
        if (isInProcessing) return
        isInProcessing = true
        try {
            block()
        } finally {
            isInProcessing = false
        }
    }
}


class LayeredImageSetting(val layerSettings: List<LayerSetting>)

fun layeredImageSettingOf(vararg layerSettings: LayerSetting) = LayeredImageSetting(layerSettings.toList())

class LayerSetting(val imagePath: String, val colorExpression: ColorExpression)

class WindowColorMaker(
    private val imageLoader: (String) -> BufferedImage,
    private val layeredImageSettings: List<LayeredImageSetting>,
    private val sliderNames: List<String>
) : JFrame() {

    // Utility
    private val exclusiveController = ExclusiveController()

    // 画像バッファ
    private val imageLayerListList = layeredImageSettings.map { layeredImageSetting ->
        layeredImageSetting.layerSettings.map { layerSetting ->
            Layer(imageLoader(layerSetting.imagePath), layerSetting.colorExpression)
        }
    }

    // Components
    private val panelImage: JPanel
    private val labelImages = mutableListOf<LayeredImage>()
    private val panelColorSliderBG: PanelColorSlider
    private val panelColorSliders = mutableListOf<PanelColorSlider>()
    private val textFieldColors: JTextField


    // Value

    private fun setBackgroundColor(backgroundColor: Color, source: Any?) {
        exclusiveController {
            if (source !== panelImage) panelImage.background = backgroundColor
            labelImages.forEach { labelImage ->
                if (source !== labelImage) labelImage.backgroundColor = backgroundColor
            }
            if (source !== panelColorSliderBG) panelColorSliderBG.setValue(backgroundColor)
            updateImage()
        }
    }

    private fun getColor(index: Int) = panelColorSliders[index].getValue()

    private fun setColors(colors: List<Color>, source: Any?) {
        exclusiveController {
            panelColorSliders.indices.forEach { i ->
                if (source !== panelColorSliders[i]) panelColorSliders[i].setValue(colors[i])
            }
            if (source !== textFieldColors) textFieldColors.text = colors.encode()
            updateImage()
        }
    }

    private var canUpdateImage = false

    private fun updateImage() {
        if (!canUpdateImage) return
        labelImages.forEachIndexed { i, it ->
            it.render(imageLayerListList[i])
        }
    }


    init {

        contentPane.layout = GridLayout(0, 1, 0, 0)

        add(UtilsComponent.createSplitPaneHorizontal(

            // 左ペイン
            JPanel().also { leftPane ->
                panelImage = leftPane

                leftPane.layout = GridBagLayout().also {
                    it.columnWidths = intArrayOf(100)
                    it.rowHeights = intArrayOf(0, 0, 0)
                    it.columnWeights = doubleArrayOf(0.0)
                    it.rowWeights = doubleArrayOf(0.0, 0.0, 0.0)
                }

                // 画像
                val colorEvaluator = ColorEvaluator().also {
                    sliderNames.forEachIndexed { i, name ->
                        it.registerVariable("@$name") { getColor(i) }
                    }
                }
                repeat(layeredImageSettings.size) { i ->
                    leftPane.add(LayeredImage().also { labelImage ->
                        labelImages += labelImage
                        labelImage.preferredSize = Dimension(64, 64)
                        labelImage.colorEvaluator = colorEvaluator
                    }, GridBagConstraints().also {
                        it.insets = Insets(0, 0, 5, 0)
                        it.gridx = 0
                        it.gridy = i
                    })
                }

            },

            // 右ペイン
            JPanel().also { rightPane ->

                rightPane.border = EmptyBorder(4, 4, 4, 4)
                rightPane.layout = GridBagLayout().also {
                    it.columnWidths = intArrayOf(0)
                    it.rowHeights = intArrayOf(0, 0, 0, 0, 0, 0)
                    it.columnWeights = doubleArrayOf(1.0)
                    it.rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                }

                // 右側スライダーコンポーネント
                rightPane.add(createPanelTitledBorder("Background",
                    PanelColorSlider().also { c ->
                        panelColorSliderBG = c
                        c.listeners += { color -> setBackgroundColor(color, c) }
                    }), GridBagConstraints().also {
                    it.fill = GridBagConstraints.HORIZONTAL
                    it.insets = Insets(0, 0, 5, 0)
                    it.gridx = 0
                    it.gridy = 0
                })
                sliderNames.forEachIndexed { i, name ->
                    rightPane.add(createPanelTitledBorder(name,
                        PanelColorSlider().also { c ->
                            panelColorSliders += c
                            c.listeners += { setColors(panelColorSliders.map { it.getValue() }, c) }
                        }), GridBagConstraints().also {
                        it.fill = GridBagConstraints.HORIZONTAL
                        it.insets = Insets(0, 0, 5, 0)
                        it.gridx = 0
                        it.gridy = 1 + i
                    })
                }

                // 右側色構文
                rightPane.add(JTextField().also { c ->
                    textFieldColors = c
                    c.columns = 10
                    fun setSliderValue() {
                        val colors = c.text.decodeColorsOrNull() ?: return
                        if (colors.size != sliderNames.size) return
                        setColors(colors, c)
                    }
                    c.addActionListener { setSliderValue() }
                    c.document.addDocumentListener(object : DocumentListener {
                        override fun removeUpdate(e: DocumentEvent) = setSliderValue()
                        override fun insertUpdate(e: DocumentEvent) = setSliderValue()
                        override fun changedUpdate(e: DocumentEvent) = setSliderValue()
                    })
                }, GridBagConstraints().also {
                    it.fill = GridBagConstraints.HORIZONTAL
                    it.gridx = 0
                    it.gridy = sliderNames.size + 1
                })

            }

        ))


        setBackgroundColor(Color(139, 139, 139), null)
        setColors(sliderNames.map { Color.white!! }, null)

        canUpdateImage = true
        updateImage()

        isLocationByPlatform = true
        defaultCloseOperation = DISPOSE_ON_CLOSE
        pack()
    }
}
