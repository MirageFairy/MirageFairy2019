package miragefairy2019.modkt.api.skill

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import java.io.File

abstract class SkillManager : ISkillManager {
    private val clientSkillContainer = SkillContainer(this)
    override fun getClientSkillContainer() = clientSkillContainer

    private val serverSkillContainers = mutableMapOf<String, ISkillContainer>()
    override fun getServerSkillContainer(player: EntityPlayer) = serverSkillContainers.computeIfAbsent(player.cachedUniqueIdString) { SkillContainer(this).also { it.load(player) } }

    override fun receive(json: String) = run { clientSkillContainer.json = json }
    override fun resetServer() = serverSkillContainers.clear()

    abstract fun getFile(player: EntityPlayer): File
    abstract fun send(player: EntityPlayerMP, json: String)
}

class SkillContainer(private val manager: SkillManager) : ISkillContainer {
    override fun load(player: EntityPlayer) = manager.getFile(player).let { if (it.exists()) json = it.readText() else model = SkillModel() }
    override fun save(player: EntityPlayer) = manager.getFile(player).also { it.parentFile.mkdirs() }.writeText(json)
    override fun send(player: EntityPlayerMP) = manager.send(player, json)

    private val gson = GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()!!
    private var model = SkillModel()
    var json: String
        get() = gson.toJson(model)
        set(json) = run { model = gson.fromJson(json, SkillModel::class.java) }

    override fun getMasteryLevel(mastery: IMastery) = model.masteryLevels[mastery.name] ?: 0
    override fun setMasteryLevel(mastery: IMastery, masteryLevel: Int) = run { model.masteryLevels[mastery.name] = masteryLevel }
}

data class SkillModel(@Expose val masteryLevels: MutableMap<String, Int> = mutableMapOf())
