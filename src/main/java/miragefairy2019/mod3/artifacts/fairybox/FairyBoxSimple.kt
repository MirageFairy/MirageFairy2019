package miragefairy2019.mod3.artifacts.fairybox

import net.minecraft.world.World

class BlockFairyBox : BlockFairyBoxBase() {
    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyBox()
}

class TileEntityFairyBox : TileEntityFairyBoxBase() {
    override val executor: TileEntityExecutor? = null
}
