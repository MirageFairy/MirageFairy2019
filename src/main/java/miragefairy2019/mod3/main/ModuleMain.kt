package miragefairy2019.mod3.main

import miragefairy2019.libkt.Module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

val moduleMain: Module = {

    onPreInit {
        ApiMain.logger = modLog
        ApiMain.side = side
    }

    onInitCreativeTab {
        ApiMain.creativeTab = object : CreativeTabs("mirageFairy2019") {
            @SideOnly(Side.CLIENT)
            override fun getTabIconItem() = ModuleFairyCrystal.variantFairyCrystal.createItemStack()
        }
    }

    onInitNetworkChannel {
        ApiMain.simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID)
    }

    // Gui Handler
    onInit {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModMirageFairy2019.instance, object : IGuiHandler {
            override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = ApiMain.guiHandlers[id]?.getServerGuiElement(id, player, world, x, y, z)
            override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = ApiMain.guiHandlers[id]?.getClientGuiElement(id, player, world, x, y, z)
        })
    }

}
