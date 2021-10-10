package miragefairy2019.colormaker

import miragefairy2019.colormaker.core.ColorIdentifier
import miragefairy2019.colormaker.core.ImageLayer
import miragefairy2019.colormaker.core.ImageLoader
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.boron.util.struct.ImmutableArray
import mirrg.boron.util.struct.Tuple3
import java.awt.Color

object MainColorMakerFairy {
    @JvmStatic
    fun main(args: Array<String>) {
        WindowColorMakerSingle(
                ImageLoader(ModMirageFairy2019::class.java.classLoader, ModMirageFairy2019.MODID),
                ImmutableArray.of(
                        Tuple3.of("skin", "fairy_layer0", Color(0xFFFFFF)),
                        Tuple3.of("dress", "fairy_layer1", Color(0xFFFFFF)),
                        Tuple3.of("dark", "fairy_layer2", Color(0xFFFFFF)),
                        Tuple3.of("bright", "fairy_layer3", Color(0xFFFFFF)),
                        Tuple3.of("hair", "fairy_layer4", Color(0xFFFFFF))),
                ImmutableArray.of("skin", "bright", "dark", "hair")
        ).isVisible = true
    }
}

object MainColorMakerSphere {
    @JvmStatic
    fun main(args: Array<String>) {
        WindowColorMakerSingle(
                ImageLoader(ModMirageFairy2019::class.java.classLoader, ModMirageFairy2019.MODID),
                ImmutableArray.of(
                        Tuple3.of("background", "sphere_layer0", Color(0xFFFFFF)),
                        Tuple3.of("plasma", "sphere_layer1", Color(0xFFFFFF)),
                        Tuple3.of("core", "sphere_layer2", Color(0xFFFFFF)),
                        Tuple3.of("highlight", "sphere_layer3", Color(0xFFFFFF))),
                ImmutableArray.of("core", "highlight", "background", "plasma")
        ).isVisible = true
    }
}

object MainColorMaker {
    @JvmStatic
    fun main(args: Array<String>) {
        WindowColorMaker().isVisible = true
    }

    @JvmStatic
    fun loadImages(): ImmutableArray<ImmutableArray<ImageLayer>> {
        val imageLoader = ImageLoader(ModMirageFairy2019::class.java.classLoader, ModMirageFairy2019.MODID)
        return ImmutableArray.of(
                ImmutableArray.of(
                        ImageLayer(imageLoader.loadItemImage("fairy_layer0"), ColorIdentifier("@skin")),
                        ImageLayer(imageLoader.loadItemImage("fairy_layer1"), ColorIdentifier("#00BE00")),
                        ImageLayer(imageLoader.loadItemImage("fairy_layer2"), ColorIdentifier("@darker")),
                        ImageLayer(imageLoader.loadItemImage("fairy_layer3"), ColorIdentifier("@brighter")),
                        ImageLayer(imageLoader.loadItemImage("fairy_layer4"), ColorIdentifier("@hair"))
                ),
                ImmutableArray.of(
                        ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer0"), ColorIdentifier("@darker")),
                        ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer1"), ColorIdentifier("@skin")),
                        ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer2"), ColorIdentifier("@brighter")),
                        ImageLayer(imageLoader.loadItemImage("mirage_wisp_layer3"), ColorIdentifier("@hair"))
                ),
                ImmutableArray.of(
                        ImageLayer(imageLoader.loadItemImage("sphere_layer0"), ColorIdentifier("@darker")),
                        ImageLayer(imageLoader.loadItemImage("sphere_layer1"), ColorIdentifier("@hair")),
                        ImageLayer(imageLoader.loadItemImage("sphere_layer2"), ColorIdentifier("@skin")),
                        ImageLayer(imageLoader.loadItemImage("sphere_layer3"), ColorIdentifier("@brighter"))
                )
        )
    }
}
