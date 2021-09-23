package miragefairy2019.mod3.main

import miragefairy2019.libkt.Module
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry


private val guiHandlers = mutableMapOf<Int, IGuiHandler>()

fun registerGuiHandler(id: Int, guiHandler: IGuiHandler) {
    if (id in guiHandlers) throw RuntimeException("Duplicated GuiId: $id")
    guiHandlers[id] = guiHandler
}


val moduleMain: Module = {

    // Gui Handler
    onInit {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModMirageFairy2019.instance, object : IGuiHandler {
            override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = guiHandlers[id]?.getServerGuiElement(id, player, world, x, y, z)
            override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = guiHandlers[id]?.getClientGuiElement(id, player, world, x, y, z)
        })
    }

}
