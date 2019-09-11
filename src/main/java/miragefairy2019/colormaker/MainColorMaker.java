package miragefairy2019.colormaker;

import java.io.IOException;

import miragefairy2019.colormaker.core.ColorIdentifier;
import miragefairy2019.colormaker.core.ImageLayer;
import miragefairy2019.colormaker.core.ImageLoader;
import miragefairy2019.mod.ModMirageFairy2019;
import mirrg.boron.util.struct.ImmutableArray;

public class MainColorMaker
{

	public static void main(String[] args) throws Exception
	{
		new WindowColorMaker().setVisible(true);
	}

	public static ImmutableArray<ImmutableArray<ImageLayer>> loadImages() throws IOException
	{
		ImageLoader imageLoader = new ImageLoader(ModMirageFairy2019.class.getClassLoader(), ModMirageFairy2019.MODID);
		return ImmutableArray.of(

			ImmutableArray.of(

				new ImageLayer(imageLoader.loadItemImage("fairy_layer0"), new ColorIdentifier("@skin")),
				new ImageLayer(imageLoader.loadItemImage("fairy_layer1"), new ColorIdentifier("#00BE00")),
				new ImageLayer(imageLoader.loadItemImage("fairy_layer2"), new ColorIdentifier("@darker")),
				new ImageLayer(imageLoader.loadItemImage("fairy_layer3"), new ColorIdentifier("@brighter")),
				new ImageLayer(imageLoader.loadItemImage("fairy_layer4"), new ColorIdentifier("@hair"))

			),
			ImmutableArray.of(

				new ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer0"), new ColorIdentifier("@darker")),
				new ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer1"), new ColorIdentifier("@skin")),
				new ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer2"), new ColorIdentifier("@brighter")),
				new ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer3"), new ColorIdentifier("@hair"))

			),
			ImmutableArray.of(

				new ImageLayer(imageLoader.loadItemImage("sphere_layer0"), new ColorIdentifier("@darker")),
				new ImageLayer(imageLoader.loadItemImage("sphere_layer1"), new ColorIdentifier("@hair")),
				new ImageLayer(imageLoader.loadItemImage("sphere_layer2"), new ColorIdentifier("@skin")),
				new ImageLayer(imageLoader.loadItemImage("sphere_layer3"), new ColorIdentifier("@brighter"))

			)

		);
	}

}
