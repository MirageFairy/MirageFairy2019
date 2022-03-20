package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.module
import miragefairy2019.libkt.textComponent
import mirrg.kotlin.castOrNull
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object VanillaItemBlocking {
    val logger: Logger = LogManager.getLogger(javaClass)
    val module = module {
        val disableVanillaWarpItems = configProperty { it.getBoolean("disableVanillaWarpItems", Config.categoryFeatures, false, "Disables vanilla items that allow warping.") }

        onInit {
            logger.info("disableVanillaWarpItems: ${disableVanillaWarpItems()}")
            if (disableVanillaWarpItems()) {
                MinecraftForge.EVENT_BUS.register(object {
                    @SubscribeEvent
                    fun handle(event: PlayerInteractEvent.RightClickItem) {
                        when (event.itemStack.item) {
                            Items.ENDER_PEARL,
                            Items.CHORUS_FRUIT
                            -> {
                                logger.info("Blocked: ${event.itemStack.displayName}(${event.itemStack}) by ${event.entityPlayer.displayName}")
                                event.isCanceled = true
                                event.entityLiving.castOrNull<EntityPlayer>()?.sendStatusMessage(textComponent { !"このアイテムは使用できません" }, true) // TODO translate
                            }
                        }
                    }
                })
            }
        }
    }
}
