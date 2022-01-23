package miragefairy2019.colormaker;

import kotlin.Unit;
import miragefairy2019.colormaker.core.ColorConstants;
import miragefairy2019.colormaker.core.ColorIdentifier;
import miragefairy2019.colormaker.core.ImageLayer;
import miragefairy2019.colormaker.core.ImageLoader;
import miragefairy2019.colormaker.core.LabelImage;
import miragefairy2019.colormaker.core.PanelColorSlider;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple3;
import mirrg.boron.util.suppliterator.ISuppliterator;

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
import java.io.IOException;

import static mirrg.boron.swing.UtilsComponent.createPanelTitledBorder;
import static mirrg.boron.swing.UtilsComponent.createSplitPaneHorizontal;
import static mirrg.boron.swing.UtilsComponent.get;

public class WindowColorMakerSingle extends JFrame {

    private final int length;
    private final ImmutableArray<Tuple3<String, String, Color>> layerSettings;
    private final ImmutableArray<ImageLayer> images;
    private final ImmutableArray<String> schema;

    private JPanel panelImage;
    private LabelImage labelImage;
    private PanelColorSlider panelColorSliderBG;
    private PanelColorSlider[] panelColorSliders;
    private JTextField textFieldColors;

    private boolean isInProcessing = false;
    private boolean readyUpdate = false;

    public WindowColorMakerSingle(ImageLoader imageLoader, ImmutableArray<Tuple3<String, String, Color>> layerSettings, ImmutableArray<String> schema) {
        this.length = layerSettings.length();
        this.layerSettings = layerSettings;
        this.images = ISuppliterator.range(length)
            .map(i -> {
                try {
                    return new ImageLayer(imageLoader.loadItemImage(layerSettings.get(i).y), new ColorIdentifier("@" + layerSettings.get(i).x));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .toImmutableArray();
        this.schema = schema;

        panelColorSliders = new PanelColorSlider[length];

        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        add(createSplitPaneHorizontal(

            // 左ペイン
            get(panelImage = new JPanel(), c2 -> {
                c2.setLayout(get(new GridBagLayout(), l -> {
                    //l.columnWidths = new int[] { 100 };
                    //l.rowHeights = new int[] { 100 };
                    //l.columnWeights = new double[] { 0.0 };
                    //l.rowWeights = new double[] { 0.0 };
                }));

                // 画像
                {
                    ColorConstants colorConstants = new ColorConstants();
                    for (int i = 0; i < length; i++) {
                        int i2 = i;
                        colorConstants.addConstant("@" + layerSettings.get(i).x, () -> value[i2]);
                    }

                    c2.add(get(labelImage = new LabelImage(), c -> {
                        c.setPreferredSize(new Dimension(64, 64));
                        c.setColorConstants(colorConstants);
                    }), get(new GridBagConstraints(), c -> {
                        c.insets = new Insets(0, 0, 5, 0);
                        c.gridx = 0;
                        c.gridy = 0;
                    }));

                }

            }),

            // 右ペイン
            get(new JPanel(), c2 -> {
                c2.setBorder(new EmptyBorder(4, 4, 4, 4));
                c2.setLayout(get(new GridBagLayout(), l -> {
                    //l.columnWidths = new int[] { 0 };
                    //l.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
                    //l.columnWeights = new double[] { 1.0 };
                    //l.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
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
                for (int i = 0; i < length; i++) {
                    int i2 = i;
                    c2.add(createPanelTitledBorder(layerSettings.get(i).x, get(
                        panelColorSliders[i] = new PanelColorSlider(), c -> {
                            c.getListeners().add(color -> {
                                if (isInProcessing) return Unit.INSTANCE;
                                setValue(getColor(), c);
                                return Unit.INSTANCE;
                            });
                        })), get(new GridBagConstraints(), c -> {
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.insets = new Insets(0, 0, 5, 0);
                        c.gridx = 0;
                        c.gridy = i2 + 1;
                    }));
                }

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
                    c.gridy = length + 1;
                }));

            })

        ));

        //

        setBackgroundColor(Color.gray);
        setValue(ISuppliterator.range(length)
            .map(i -> layerSettings.get(i).z)
            .toArray(Color[]::new));

        readyUpdate = true;
        updateImage();

        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    private Color[] getColor() {
        return ISuppliterator.range(length)
            .map(i -> panelColorSliders[i].getValue())
            .toArray(Color[]::new);
    }

    //

    public void updateImage() {
        if (!readyUpdate) return;
        labelImage.setImage(images);
    }

    //

    private Color[] value;

    private void setValue(String text, Object source) {
        Color[] colors;
        try {

            Color[] colors2 = getColor().clone();

            Color[] colors1 = ISuppliterator.of(text.split(","))
                .map(s -> Color.decode(s.trim()))
                .toArray(Color[]::new);

            ISuppliterator.ofObjArray(colors1)
                .forEach((c, i) -> {

                    String name = schema.get(i);

                    int index = ISuppliterator.ofIterable(layerSettings)
                        .findWithIndex(s -> s.x.equals(name)).get().index;

                    colors2[index] = c;

                });

            colors = colors2;

        } catch (RuntimeException e) {
            return;
        }
        if (colors.length != length) return;

        setValue(colors, source);
    }

    private void setValue(Color[] value) {
        setValue(value, null);
    }

    private void setValue(Color[] value, Object source) {
        isInProcessing = true;

        this.value = value;
        for (int i = 0; i < panelColorSliders.length; i++) {
            if (source != panelColorSliders[i]) panelColorSliders[i].setValue(value[i]);
        }
        if (source != textFieldColors) {
            textFieldColors.setText(ISuppliterator.ofIterable(schema)
                .map(name -> {

                    int index = ISuppliterator.ofIterable(layerSettings)
                        .findWithIndex(s -> s.x.equals(name)).get().index;

                    Color color = panelColorSliders[index].getValue();

                    return String.format("0x%06X", color.getRGB() & 0xffffff);
                })
                .join(", "));
        }
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
        labelImage.setBackgroundColor(backgroundColor);
        if (source != panelColorSliderBG) panelColorSliderBG.setValue(backgroundColor);
        updateImage();

        isInProcessing = false;
    }

}
