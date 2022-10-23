package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.DimensionalPos
import mirrg.kotlin.log4j.hydrogen.getLogger
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class LightningBoltBlocker // TODO remove

val lightningBoltBlockerModule = module {
    onInit {
        MinecraftForge.EVENT_BUS.register(object {
            @Suppress("unused")
            @SubscribeEvent
            fun handle(event: EntityJoinWorldEvent) {
                if (event.world.isRemote) return
                val daemons = DaemonManager.daemons ?: return
                val entity = event.entity as? EntityLightningBolt ?: return
                val guard = daemons.any { (dimensionalPos, daemon) ->
                    if (daemon !is ILightningBoltBlockerDaemon) return@any false
                    if (!daemon.canBlockLightningBolt(dimensionalPos, event.world, entity)) return@any false
                    getLogger(LightningBoltBlocker::class.java).debug("$dimensionalPos $daemon") // TODO remove
                    true
                }
                if (guard) event.isCanceled = true
                getLogger(LightningBoltBlocker::class.java).debug("${event.world.provider.dimension},${entity.position} guard=$guard") // TODO remove
            }
        })
    }
}

interface ILightningBoltBlockerDaemon {
    fun canBlockLightningBolt(dimensionalPos: DimensionalPos, world: World, entity: EntityLightningBolt): Boolean { // TODO -> abstract
        if (dimensionalPos.dimension != world.provider.dimension) return false
        val distance = dimensionalPos.pos.getDistance(entity.position.x, entity.position.y, entity.position.z)
        if (distance > 64) return false
        return true
    }
}
