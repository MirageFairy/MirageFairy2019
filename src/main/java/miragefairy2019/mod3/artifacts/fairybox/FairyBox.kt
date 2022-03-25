package miragefairy2019.mod3.artifacts.fairybox

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.item.ItemBlock

object FairyBox {
    lateinit var blockFairyBox: () -> BlockFairyBox
    lateinit var itemBlockFairyBox: () -> ItemBlock

    lateinit var blockFairyResinTapper: () -> BlockFairyResinTapper
    lateinit var itemBlockFairyResinTapper: () -> ItemBlock

    val module = module {

        blockFairyBox = block({ BlockFairyBox() }, "fairy_box") {
            setUnlocalizedName("fairyBox")
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                        "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_box", y = facing.second)
                    }
                )
            }
        }
        itemBlockFairyBox = item({ ItemBlock(blockFairyBox()) }, "fairy_box") {
            setCustomModelResourceLocation(variant = "facing=north")
        }
        tileEntity("fairy_box", TileEntityFairyBox::class.java)

        blockFairyResinTapper = block({ BlockFairyResinTapper() }, "fairy_resin_tapper") {
            setUnlocalizedName("fairyResinTapper")
            setCreativeTab { ApiMain.creativeTab }
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
        itemBlockFairyResinTapper = item({ ItemBlock(blockFairyResinTapper()) }, "fairy_resin_tapper") {
            setCustomModelResourceLocation(variant = "facing=north")
        }
        tileEntity("fairy_resin_tapper", TileEntityFairyResinTapper::class.java)

    }
}
