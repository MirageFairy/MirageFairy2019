package miragefairy2019.mod3.main

import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.artifacts.FairyCrystal
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger

object Main {
    lateinit var logger: Logger
    lateinit var side: Side
    lateinit var creativeTab: CreativeTabs

    lateinit var simpleNetworkWrapper: SimpleNetworkWrapper

    private val guiHandlers = mutableMapOf<Int, IGuiHandler>()
    fun registerGuiHandler(id: Int, guiHandler: IGuiHandler) {
        if (id in guiHandlers) throw RuntimeException("Duplicated GuiId: $id")
        guiHandlers[id] = guiHandler
    }

    val module = module {

        onPreInit {
            Main.logger = modLog
            Main.side = side
        }

        onInitCreativeTab {
            creativeTab = object : CreativeTabs("mirageFairy2019") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyCrystal.variantFairyCrystal().createItemStack()
            }
        }

        onInitNetworkChannel {
            simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID)
        }

        // Gui Handler
        onInit {
            NetworkRegistry.INSTANCE.registerGuiHandler(ModMirageFairy2019.instance, object : IGuiHandler {
                override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = guiHandlers[id]?.getServerGuiElement(id, player, world, x, y, z)
                override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = guiHandlers[id]?.getClientGuiElement(id, player, world, x, y, z)
            })
        }

    }
}
