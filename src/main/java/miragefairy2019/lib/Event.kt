package miragefairy2019.lib

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event

fun Event.send() = MinecraftForge.EVENT_BUS.post(this)
