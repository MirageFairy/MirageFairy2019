package miragefairy2019.colormaker.core

import java.awt.Color
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.MouseInfo
import java.awt.Rectangle
import java.awt.Robot
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.util.function.Consumer
import java.util.regex.Pattern
import javax.swing.JPanel
import javax.swing.JToggleButton
import javax.swing.Timer

class PanelColorSlider : JPanel {

    private lateinit var sliderG: PanelSliderField
    private lateinit var sliderB: PanelSliderField
    private lateinit var sliderR: PanelSliderField
    private lateinit var textField: ParsingTextField<Int>

    private var isInProcessing = false

    constructor() {

        layout = GridBagLayout().also { l ->
            l.columnWidths = intArrayOf(
                0, 0
            )
            l.rowHeights = intArrayOf(
                0, 0, 0, 0
            )
            l.columnWeights = doubleArrayOf(
                1.0, Double.MIN_VALUE
            )
            l.rowWeights = doubleArrayOf(
                0.0, 0.0, 0.0, 0.0
            )
        }

        add(PanelSliderField().also { sliderR = it }.also { c ->
            c.listeners.add(java.util.function.IntConsumer {
                if (isInProcessing) return@IntConsumer
                setValue(Color(sliderR.value, sliderG.value, sliderB.value), c)
            })
        }, GridBagConstraints().also { c ->
            c.insets = Insets(0, 0, 5, 0)
            c.fill = GridBagConstraints.BOTH
            c.gridx = 0
            c.gridy = 0
            c.gridwidth = 2
            c.gridheight = 1
        })

        add(PanelSliderField().also { sliderG = it }.also { c ->
            c.listeners.add(java.util.function.IntConsumer {
                if (isInProcessing) return@IntConsumer
                setValue(Color(sliderR.value, sliderG.value, sliderB.value), c)
            })
        }, GridBagConstraints().also { c ->
            c.insets = Insets(0, 0, 5, 0)
            c.fill = GridBagConstraints.BOTH
            c.gridx = 0
            c.gridy = 1
            c.gridwidth = 2
            c.gridheight = 1
        })

        add(PanelSliderField().also { sliderB = it }.also { c ->
            c.listeners.add(java.util.function.IntConsumer {
                if (isInProcessing) return@IntConsumer
                setValue(Color(sliderR.value, sliderG.value, sliderB.value), c)
            })
        }, GridBagConstraints().also { c ->
            c.insets = Insets(0, 0, 5, 0)
            c.fill = GridBagConstraints.BOTH
            c.gridx = 0
            c.gridy = 2
            c.gridwidth = 2
            c.gridheight = 1
        })

        val pattern = Pattern.compile("[0-9a-fA-F]{6}")
        add(ParsingTextField({ s ->
            if (pattern.matcher(s.trim()).matches()) {
                return@ParsingTextField Integer.parseInt(s.trim(), 16)
            } else {
                return@ParsingTextField null
            }
        }, { v -> String.format("%06X", v and 0xffffff) }).also { textField = it }.also { c ->
            c.columns = 10
            c.listeners.add {
                if (isInProcessing) return@add
                setValue(Color(c.value!!), c)
            }
        }, GridBagConstraints().also { c ->
            c.fill = GridBagConstraints.HORIZONTAL
            c.gridx = 0
            c.gridy = 3
            c.gridwidth = 1
            c.gridheight = 1
        })

        add(JToggleButton("Pick").also { object : (JToggleButton) -> Unit {
            private val timer = Timer(20) {
                try {
                    val location = MouseInfo.getPointerInfo().location
                    val createScreenCapture = Robot().createScreenCapture(Rectangle(location.x, location.y, 1, 1))
                    setValue(Color(createScreenCapture.getRGB(0, 0)))
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }

            init {
                timer.isRepeats = true
            }

            override fun invoke(c: JToggleButton) {
                c.addActionListener {
                    if (c.isSelected) {
                        timer.start()
                    } else {
                        timer.stop()
                    }
                }
                c.addComponentListener(object : ComponentAdapter() {
                    override fun componentHidden(e: ComponentEvent) {
                        timer.stop()
                    }
                })
            }
        }(it) }, GridBagConstraints().also { c ->
            c.fill = GridBagConstraints.HORIZONTAL
            c.gridx = 1
            c.gridy = 3
            c.gridwidth = 1
            c.gridheight = 1
        })

        //

        setValue(Color.white)

    }

    //

    private lateinit var value: Color

    fun setValue(value: Color) {
        setValue(value, null)
    }

    fun getValue(): Color {
        return value
    }

    //

    @JvmField
    val listeners: ArrayList<Consumer<Color>> = ArrayList()

    private fun setValue(value: Color, source: Any?) {
        isInProcessing = true

        this.value = value
        if (source != textField) textField.setValue(value.rgb)
        if (source != sliderR) sliderR.value = value.red
        if (source != sliderG) sliderG.value = value.green
        if (source != sliderB) sliderB.value = value.blue
        listeners.forEach { l ->
            try {
                l.accept(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        isInProcessing = false
    }

}
