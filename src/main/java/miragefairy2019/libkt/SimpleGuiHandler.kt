package miragefairy2019.libkt

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

interface ISimpleGuiHandler {
    fun GuiHandlerContext.onServer(): Any?
    fun GuiHandlerContext.onClient(): Any?
}

val ISimpleGuiHandler.guiHandler
    get() = object : IGuiHandler {
        override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = GuiHandlerContext(id, player, world, x, y, z).onServer()
        override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = GuiHandlerContext(id, player, world, x, y, z).onClient()
    }


data class GuiHandlerContext(val id: Int, val player: EntityPlayer, val world: World, val x: Int, val y: Int, val z: Int)

val GuiHandlerContext.tileEntity get() = world.getTileEntity(BlockPos(x, y, z))
