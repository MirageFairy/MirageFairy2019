package miragefairy2019.mod.fairybox

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand

class TileEntityFairyBoxCentrifuge : TileEntityFairyBoxBase() {
    override fun getExecutor(): IFairyBoxExecutor {
        return object : IFairyBoxExecutor {
            override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
                return true
            }

            override fun onUpdateTick() {

            }
        }
    }
}
