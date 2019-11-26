package miragefairy2019.colormaker;

import java.awt.Color;

import miragefairy2019.colormaker.core.ImageLoader;
import miragefairy2019.mod.ModMirageFairy2019;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple3;

public class MainColorMakerSphere
{

	public static void main(String[] args) throws Exception
	{
		new WindowColorMakerSingle(
			new ImageLoader(ModMirageFairy2019.class.getClassLoader(), ModMirageFairy2019.MODID),
			ImmutableArray.of(
				Tuple3.of("background", "sphere_layer0", new Color(0xFFFFFF)),
				Tuple3.of("plasma", "sphere_layer1", new Color(0xFFFFFF)),
				Tuple3.of("core", "sphere_layer2", new Color(0xFFFFFF)),
				Tuple3.of("highlight", "sphere_layer3", new Color(0xFFFFFF))),
			ImmutableArray.of("core", "highlight", "background", "plasma")).setVisible(true);
	}

}
