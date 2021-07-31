package miragefairy2019.colormaker.core;

import java.awt.image.BufferedImage;

public class ImageLayer {

    public final BufferedImage image;
    public final ColorIdentifier colorIdentifier;

    public ImageLayer(BufferedImage image, ColorIdentifier colorIdentifier) {
        this.image = image;
        this.colorIdentifier = colorIdentifier;
    }

}
