package miragefairy2019.mod3.skill

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.main.api.ApiMain.creativeTab
import miragefairy2019.mod3.main.api.ApiMain.logger
import miragefairy2019.mod3.skill.api.ApiSkill
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.relauncher.Side
import java.io.File

val moduleSkill: Module = {

    // マネージャー初期化
    onInstantiation {
        ApiSkill.skillManager = object : SkillManager() {
            override fun getFile(player: EntityPlayer): File = player.world.minecraftServer!!.getWorld(0).saveHandler.worldDirectory.resolve("${ModMirageFairy2019.MODID}/skill/${player.cachedUniqueIdString}.json")
            override fun send(player: EntityPlayerMP, json: String) {
                ApiMain.simpleNetworkWrapper.sendTo(MessageSkill(json), player)
            }
        }
    }

    // ネットワークメッセージ登録
    onRegisterNetworkMessage {
        ApiMain.simpleNetworkWrapper.registerMessage(PacketSkill::class.java, MessageSkill::class.java, discriminatorSkill, Side.CLIENT)
        ApiMain.simpleNetworkWrapper.registerMessage(PacketTrainMastery::class.java, MessageTrainMastery::class.java, discriminatorTrainMastery, Side.SERVER)
        ApiMain.simpleNetworkWrapper.registerMessage(PacketResetMastery::class.java, MessageResetMastery::class.java, discriminatorResetMastery, Side.SERVER)
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

    // スキルGUI
    onInit {
        ApiMain.registerGuiHandler(guiIdSkill, object : IGuiHandler {
            override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = ContainerSkill()
            override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int) = GuiSkill()
        })
    }

    // スキルブック
    itemSkillBook = item({ ItemSkillBook() }, "skill_book") {
        setUnlocalizedName("skillBook")
        setCreativeTab { creativeTab }
        setCustomModelResourceLocation()
    }

    // 天体観測ブック
    itemAstronomicalObservationBook = item({ ItemAstronomicalObservationBook() }, "astronomical_observation_book") {
        setUnlocalizedName("astronomicalObservationBook")
        setCreativeTab { creativeTab }
        setCustomModelResourceLocation()
    }

}
