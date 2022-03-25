package miragefairy2019.mod3.artifacts.fairybox

import net.minecraft.world.World

class BlockFairyBoxEmpty : BlockFairyBoxBase() {
    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyBoxEmpty()
}

class TileEntityFairyBoxEmpty : TileEntityFairyBoxBase() {
    override fun getExecutor(): TileEntityExecutor? = null
}
