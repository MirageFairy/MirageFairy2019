package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import java.util.function.Supplier

object FairyCollectionBox {
    lateinit var blockFairyCollectionBox: Supplier<BlockFairyCollectionBox>
    lateinit var itemDish: Supplier<ItemBlock>

    val module: Module = {
        blockFairyCollectionBox = block({ BlockFairyCollectionBox() }, "fairy_collection_box") {
            setUnlocalizedName("fairyCollectionBox")
            setCreativeTab { ApiMain.creativeTab }
        }
        itemDish = item({ ItemBlock(blockFairyCollectionBox.get()) }, "fairy_collection_box") {
            setCustomModelResourceLocation()
        }
        tileEntity("fairy_collection_box", TileEntityFairyCollectionBox::class.java)
    }
}

class BlockFairyCollectionBox : BlockContainer(Material.WOOD) {

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyCollectionBox()
}

class TileEntityFairyCollectionBox : TileEntity() {

}
