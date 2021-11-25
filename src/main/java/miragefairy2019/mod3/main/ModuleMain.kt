package miragefairy2019.mod3.main

import miragefairy2019.libkt.Module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger


lateinit var logger: Logger
lateinit var side: Side

lateinit var creativeTab: CreativeTabs


private val guiHandlers = mutableMapOf<Int, IGuiHandler>()

fun registerGuiHandler(id: Int, guiHandler: IGuiHandler) {
    if (id in guiHandlers) throw RuntimeException("Duplicated GuiId: $id")
    guiHandlers[id] = guiHandler
}


val moduleMain: Module = {

    onPreInit {
        logger = modLog
        miragefairy2019.mod3.main.side = side
    }

    // Gui Handler
    onInit {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModMirageFairy2019.instance, object : IGuiHandler {
            override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = guiHandlers[id]?.getServerGuiElement(id, player, world, x, y, z)
            override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = guiHandlers[id]?.getClientGuiElement(id, player, world, x, y, z)
        })
    }

    // ネットワークラッパー初期化
    onInitNetworkChannel {
        ApiMain.simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID)
    }
    onInitCreativeTab {
        creativeTab = object : CreativeTabs("mirageFairy2019") {
            @SideOnly(Side.CLIENT)
            override fun getTabIconItem() = ModuleFairyCrystal.variantFairyCrystal.createItemStack()
        }
    }

}
