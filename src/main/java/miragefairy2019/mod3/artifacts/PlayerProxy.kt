package miragefairy2019.mod3.artifacts

import miragefairy2019.mod3.playeraura.api.ApiPlayerAura
import miragefairy2019.mod3.playeraura.api.IPlayerAuraHandler
import miragefairy2019.mod3.skill.ApiSkill
import miragefairy2019.mod3.skill.ISkillContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP

sealed class PlayerProxy
object ClientPlayerProxy : PlayerProxy()
class ServerPlayerProxy(val player: EntityPlayerMP) : PlayerProxy()

val EntityPlayer.proxy get() = if (world.isRemote) ClientPlayerProxy else ServerPlayerProxy(this as EntityPlayerMP)


// TODO move

val PlayerProxy.playerAuraHandler: IPlayerAuraHandler
    get() = when (this) {
        is ClientPlayerProxy -> ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler
        is ServerPlayerProxy -> ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(player)
    }

val PlayerProxy.skillContainer: ISkillContainer
    get() = when (this) {
        is ClientPlayerProxy -> ApiSkill.skillManager.clientSkillContainer
        is ServerPlayerProxy -> ApiSkill.skillManager.getServerSkillContainer(player)
    }
