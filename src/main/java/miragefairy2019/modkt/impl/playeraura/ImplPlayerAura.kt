package miragefairy2019.modkt.impl.playeraura

import com.google.gson.Gson
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairy.IItemFairy
import miragefairy2019.mod.api.fairy.IManaSet
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.json.JsonExtractor
import miragefairy2019.modkt.api.playeraura.IPlayerAura
import miragefairy2019.modkt.api.playeraura.IPlayerAuraManager
import miragefairy2019.modkt.impl.ManaSet
import miragefairy2019.modkt.impl.MutableManaSet
import miragefairy2019.modkt.impl.copy
import miragefairy2019.modkt.modules.playeraura.MessagePlayerAura
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.IOException

class PlayerAuraManager : IPlayerAuraManager {
    companion object {
        private fun getFairyType(itemStackFairy: ItemStack) = itemStackFairy.item.let { if (it is IItemFairy) it.getMirageFairy2019Fairy(itemStackFairy).get() else null }
    }

    private val playerAuraClient = PlayerAura(this)
    private val mapServer: MutableMap<String, PlayerAura> = HashMap()
    override fun getClientPlayerAura() = playerAuraClient
    override fun getServerPlayerAura(player: EntityPlayer) = mapServer.computeIfAbsent(player.cachedUniqueIdString) { PlayerAura(this).also { it.load(player) } }

    override fun getFoodAura(itemStack: ItemStack): IManaSet? {
        val listFairyRelation = ApiFairy.fairyRelationRegistry.ingredientFairyRelations.toList()
                .filter { it.ingredient.test(itemStack) }
                .filter { it.relevance >= 1 }
        if (listFairyRelation.isEmpty()) return null
        val relevanceMax = listFairyRelation.map { it.relevance }.max()!!
        val listFairyRelationMax = listFairyRelation.filter { it.relevance == relevanceMax }.mapNotNull { getFairyType(it.itemStackFairy) }
        fun f(typeChooser: (IManaSet) -> Double) = listFairyRelationMax.map { typeChooser(it.manas) / it.cost * 50 * 0.5 }.average()
        return ManaSet(f { it.shine }, f { it.fire }, f { it.wind }, f { it.gaia }, f { it.aqua }, f { it.dark })
    }
}

class PlayerAura(private val playerAuraManager: PlayerAuraManager) : IPlayerAura {
    companion object {
        private val LOGGER = LogManager.getLogger(PlayerAura::class.java)
    }

    private fun init() = aura.set(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    private val aura = MutableManaSet()
    override fun getAura() = aura
    override fun setAura(aura: IManaSet) = this.aura.set(aura)

    // TODO 摂食履歴による効果低減
    override fun getFoodAura(itemStack: ItemStack) = playerAuraManager.getFoodAura(itemStack)?.copy()

    //

    private fun getFile(player: EntityPlayer) = File(player.world.saveHandler.worldDirectory, "${ModMirageFairy2019.MODID}/playeraura/${player.cachedUniqueIdString}.json")

    override fun load(player: EntityPlayer) {
        init()
        getFile(player).takeIf { it.exists() }?.let {
            fromJson(Gson().fromJson(try {
                it.readText()
            } catch (e: IOException) {
                LOGGER.error("Can not load player aura: " + player.name, e)
                return
            }, Any::class.java))
        }
    }

    private fun fromJson(any: Any?) {
        JsonExtractor(any).asMap()
                .get("aura") { mapAura ->
                    mapAura.asMap()
                            .get("shine") { value -> aura.shine = value.toInt(0).toDouble() }
                            .get("fire") { value -> aura.fire = value.toInt(0).toDouble() }
                            .get("wind") { value -> aura.wind = value.toInt(0).toDouble() }
                            .get("gaia") { value -> aura.gaia = value.toInt(0).toDouble() }
                            .get("aqua") { value -> aura.aqua = value.toInt(0).toDouble() }
                            .get("dark") { value -> aura.dark = value.toInt(0).toDouble() }
                }
    }

    override fun save(player: EntityPlayer) {
        LOGGER.debug("Saving: " + player.cachedUniqueIdString + " > " + getFile(player))
        try {
            getFile(player).also { it.parentFile.mkdirs() }.writeText(Gson().toJson(toJson()))
        } catch (e: IOException) {
            LOGGER.error("Can not save player aura: " + player.name, e)
            LOGGER.error(toJson())
        }
    }

    private fun toJson(): Any = mapOf("aura" to mapOf("shine" to aura.shine, "fire" to aura.fire, "wind" to aura.wind, "gaia" to aura.gaia, "aqua" to aura.aqua, "dark" to aura.dark))

    //

    // TODO 摂食履歴システム　連続で同じアイテムを食べると効果が下がっていく
    override fun onEat(player: EntityPlayerMP, itemStack: ItemStack, healAmount: Int) {
        aura.set(playerAuraManager.getFoodAura(itemStack) ?: return)
        send(player)
    }

    override fun send(player: EntityPlayerMP) = ApiMain.simpleNetworkWrapper.sendTo(MessagePlayerAura(aura), player)

}
