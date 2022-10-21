package miragefairy2019.mod.fairybox

import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.mod.Main
import net.minecraft.item.ItemBlock

lateinit var blockFairyHouse: () -> BlockFairyBoxBase
lateinit var itemBlockFairyHouse: () -> ItemBlock

val fairyHouseModule = module {

    // ブロック
    blockFairyHouse = block({ BlockFairyBoxBase(5) { TileEntityFairyHouse() } }, "fairy_box") {
        setUnlocalizedName("fairyHouse")
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataModelBlockDefinition(
                variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                    "facing=${facing.first}" to DataSingleVariantList(DataVariant("miragefairy2019:fairy_box", y = facing.second))
                }
            )
        }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                elements = listOf(
                    DataElement(
                        from = DataPoint(0.0, 0.0, 0.0),
                        to = DataPoint(16.0, 16.0, 16.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#end", cullface = "down"),
                            up = DataFace(texture = "#end", cullface = "up"),
                            north = DataFace(texture = "#side", cullface = "north"),
                            south = DataFace(texture = "#side", cullface = "south"),
                            west = DataFace(texture = "#side", cullface = "west"),
                            east = DataFace(texture = "#side", cullface = "east")
                        )
                    ),
                    DataElement(
                        from = DataPoint(0.0, 0.0, 0.0),
                        to = DataPoint(16.0, 16.0, 16.0),
                        faces = DataFaces(
                            north = DataFace(texture = "#entrance", cullface = "north"),
                            south = DataFace(texture = "#window", cullface = "south"),
                            west = DataFace(texture = "#window", cullface = "west"),
                            east = DataFace(texture = "#window", cullface = "east")
                        )
                    )
                ),
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/fairy_wood_log",
                    "end" to "miragefairy2019:blocks/fairy_wood_log_top",
                    "side" to "miragefairy2019:blocks/fairy_wood_log",
                    "entrance" to "miragefairy2019:blocks/fairy_box_entrance",
                    "window" to "miragefairy2019:blocks/fairy_box_output"
                )
            )
        }
    }

    // アイテム
    itemBlockFairyHouse = item({ ItemBlock(blockFairyHouse()) }, "fairy_box") {
        setCustomModelResourceLocation(variant = "facing=north")
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "sls",
                    "PLD",
                    "sCs"
                ),
                key = mapOf(
                    "L" to DataOreIngredient(ore = "logFairyWood"),
                    "P" to DataOreIngredient(ore = "paneGlass"),
                    "D" to DataOreIngredient(ore = "doorWood"),
                    "l" to DataSimpleIngredient(item = "miragefairy2019:light_magic_wand"),
                    "C" to DataOreIngredient(ore = "mirageFairyLeather"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSpace")
                ),
                result = DataResult(item = "miragefairy2019:fairy_box")
            )
        }
    }

    // タイルエンティティ登録
    tileEntity("fairy_box", TileEntityFairyHouse::class.java)

    // 翻訳生成
    lang("tile.fairyHouse.name", "Fairy Box", "妖精の家")
    lang("tile.fairyHouse.poem", "", "大きな刳りの木の中で")

}

class TileEntityFairyHouse : TileEntityFairyBoxBase()
