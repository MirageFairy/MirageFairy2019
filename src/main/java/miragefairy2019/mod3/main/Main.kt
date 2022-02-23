package miragefairy2019.mod3.main

import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.artifacts.fairycrystal.variantFairyCrystal
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object Main {
    val module = module {

        onPreInit {
            ApiMain.logger = modLog
            ApiMain.side = side
        }

        onInitCreativeTab {
            ApiMain.creativeTab = object : CreativeTabs("mirageFairy2019") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = variantFairyCrystal().createItemStack()
            }
        }

        onInitNetworkChannel {
            ApiMain.simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID)
        }

        // Gui Handler
        onInit {
            NetworkRegistry.INSTANCE.registerGuiHandler(ModMirageFairy2019.instance, object : IGuiHandler {
                override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = ApiMain.guiHandlers[id]?.getServerGuiElement(id, player, world, x, y, z)
                override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = ApiMain.guiHandlers[id]?.getClientGuiElement(id, player, world, x, y, z)
            })
        }

    }
}
