package miragefairy2019.mod.systems

import appeng.api.AEApi
import miragefairy2019.lib.Compatibility
import miragefairy2019.lib.getLogger
import miragefairy2019.libkt.module
import miragefairy2019.mod.Main
import miragefairy2019.mod.configProperty
import miragefairy2019.mod.artifacts.ChatWebhook
import net.minecraft.block.Block
import net.minecraft.init.Blocks

object Ae2SpatialIoBlocking {
    val logger = getLogger()
    val module = module {
        val disableVanillaAe2SpatialIo = configProperty { it.getBoolean("disableVanillaAe2SpatialIo", Main.categoryFeatures, false, "Disables the movement of some vanilla blocks by Spatial IO.") }

        onInit {

            logger.info("enabled: ${Compatibility.ae2}")
            if (Compatibility.ae2) {
                fun disable(block: Block) = AEApi.instance().registries().movable().blacklistBlock(block)

                logger.info("disableVanillaAe2SpatialIo: ${disableVanillaAe2SpatialIo()}")
                if (disableVanillaAe2SpatialIo()) {
                    disable(Blocks.BARRIER)
                    disable(Blocks.COMMAND_BLOCK)
                    disable(Blocks.CHAIN_COMMAND_BLOCK)
                    disable(Blocks.END_GATEWAY)
                    disable(Blocks.END_PORTAL)
                    disable(Blocks.END_PORTAL_FRAME)
                    disable(Blocks.REPEATING_COMMAND_BLOCK)
                    disable(Blocks.STRUCTURE_BLOCK)
                    disable(Blocks.STRUCTURE_VOID)
                }

                disable(ChatWebhook.blockCreativeChatWebhookTransmitter())

            }

        }
    }
}
