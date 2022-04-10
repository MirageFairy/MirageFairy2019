package miragefairy2019.mod.fairybox

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.block
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod.Main
import net.minecraft.item.ItemBlock

object FairyBox {
    val module = module {

        // 妖精の家（空）
        run {
            val block = block({ BlockFairyBoxBase { TileEntityFairyBoxEmpty() } }, "fairy_box") {
                setUnlocalizedName("fairyBox")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_box", y = facing.second)
                        }
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "fairy_box") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyBox.name", "Fairy Box", "妖精の家") }
            tileEntity("fairy_box", TileEntityFairyBoxEmpty::class.java)
        }

        // 樹液の家
        run {
            val block = block({ BlockFairyBoxBase { TileEntityFairyBoxResinTapper() } }, "fairy_resin_tapper") {
                setUnlocalizedName("fairyResinTapper")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState(
                                "miragefairy2019:fairy_resin_tapper",
                                y = facing.second
                            )
                        }
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "fairy_resin_tapper") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyResinTapper.name", "Fairy Resin Extractor", "樹液取り職人スプルーツァの家") }
            tileEntity("fairy_resin_tapper", TileEntityFairyBoxResinTapper::class.java)
        }

    }
}
