package miragefairy2019.mod.skill

import miragefairy2019.libkt.textComponent
import mirrg.kotlin.gson.hydrogen.JsonWrapper
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull
import mirrg.kotlin.gson.hydrogen.toJsonWrapper
import mirrg.kotlin.gson.hydrogen.toJson
import mirrg.kotlin.gson.hydrogen.toJsonElement
import mirrg.kotlin.startOfMonth
import mirrg.kotlin.toInstantAsUtc
import mirrg.kotlin.utcLocalDateTime
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
    override val skillManager get() = manager
    override fun load(player: EntityPlayer) = manager.getFile(player).let { if (it.exists()) json = it.readText() else model = SkillModel() }
    override fun save(player: EntityPlayer) = manager.getFile(player).also { it.parentFile.mkdirs() }.writeText(json)
    override fun send(player: EntityPlayerMP) = manager.send(player, json)


    var json: String
        get() = model.toJsonElement().toJson { setPrettyPrinting() }
        set(json) = run { model = json.toJsonElement().toJsonWrapper().toSkillModel() }

    private var model = SkillModel()


    override val masteryList get() = model.masteryLevels.keys
    override fun getMasteryLevel(mastery: String) = model.masteryLevels[mastery] ?: 0
    override fun setMasteryLevel(mastery: String, masteryLevel: Int) = run { model.masteryLevels[mastery] = masteryLevel }

    override val variables get(): ISkillVariables = model.variables
}

fun ISkillContainer.getSkillLevel(mastery: IMastery): Int = getMasteryLevel(mastery.name) * mastery.coefficient + (mastery.parent?.let { getSkillLevel(it) } ?: 0)
val ISkillContainer.usedSkillPoints get() = masteryList.sumBy { getMasteryLevel(it) }
val ISkillContainer.remainingSkillPoints get() = skillManager.getFairyMasterLevel(variables.exp) - usedSkillPoints
fun ISkillContainer.canResetMastery(now: Instant) = variables.lastMasteryResetTime.let { it == null || it < now.utcLocalDateTime.toLocalDate().startOfMonth.toInstantAsUtc }


class SkillModel(
    masteryLevels: Map<String, Int>? = null,
    variables: SkillVariables? = null
) {
    val masteryLevels = masteryLevels?.toMutableMap() ?: mutableMapOf()
    val variables = variables ?: SkillVariables()
}

fun JsonWrapper.toSkillModel() = SkillModel(
    masteryLevels = this["masteryLevels"].asMap().mapValues { it.value.asInt() }.toMutableMap(),
    variables = this["variables"].toSkillVariables()
)

fun SkillModel.toJsonElement() = jsonObjectNotNull(
    "masteryLevels" to masteryLevels.map { it.key to it.value.jsonElement }.jsonObject,
    "variables" to variables.toJsonElement()
)


class SkillVariables(
    exp: Int? = null,
    override var lastMasteryResetTime: Instant? = null,
    override var lastAstronomicalObservationTime: Instant? = null
) : ISkillVariables {
    override var exp = exp ?: 0
}

fun JsonWrapper.toSkillVariables() = SkillVariables(
    exp = this["exp"].orNull?.asInt(),
    lastMasteryResetTime = this["lastMasteryResetTime"].orNull?.asLong()?.let { Instant.ofEpochMilli(it) },
    lastAstronomicalObservationTime = this["lastAstronomicalObservationTime"].orNull?.asLong()?.let { Instant.ofEpochMilli(it) }
)

fun SkillVariables.toJsonElement() = jsonObjectNotNull(
    "exp" to exp.jsonElement,
    "lastMasteryResetTime" to lastMasteryResetTime?.toEpochMilli()?.jsonElement,
    "lastAstronomicalObservationTime" to lastAstronomicalObservationTime?.toEpochMilli()?.jsonElement
)


val IMastery.displayName get() = textComponent { translate("mirageFairy2019.mastery.$name.name") }
val IMastery.displayPoem get() = textComponent { translate("mirageFairy2019.mastery.$name.poem") }
