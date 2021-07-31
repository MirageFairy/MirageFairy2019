package miragefairy2019.colormaker;

import miragefairy2019.colormaker.core.ImageLoader;
import miragefairy2019.mod.ModMirageFairy2019;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple3;

import java.awt.*;

public class MainColorMakerFairy {

    public static void main(String[] args) throws Exception {
        new WindowColorMakerSingle(
                new ImageLoader(ModMirageFairy2019.class.getClassLoader(), ModMirageFairy2019.MODID),
                ImmutableArray.of(
                        Tuple3.of("skin", "fairy_layer0", new Color(0xFFFFFF)),
                        Tuple3.of("dress", "fairy_layer1", new Color(0xFFFFFF)),
                        Tuple3.of("dark", "fairy_layer2", new Color(0xFFFFFF)),
                        Tuple3.of("bright", "fairy_layer3", new Color(0xFFFFFF)),
                        Tuple3.of("hair", "fairy_layer4", new Color(0xFFFFFF))),
                ImmutableArray.of("skin", "bright", "dark", "hair")).setVisible(true);
    }

}
