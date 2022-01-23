package miragefairy2019.colormaker;

import kotlin.Unit;
import miragefairy2019.colormaker.core.ColorConstants;
import miragefairy2019.colormaker.core.ImageLayer;
import miragefairy2019.colormaker.core.LabelImage;
import miragefairy2019.colormaker.core.PanelColorSlider;
import mirrg.boron.util.struct.ImmutableArray;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.stream.Stream;

import static mirrg.boron.swing.UtilsComponent.createPanelTitledBorder;
import static mirrg.boron.swing.UtilsComponent.createSplitPaneHorizontal;
import static mirrg.boron.swing.UtilsComponent.get;

public class WindowColorMaker extends JFrame {

    private JPanel panelImage;
    private LabelImage labelMirageFairy;
    private LabelImage labelMirageWisp;
    private LabelImage labelMagicSphere;
    private PanelColorSlider panelColorSliderBG;
    private PanelColorSlider panelColorSlider0;
    private PanelColorSlider panelColorSlider1;
    private PanelColorSlider panelColorSlider2;
    private PanelColorSlider panelColorSlider3;
    private JTextField textFieldColors;

    private boolean isInProcessing = false;
    private boolean leadyUpdate = false;

    public WindowColorMaker() {

        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        add(createSplitPaneHorizontal(

            // 左ペイン
            get(panelImage = new JPanel(), c2 -> {
                c2.setLayout(get(new GridBagLayout(), l -> {
                    l.columnWidths = new int[]{100};
                    l.rowHeights = new int[]{0, 0, 0};
                    l.columnWeights = new double[]{0.0};
                    l.rowWeights = new double[]{0.0, 0.0, 0.0};
                }));

                // 画像3枚
                {
                    ColorConstants colorConstants = new ColorConstants();
                    colorConstants.addConstant("@skin", () -> value[0]);
                    colorConstants.addConstant("@darker", () -> value[1]);
                    colorConstants.addConstant("@brighter", () -> value[2]);
                    colorConstants.addConstant("@hair", () -> value[3]);

                    c2.add(get(labelMirageFairy = new LabelImage(), c -> {
                        c.setPreferredSize(new Dimension(64, 64));
                        c.setColorConstants(colorConstants);
                    }), get(new GridBagConstraints(), c -> {
                        c.insets = new Insets(0, 0, 5, 0);
                        c.gridx = 0;
                        c.gridy = 0;
                    }));
                    c2.add(get(labelMirageWisp = new LabelImage(), c -> {
                        c.setPreferredSize(new Dimension(64, 64));
                        c.setColorConstants(colorConstants);
                    }), get(new GridBagConstraints(), c -> {
                        c.insets = new Insets(0, 0, 5, 0);
                        c.gridx = 0;
                        c.gridy = 1;
                    }));
                    c2.add(get(labelMagicSphere = new LabelImage(), c -> {
                        c.setPreferredSize(new Dimension(64, 64));
                        c.setColorConstants(colorConstants);
                    }), get(new GridBagConstraints(), c -> {
                        c.gridx = 0;
                        c.gridy = 2;
                    }));

                }

            }),

            // 右ペイン
            get(new JPanel(), c2 -> {
                c2.setBorder(new EmptyBorder(4, 4, 4, 4));
                c2.setLayout(get(new GridBagLayout(), l -> {
                    l.columnWidths = new int[]{0};
                    l.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
                    l.columnWeights = new double[]{1.0};
                    l.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                }));

                // 右側スライダーコンポーネント
                c2.add(createPanelTitledBorder("Background", get(
                    panelColorSliderBG = new PanelColorSlider(), c -> {
                        c.getListeners().add(color -> {
                            if (isInProcessing) return Unit.INSTANCE;
                            setBackgroundColor(color, c);
                            return Unit.INSTANCE;
                        });
                    })), get(new GridBagConstraints(), c -> {
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.insets = new Insets(0, 0, 5, 0);
                    c.gridx = 0;
                    c.gridy = 0;
                }));
                c2.add(createPanelTitledBorder("Skin", get(
                    panelColorSlider0 = new PanelColorSlider(), c -> {
                        c.getListeners().add(color -> {
                            if (isInProcessing) return Unit.INSTANCE;
                            setValue(getColor(), c);
                            return Unit.INSTANCE;
                        });
                    })), get(new GridBagConstraints(), c -> {
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.insets = new Insets(0, 0, 5, 0);
                    c.gridx = 0;
                    c.gridy = 1;
                }));
                c2.add(createPanelTitledBorder("Darker", get(
                    panelColorSlider1 = new PanelColorSlider(), c -> {
                        c.getListeners().add(color -> {
                            if (isInProcessing) return Unit.INSTANCE;
                            setValue(getColor(), c);
                            return Unit.INSTANCE;
                        });
                    })), get(new GridBagConstraints(), c -> {
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.insets = new Insets(0, 0, 5, 0);
                    c.gridx = 0;
                    c.gridy = 2;
                }));
                c2.add(createPanelTitledBorder("Brighter", get(
                    panelColorSlider2 = new PanelColorSlider(), c -> {
                        c.getListeners().add(color -> {
                            if (isInProcessing) return Unit.INSTANCE;
                            setValue(getColor(), c);
                            return Unit.INSTANCE;
                        });
                    })), get(new GridBagConstraints(), c -> {
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.insets = new Insets(0, 0, 5, 0);
                    c.gridx = 0;
                    c.gridy = 3;
                }));
                c2.add(createPanelTitledBorder("Hair", get(
                    panelColorSlider3 = new PanelColorSlider(), c -> {
                        c.getListeners().add(color -> {
                            if (isInProcessing) return Unit.INSTANCE;
                            setValue(getColor(), c);
                            return Unit.INSTANCE;
                        });
                    })), get(new GridBagConstraints(), c -> {
                    c.insets = new Insets(0, 0, 5, 0);
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = 0;
                    c.gridy = 4;
                }));

                // 右側色構文
                c2.add(get(textFieldColors = new JTextField(), c -> {
                    c.setColumns(10);
                    c.addActionListener(e -> {
                        if (isInProcessing) return;
                        setValue(c.getText(), c);
                    });
                    c.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            if (isInProcessing) return;
                            setValue(c.getText(), c);
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            if (isInProcessing) return;
                            setValue(c.getText(), c);
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            if (isInProcessing) return;
                            setValue(c.getText(), c);
                        }
                    });
                }), get(new GridBagConstraints(), c -> {
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = 0;
                    c.gridy = 5;
                }));

            })

        ));

        //

        setBackgroundColor(new Color(139, 139, 139));
        setValue(new Color[]{
            Color.white,
            Color.white,
            Color.white,
            Color.white,
        });

        leadyUpdate = true;
        updateImage();

        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    private Color[] getColor() {
        return new Color[]{
            panelColorSlider0.getValue(),
            panelColorSlider1.getValue(),
            panelColorSlider2.getValue(),
            panelColorSlider3.getValue(),
        };
    }

    //

    private ImmutableArray<ImmutableArray<ImageLayer>> images = MainColorMaker.loadImages();

    public void updateImage() {
        if (!leadyUpdate) return;
        labelMirageFairy.setImage(images.get(0));
        labelMirageWisp.setImage(images.get(1));
        labelMagicSphere.setImage(images.get(2));
    }

    //

    private Color[] value;

    private void setValue(String text, Object source) {
        Color[] colors;
        try {
            colors = Stream.of(text.split(","))
                .map(s -> Color.decode(s.trim()))
                .toArray(Color[]::new);
        } catch (RuntimeException e) {
            return;
        }
        if (colors.length != 4) return;

        setValue(colors, source);
    }

    private void setValue(Color[] value) {
        setValue(value, null);
    }

    private void setValue(Color[] value, Object source) {
        isInProcessing = true;

        this.value = value;
        if (source != panelColorSlider0) panelColorSlider0.setValue(value[0]);
        if (source != panelColorSlider1) panelColorSlider1.setValue(value[1]);
        if (source != panelColorSlider2) panelColorSlider2.setValue(value[2]);
        if (source != panelColorSlider3) panelColorSlider3.setValue(value[3]);
        if (source != textFieldColors) textFieldColors.setText(String.format("0x%06X, 0x%06X, 0x%06X, 0x%06X",
            value[0].getRGB() & 0xffffff,
            value[1].getRGB() & 0xffffff,
            value[2].getRGB() & 0xffffff,
            value[3].getRGB() & 0xffffff));
        updateImage();

        isInProcessing = false;
    }

    //

    private void setBackgroundColor(Color backgroundColor) {
        setBackgroundColor(backgroundColor, null);
    }

    private void setBackgroundColor(Color backgroundColor, Object source) {
        isInProcessing = true;

        panelImage.setBackground(backgroundColor);
        labelMirageFairy.setBackgroundColor(backgroundColor);
        labelMirageWisp.setBackgroundColor(backgroundColor);
        labelMagicSphere.setBackgroundColor(backgroundColor);
        if (source != panelColorSliderBG) panelColorSliderBG.setValue(backgroundColor);
        updateImage();

        isInProcessing = false;
    }

}
