package miragefairy2019.colormaker.core;

import kotlin.Unit;

import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

import mirrg.boron.swing.UtilsComponent.get;

public class PanelSliderField : JPanel {

    private lateinit var slider: JSlider;
    private lateinit var textField: ParsingTextField<Int>;

    private var isInProcessing: Boolean = false;

    public constructor() {

        setLayout(get(GridBagLayout(), { l ->
            l.columnWidths = intArrayOf(300, 50);
            l.rowHeights = intArrayOf(0);
            l.columnWeights = doubleArrayOf(0.0, 0.0);
            l.rowWeights = doubleArrayOf(0.0);
        }));

        add(get(JSlider().also { slider = it }, { c ->
            c.setMajorTickSpacing(8);
            c.setPaintTicks(true);
            c.setMaximum(255);
            c.addChangeListener({ e ->
                if (isInProcessing) return@addChangeListener;
                setValue(c.getValue(), c);
            });
        }), get(GridBagConstraints(), { c ->
            c.insets = Insets(0, 0, 0, 5);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
        }));

        var pattern: Pattern = Pattern.compile("[0-9]+");
        add(get(ParsingTextField({ s ->
            var i: Int = 0;
            if (pattern.matcher(s.trim()).matches()) {
                try {
                    i = Integer.parseInt(s.trim(), 10);
                } catch (e: Exception) {
                    return@ParsingTextField null;
                }
                if (i < slider.getMinimum()) return@ParsingTextField null;
                if (i > slider.getMaximum()) return@ParsingTextField null;
                return@ParsingTextField i;
            } else {
                return@ParsingTextField null;
            }
        }, { v -> "" + v }).also { textField = it }, { c ->
            c.setColumns(5);
            c.listeners.add({ i ->
                if (isInProcessing) return@add;
                setValue(i, c);
                return@add;
            });
        }), get(GridBagConstraints(), { c ->
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
        }));

        //

        value = 0;

    }

    //

    public var _value: Int = 0
    public var value: Int

    public get(): Int {
        return _value;
    }

    public set(value: Int): Unit {
        setValue(value, null);
    }

    //

    public var listeners: ArrayList<IntConsumer> = ArrayList();

    private fun setValue(value: Int, source: Any?): Unit {
        isInProcessing = true;

        this._value = value;
        if (source != slider) slider.setValue(value);
        if (source != textField) textField.setValue(value);
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
