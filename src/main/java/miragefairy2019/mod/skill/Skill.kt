package miragefairy2019.mod.skill

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.Main
import miragefairy2019.mod.Main.logger
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.PacketId
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.fml.relauncher.Side
import java.io.File

val skillModule = module {

    // マネージャー初期化
    onInstantiation {
        ApiSkill.skillManager = object : SkillManager() {
            override fun getFile(player: EntityPlayer): File = player.world.minecraftServer!!.getWorld(0).saveHandler.worldDirectory.resolve("${ModMirageFairy2019.MODID}/skill/${player.cachedUniqueIdString}.json")
            override fun send(player: EntityPlayerMP, json: String) {
                Main.simpleNetworkWrapper.sendTo(MessageSkill(json), player)
            }
        }
    }

    // ネットワークメッセージ登録
    onRegisterNetworkMessage {
        Main.simpleNetworkWrapper.registerMessage(PacketSkill::class.java, MessageSkill::class.java, PacketId.skill, Side.CLIENT)
        Main.simpleNetworkWrapper.registerMessage(PacketTrainMastery::class.java, MessageTrainMastery::class.java, PacketId.trainMastery, Side.SERVER)
        Main.simpleNetworkWrapper.registerMessage(PacketResetMastery::class.java, MessageResetMastery::class.java, PacketId.resetMastery, Side.SERVER)
    }

    onInit {
        MinecraftForge.EVENT_BUS.register(object {

            // ログインイベント
            @[Suppress("unused") SubscribeEvent]
            fun handle(event: FMLNetworkEvent.ServerConnectionFromClientEvent) {
                val handler = event.handler as? NetHandlerPlayServer ?: return
                ApiSkill.skillManager.getServerSkillContainer(handler.player).send(handler.player)
            }

            // 保存イベント
            @[Suppress("unused") SubscribeEvent]
            fun handle(event: PlayerEvent.SaveToFile) {
                val player = event.entityPlayer as? EntityPlayerMP ?: return
                ApiSkill.skillManager.getServerSkillContainer(player).save(player)
            }

            // ワールド停止時にロード済みのデータを破棄
            @[Suppress("unused") SubscribeEvent]
            fun handle(event: WorldEvent.Unload) {
                if (event.world.isRemote) return // サーバーワールドのみ
                if (event.world.minecraftServer!!.getWorld(0) !== event.world) return // 地上ディメンションのみ
                logger.info("Unloading all the skill containers")
                ApiSkill.skillManager.resetServer()
            }

        })
    }

    skillGuiModule()
}
