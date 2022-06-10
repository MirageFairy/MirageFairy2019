package miragefairy2019.mod.skill

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.time.Instant

object ApiSkill {
    lateinit var skillManager: ISkillManager
}

interface ISkillManager {
    /** Client World Only */
    @SideOnly(Side.CLIENT)
    fun getClientSkillContainer(): ISkillContainer

    /** Server World Only */
    fun getServerSkillContainer(player: EntityPlayer): ISkillContainer

    /** Client World Only */
    fun receive(json: String)

    /** Server World Only */
    fun resetServer()

    fun getFairyMasterExp(lv: Int): Int
    fun getFairyMasterLevel(exp: Int): Int
}

interface ISkillContainer {
    val skillManager: ISkillManager

    /** Server World Only */
    fun load(player: EntityPlayer)

    /** Server World Only */
    fun save(player: EntityPlayer)

    /** Server World Only */
    fun send(player: EntityPlayerMP)


    val masteryList: Iterable<String>
    fun getMasteryLevel(mastery: String): Int
    fun setMasteryLevel(mastery: String, masteryLevel: Int)
    val variables: ISkillVariables
}

interface ISkillVariables {
    fun getExp(): Int
    fun setExp(exp: Int)
    fun getLastMasteryResetTime(): Instant?
    fun setLastMasteryResetTime(lastMasteryResetTime: Instant?)
    fun getLastAstronomicalObservationTime(): Instant?
    fun setLastAstronomicalObservationTime(lastAstronomicalObservationTime: Instant?)
}


interface IMastery {
    val parent: IMastery?
    val name: String
    val coefficient: Int
}
