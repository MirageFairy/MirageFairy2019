package miragefairy2019.mod.fairybox

import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import net.minecraft.item.ItemBlock

lateinit var blockFairyHouse: () -> BlockFairyBoxBase
lateinit var itemBlockFairyHouse: () -> ItemBlock

val fairyHouseModule = module {
    blockFairyHouse = block({ BlockFairyBoxBase(4) { TileEntityFairyBoxEmpty() } }, "fairy_box") {
        setUnlocalizedName("fairyBox") // TODO rename
        setCreativeTab { Main.creativeTab }
        makeBlockStates(resourceName.path) {
            DataBlockStates(
                variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                    "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_box", y = facing.second)
                }
            )
        }
        makeBlockModel(resourceName.path) {
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
    itemBlockFairyHouse = item({ ItemBlock(blockFairyHouse()) }, "fairy_box") {
        setCustomModelResourceLocation(variant = "facing=north")
    }
    onMakeLang { enJa("tile.fairyBox.name", "Fairy Box", "妖精の家") }
    onMakeLang { enJa("tile.fairyBox.poem", "", "大きな刳りの木の中で") }
    tileEntity("fairy_box", TileEntityFairyBoxEmpty::class.java)
    makeRecipe("fairy_box") {
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

class TileEntityFairyBoxEmpty : TileEntityFairyBoxBase()
