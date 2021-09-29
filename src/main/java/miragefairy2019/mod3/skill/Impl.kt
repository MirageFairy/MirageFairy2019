package miragefairy2019.mod3.skill

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import miragefairy2019.libkt.buildText
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.api.ISkillContainer
import miragefairy2019.mod3.skill.api.ISkillManager
import miragefairy2019.mod3.skill.api.ISkillVariables
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import java.io.File
import java.time.Instant
import kotlin.math.roundToInt
import kotlin.math.sqrt


abstract class SkillManager : ISkillManager {
    private val clientSkillContainer = SkillContainer(this)
    override fun getClientSkillContainer() = clientSkillContainer

    private val serverSkillContainers = mutableMapOf<String, ISkillContainer>()
    override fun getServerSkillContainer(player: EntityPlayer) = serverSkillContainers.computeIfAbsent(player.cachedUniqueIdString) { SkillContainer(this).also { it.load(player) } }

    override fun receive(json: String) = run { clientSkillContainer.json = json }
    override fun resetServer() = serverSkillContainers.clear()

    abstract fun getFile(player: EntityPlayer): File
    abstract fun send(player: EntityPlayerMP, json: String)

    override fun getFairyMasterExp(lv: Int) = 5 * lv * (lv + 1) / 2
    private fun getInaccurateFairyMasterLevel(exp: Int) = (1.0 / 10.0) * (sqrt(5.0) * sqrt(8.0 * exp.toDouble() + 5.0) - 5.0)
    override fun getFairyMasterLevel(exp: Int) = getInaccurateFairyMasterLevel(exp).roundToInt().let { if (exp < getFairyMasterExp(it)) it - 1 else it }
}

fun ISkillManager.getRequiredFairyMasterExpForNextLevel(exp: Int) = getFairyMasterExp(getFairyMasterLevel(exp) + 1) - exp


class SkillContainer(private val manager: SkillManager) : ISkillContainer {
    override fun getSkillManager() = manager
    override fun load(player: EntityPlayer) = manager.getFile(player).let { if (it.exists()) json = it.readText() else model = SkillModel() }
    override fun save(player: EntityPlayer) = manager.getFile(player).also { it.parentFile.mkdirs() }.writeText(json)
    override fun send(player: EntityPlayerMP) = manager.send(player, json)


    private val gson = GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()!!
    var json: String
        get() = gson.toJson(model)
        set(json) = run { model = gson.fromJson(json, SkillModel::class.java) }

    private var model = SkillModel()


    override fun getMasteryList() = model.getMasteryLevels().keys
    override fun getMasteryLevel(mastery: String) = model.getMasteryLevels()[mastery] ?: 0
    override fun setMasteryLevel(mastery: String, masteryLevel: Int) = run { model.getMasteryLevels()[mastery] = masteryLevel }

    override fun getVariables(): ISkillVariables = model.getVariables()
}

fun ISkillContainer.getSkillLevel(mastery: IMastery): Int = getMasteryLevel(mastery.name) * mastery.coefficient + (mastery.parent?.let { getSkillLevel(it) } ?: 0)

data class SkillModel(
        @[JvmField Expose] var masteryLevels: MutableMap<String, Int>? = null,
        @[JvmField Expose] var variables: SkillVariables? = null
) {
    fun getMasteryLevels() = masteryLevels ?: mutableMapOf<String, Int>().also { masteryLevels = it }
    fun getVariables() = variables ?: SkillVariables().also { variables = it }
}

data class SkillVariables(
        @[JvmField Expose] var exp: Int? = null,
        @[JvmField Expose] var lastMasteryResetTime: Long? = null,
        @[JvmField Expose] var lastAstronomicalObservationTime: Long? = null
) : ISkillVariables {
    override fun getExp(): Int = exp ?: 0
    override fun setExp(it: Int) = run { exp = it }
    override fun getLastMasteryResetTime() = lastMasteryResetTime?.let { Instant.ofEpochMilli(it) }
    override fun setLastMasteryResetTime(it: Instant?) = run { lastMasteryResetTime = it?.toEpochMilli() }
    override fun getLastAstronomicalObservationTime() = lastAstronomicalObservationTime?.let { Instant.ofEpochMilli(it) }
    override fun setLastAstronomicalObservationTime(it: Instant?) = run { lastAstronomicalObservationTime = it?.toEpochMilli() }
}


val IMastery.displayName get() = buildText { translate("mirageFairy2019.mastery.$name.name") }
val IMastery.displayPoem get() = buildText { translate("mirageFairy2019.mastery.$name.poem") }
