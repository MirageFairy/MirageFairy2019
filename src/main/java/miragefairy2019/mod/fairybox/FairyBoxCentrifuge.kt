package miragefairy2019.mod.fairybox

import miragefairy2019.lib.container
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand

class TileEntityFairyBoxCentrifuge : TileEntityFairyBoxBase() {
    override fun getExecutor(): IFairyBoxExecutor {
        return object : IFairyBoxExecutor {
            override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
                if (world.isRemote) return true
                player.openGui(ModMirageFairy2019.instance, GuiId.fairyBoxCentrifuge, world, pos.x, pos.y, pos.z)
                return true
            }

            override fun onUpdateTick() {

            }
        }
    }

    fun createContainer(player: EntityPlayer) = container {
        width = 100
        height = 100
    }
}
