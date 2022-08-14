package miragefairy2019.mod.beanstalk

import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.faced
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import net.minecraft.item.ItemBlock

lateinit var blockBeanstalkRoot: () -> BlockBeanstalk
lateinit var itemBlockBeanstalkRoot: () -> ItemBlock

val beanstalkRootModule = module {

    // ブロック
    blockBeanstalkRoot = block({ BlockBeanstalkEnd() }, "beanstalk_root") {
        setUnlocalizedName("beanstalkRoot")
        setCreativeTab { Main.creativeTab }
        makeBlockStates { faced }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/beanstalk",
                    "top" to "miragefairy2019:blocks/beanstalk_top",
                    "main" to "miragefairy2019:blocks/beanstalk_root",
                    "background" to "miragefairy2019:blocks/beanstalk_root_background",
                    "ground" to "miragefairy2019:blocks/beanstalk_root_ground"
                ),
                elements = listOf(

                    upDuplex(DataPoint(5.0, 16.0, 5.0), DataPoint(11.0, 16.0, 11.0), "#top"),
                    northDuplex(DataPoint(0.0, 0.0, 5.0), DataPoint(16.0, 16.0, 5.0), "#main"),
                    southDuplex(DataPoint(0.0, 0.0, 11.0), DataPoint(16.0, 16.0, 11.0), "#main"),
                    westDuplex(DataPoint(5.0, 0.0, 0.0), DataPoint(5.0, 16.0, 16.0), "#main"),
                    eastDuplex(DataPoint(11.0, 0.0, 0.0), DataPoint(11.0, 16.0, 16.0), "#main"),

                    // 根元の太い部分
                    DataElement(
                        from = DataPoint(4.0, 0.0, 4.0),
                        to = DataPoint(12.0, 2.0, 12.0),
                        faces = DataFaces(
                            north = DataFace(texture = "#background"),
                            south = DataFace(texture = "#background"),
                            west = DataFace(texture = "#background"),
                            east = DataFace(texture = "#background"),
                            up = DataFace(texture = "#background")
                        )
                    ),

                    // 地面を這う根
                    DataElement(
                        from = DataPoint(0.0, 0.01, 0.0),
                        to = DataPoint(16.0, 0.01, 16.0),
                        faces = DataFaces(
                            up = DataFace(texture = "#ground"),
                            down = DataFace(texture = "#ground")
                        )
                    )

                )
            )
        }
    }

    // アイテム
    itemBlockBeanstalkRoot = item({ ItemBlock(blockBeanstalkRoot()) }, "beanstalk_root") {
        setCustomModelResourceLocation()
        makeItemModel { block }
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "#",
                    "m"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:beanstalk_pipe"),
                    "m" to DataOreIngredient(ore = "mirageFairyMandrake")
                ),
                result = DataResult(item = "miragefairy2019:beanstalk_root")
            )
        }
    }

    // 翻訳生成
    onMakeLang {
        enJa("tile.beanstalkRoot.name", "Beanstalk Root", "豆の木の根")
    }

}
