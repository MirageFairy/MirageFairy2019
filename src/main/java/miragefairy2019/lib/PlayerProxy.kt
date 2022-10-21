package miragefairy2019.lib

import miragefairy2019.mod.playeraura.ApiPlayerAura
import miragefairy2019.mod.playeraura.IPlayerAuraHandler
import miragefairy2019.mod.skill.ApiSkill
import miragefairy2019.mod.skill.ISkillContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP

sealed class PlayerProxy
object ClientPlayerProxy : PlayerProxy()
class ServerPlayerProxy(val player: EntityPlayerMP) : PlayerProxy()

val EntityPlayer.proxy get() = if (world.isRemote) ClientPlayerProxy else ServerPlayerProxy(this as EntityPlayerMP)


// TODO move

val PlayerProxy.playerAuraHandler: IPlayerAuraHandler
    get() = when (this) {
        is ClientPlayerProxy -> ApiPlayerAura.playerAuraManager.getClientPlayerAuraHandler()
        is ServerPlayerProxy -> ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(player)
    }
