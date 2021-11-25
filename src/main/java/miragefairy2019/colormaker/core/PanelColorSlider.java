package miragefairy2019.colormaker.core;

import kotlin.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static mirrg.boron.swing.UtilsComponent.get;

public class PanelColorSlider extends JPanel {

    private PanelSliderField sliderG;
    private PanelSliderField sliderB;
    private PanelSliderField sliderR;
    private ParsingTextField<Integer> textField;

    private boolean isInProcessing = false;

    public PanelColorSlider() {

        setLayout(get(new GridBagLayout(), l -> {
            l.columnWidths = new int[]{
                    0, 0
            };
            l.rowHeights = new int[]{
                    0, 0, 0, 0
            };
            l.columnWeights = new double[]{
                    1.0, Double.MIN_VALUE
            };
            l.rowWeights = new double[]{
                    0.0, 0.0, 0.0, 0.0
            };
        }));

        add(get(sliderR = new PanelSliderField(), c -> {
            c.listeners.add(value -> {
                if (isInProcessing) return;
                setValue(new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), c);
            });
        }), get(new GridBagConstraints(), c -> {
            c.insets = new Insets(0, 0, 5, 0);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 1;
        }));

        add(get(sliderG = new PanelSliderField(), c -> {
            c.listeners.add(value -> {
                if (isInProcessing) return;
                setValue(new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), c);
            });
        }), get(new GridBagConstraints(), c -> {
            c.insets = new Insets(0, 0, 5, 0);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 2;
            c.gridheight = 1;
        }));

        add(get(sliderB = new PanelSliderField(), c -> {
            c.listeners.add(value -> {
                if (isInProcessing) return;
                setValue(new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), c);
            });
        }), get(new GridBagConstraints(), c -> {
            c.insets = new Insets(0, 0, 5, 0);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 2;
            c.gridheight = 1;
        }));

        Pattern pattern = Pattern.compile("[0-9a-fA-F]{6}");
        add(get(textField = new ParsingTextField<>(s -> {
            if (pattern.matcher(s.trim()).matches()) {
                return Integer.parseInt(s.trim(), 16);
            } else {
                return null;
            }
        }, v -> String.format("%06X", v & 0xffffff)), c -> {
            c.setColumns(10);
            c.getListeners().add(i -> {
                if (isInProcessing) return Unit.INSTANCE;
                setValue(new Color(c.getValue()), c);
                return Unit.INSTANCE;
            });
        }), get(new GridBagConstraints(), c -> {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 1;
            c.gridheight = 1;
        }));

        add(get(new JToggleButton("Pick"), new Consumer<JToggleButton>() {
            private Timer timer = new Timer(20, e -> {
                try {
                    Point location = MouseInfo.getPointerInfo().getLocation();
                    BufferedImage createScreenCapture = new Robot().createScreenCapture(new Rectangle(location.x, location.y, 1, 1));
                    setValue(new Color(createScreenCapture.getRGB(0, 0)));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            });

            {
                timer.setRepeats(true);
            }

            @Override
            public void accept(JToggleButton c) {
                c.addActionListener(e -> {
                    if (c.isSelected()) {
                        timer.start();
                    } else {
                        timer.stop();
                    }
                });
                c.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentHidden(ComponentEvent e) {
                        timer.stop();
                    }
                });
            }
        }), get(new GridBagConstraints(), c -> {
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

    private Color value;

    public void setValue(Color value) {
        setValue(value, null);
    }

    public Color getValue() {
        return value;
    }

    //

    public ArrayList<Consumer<Color>> listeners = new ArrayList<>();

    private void setValue(Color value, Object source) {
        isInProcessing = true;

        this.value = value;
        if (source != textField) textField.setValue(value.getRGB());
        if (source != sliderR) sliderR.setValue(value.getRed());
        if (source != sliderG) sliderG.setValue(value.getGreen());
        if (source != sliderB) sliderB.setValue(value.getBlue());
        listeners.forEach(l -> {
            try {
                l.accept(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        isInProcessing = false;
    }

}
