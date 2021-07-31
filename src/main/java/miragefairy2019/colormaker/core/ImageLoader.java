package miragefairy2019.colormaker.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageLoader {

    private ClassLoader classLoader;
    private String modid;

    public ImageLoader(ClassLoader classLoader, String modid) {
        this.classLoader = classLoader;
        this.modid = modid;
    }

    public BufferedImage loadItemImage(String name) throws IOException {
        URL url = classLoader.getResource("assets/" + modid + "/textures/items/" + name + ".png");
        if (url == null) throw new RuntimeException("No such resource: " + name);
        return ImageIO.read(url);
    }

}
