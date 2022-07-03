package miragefairy2019.libkt

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler


class GuiHandlerEvent(val id: Int, val player: EntityPlayer, val world: World, val x: Int, val y: Int, val z: Int)

val GuiHandlerEvent.tileEntity get() = world.getTileEntity(BlockPos(x, y, z))


interface ISimpleGuiHandler {
    fun onServer(event: GuiHandlerEvent): Any?
    fun onClient(event: GuiHandlerEvent): Any?
}

val ISimpleGuiHandler.guiHandler
    get() = object : IGuiHandler {
        override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = onServer(GuiHandlerEvent(id, player, world, x, y, z))
        override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = onClient(GuiHandlerEvent(id, player, world, x, y, z))
    }


interface ISimpleGuiHandlerTileEntity : ISimpleGuiHandler
