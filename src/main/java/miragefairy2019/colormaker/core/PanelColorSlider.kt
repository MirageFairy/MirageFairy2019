package miragefairy2019.colormaker.core;

import kotlin.Unit;

import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import mirrg.boron.swing.UtilsComponent.get;

class PanelColorSlider : JPanel {

    private lateinit var sliderG: PanelSliderField;
    private lateinit var sliderB: PanelSliderField;
    private lateinit var sliderR: PanelSliderField;
    private lateinit var textField: ParsingTextField<Int>;

    private var isInProcessing = false;

    constructor() {

        setLayout(get(GridBagLayout(), { l ->
            l.columnWidths = intArrayOf(
                    0, 0
            );
            l.rowHeights = intArrayOf(
                    0, 0, 0, 0
            );
            l.columnWeights = doubleArrayOf(
                    1.0, Double.MIN_VALUE
            );
            l.rowWeights = doubleArrayOf(
                    0.0, 0.0, 0.0, 0.0
            );
        }));

        add(get(PanelSliderField().also { sliderR = it }, { c ->
            c.listeners.add(java.util.function.IntConsumer { value ->
                if (isInProcessing) return@IntConsumer;
                setValue(Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), c);
            });
        }), get(GridBagConstraints(), { c ->
            c.insets = Insets(0, 0, 5, 0);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 1;
        }));

        add(get(PanelSliderField().also { sliderG = it }, { c ->
            c.listeners.add(java.util.function.IntConsumer { value ->
                if (isInProcessing) return@IntConsumer;
                setValue(Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), c);
            });
        }), get(GridBagConstraints(), { c ->
            c.insets = Insets(0, 0, 5, 0);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 2;
            c.gridheight = 1;
        }));

        add(get(PanelSliderField().also { sliderB = it }, { c ->
            c.listeners.add(java.util.function.IntConsumer { value ->
                if (isInProcessing) return@IntConsumer;
                setValue(Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), c);
            });
        }), get(GridBagConstraints(), { c ->
            c.insets = Insets(0, 0, 5, 0);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 2;
            c.gridheight = 1;
        }));

        val pattern = Pattern.compile("[0-9a-fA-F]{6}");
        add(get(ParsingTextField({ s ->
            if (pattern.matcher(s.trim()).matches()) {
                return@ParsingTextField Integer.parseInt(s.trim(), 16);
            } else {
                return@ParsingTextField null;
            }
        }, { v -> String.format("%06X", v and 0xffffff) }).also { textField = it }, { c ->
            c.setColumns(10);
            c.listeners.add({ i ->
                if (isInProcessing) return@add Unit;
                setValue(Color(c.value!!), c);
                return@add Unit;
            });
        }), get(GridBagConstraints(), { c ->
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 1;
            c.gridheight = 1;
        }));

        add(get(JToggleButton("Pick"), object : Consumer<JToggleButton> {
            private val timer = Timer(20, { e ->
                try {
                    val location = MouseInfo.getPointerInfo().getLocation();
                    val createScreenCapture = Robot().createScreenCapture(Rectangle(location.x, location.y, 1, 1));
                    setValue(Color(createScreenCapture.getRGB(0, 0)));
                } catch (e2: Exception) {
                    e2.printStackTrace();
                }
            });

            init {
                timer.setRepeats(true);
            }

            override fun accept(c: JToggleButton) {
                c.addActionListener({ e ->
                    if (c.isSelected()) {
                        timer.start();
                    } else {
                        timer.stop();
                    }
                });
                c.addComponentListener(object : ComponentAdapter() {
                    override fun componentHidden(e: ComponentEvent) {
                        timer.stop();
                    }
                });
            }
        }), get(GridBagConstraints(), { c ->
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 3;
            c.gridwidth = 1;
            c.gridheight = 1;
        }));

        //

        setValue(Color.white);

    }

    //

    private lateinit var value: Color;

    fun setValue(value: Color) {
        setValue(value, null);
    }

    fun getValue(): Color {
        return value;
    }

    //

    @JvmField val listeners : ArrayList<Consumer<Color>> = ArrayList();

    private fun setValue(value: Color, source: Any?) {
        isInProcessing = true;

        this.value = value;
        if (source != textField) textField.setValue(value.getRGB());
        if (source != sliderR) sliderR.setValue(value.getRed());
        if (source != sliderG) sliderG.setValue(value.getGreen());
        if (source != sliderB) sliderB.setValue(value.getBlue());
        listeners.forEach({ l ->
            try {
                l.accept(value);
            } catch (e: Exception) {
                e.printStackTrace();
            }
        });

        isInProcessing = false;
    }

}
