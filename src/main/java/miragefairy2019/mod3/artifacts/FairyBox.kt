package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

object FairyBox {
    lateinit var blockFairyBox: () -> BlockFairyBox
    lateinit var itemBlockFairyBox: () -> ItemBlock
    val module: Module = {
        blockFairyBox = block({ BlockFairyBox() }, "fairy_box") {
            setUnlocalizedName("fairyBox")
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).map { facing ->
                        "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_box", y = facing.second)
                    }
                )
            }
        }
        itemBlockFairyBox = item({ ItemBlock(blockFairyBox()) }, "fairy_box") {
            setCustomModelResourceLocation(variant = "facing=north")
        }
        tileEntity("fairy_box", TileEntityFairyBox::class.java)
    }
}

class BlockFairyBox : BlockFairyBoxBase() {
    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyBox()
}

class TileEntityFairyBox : TileEntity()
