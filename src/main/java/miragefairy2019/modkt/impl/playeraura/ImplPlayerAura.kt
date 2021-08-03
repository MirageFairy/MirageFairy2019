package miragefairy2019.modkt.impl.playeraura

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairy.IItemFairy
import miragefairy2019.mod.api.fairy.IManaSet
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.modkt.api.playeraura.IClientPlayerAuraHandler
import miragefairy2019.modkt.api.playeraura.IPlayerAuraHandler
import miragefairy2019.modkt.api.playeraura.IPlayerAuraManager
import miragefairy2019.modkt.api.playeraura.IServerPlayerAuraHandler
import miragefairy2019.modkt.impl.ManaSet
import miragefairy2019.modkt.impl.copy
import miragefairy2019.modkt.impl.plus
import miragefairy2019.modkt.impl.times
import miragefairy2019.modkt.modules.playeraura.MessagePlayerAura
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.*

class PlayerAuraManager : IPlayerAuraManager {
    companion object {
        private fun getFairyType(itemStackFairy: ItemStack) = itemStackFairy.item.let { if (it is IItemFairy) it.getMirageFairy2019Fairy(itemStackFairy).get() else null }
    }

    private val playerAuraModelClient = PlayerAuraModel()

    @SideOnly(Side.CLIENT)
    override fun getClientPlayerAuraHandler() = ClientPlayerAuraHandler(this, playerAuraModelClient)

    @SideOnly(Side.CLIENT)
    override fun setClientPlayerAuraModelJson(json: String) = playerAuraModelClient.fromJson(json)

    private val playerAuraHandlersServer = mutableMapOf<String, PlayerAuraModel>()
    override fun getServerPlayerAuraHandler(player: EntityPlayerMP) = ServerPlayerAuraHandler(this, playerAuraHandlersServer.computeIfAbsent(player.cachedUniqueIdString) {
        val playerAuraModel = PlayerAuraModel()
        playerAuraModel.load(player)
        playerAuraModel
    }, player)

    override fun getGlobalFoodAura(itemStack: ItemStack): IManaSet? {
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


data class ItemStackData(@Expose val item: String, @Expose val metadata: Int)
data class FoodData(@Expose val itemStack: ItemStackData, @Expose val aura: ManaSet)
data class PlayerAuraData(@Expose val foods: List<FoodData>)

fun ItemStack.toJsonObject() = ItemStackData(item.registryName.toString(), metadata)
fun ItemStackData.fromJsonObject() = run { ItemStack(Item.getByNameOrId(item) ?: return@run ItemStack.EMPTY!!, 1, metadata) }
fun Food.toJsonObject() = FoodData(itemStack.toJsonObject(), aura.copy())
fun FoodData.fromJsonObject() = Food(itemStack.fromJsonObject(), aura)
fun PlayerAuraData.fromJsonObject() = PlayerAuraModel().also { it.fromJsonObject(this) }

class Food(val itemStack: ItemStack, val aura: IManaSet)

data class ResettableProperty<T : Any>(private val getter: () -> T) {
    private var value: T? = null
    fun getValue() = value ?: getter().also { value = it }
    fun dirty() = run { value = null }
}

class PlayerAuraModel {
    companion object {
        private val LOGGER = LogManager.getLogger(PlayerAuraModel::class.java)
    }


    private fun reset() {
        foods.clear()
        auraCache.dirty()
    }

    fun fromJsonObject(data: PlayerAuraData) {
        reset()
        data.foods.forEach { foods += it.fromJsonObject() }
        while (foods.size > 100) foods.removeLast()
    }

    fun toJsonObject() = PlayerAuraData(foods.map { it.toJsonObject() })


    private val foods = ArrayDeque<Food>()
    fun getFoods(): List<Food> = foods.toList()

    // 過去100エントリーの回複分について、それ自身のオーラにその寿命割合を乗じたものの合計
    private val auraCache = ResettableProperty { foods.mapIndexed { index, food -> food.aura * ((100 - index) / 100.0) }.fold<IManaSet, IManaSet>(ManaSet.ZERO) { a, b -> a + b } }
    val aura get() = auraCache.getValue()


    // 回復量の分だけ、その都度計算したローカルオーラをキューに追加
    fun pushFood(globalFoodAura: IManaSet, itemStack: ItemStack, healAmount: Int) {
        val itemStackNormalized = normalizeItemStack(itemStack)
        repeat(healAmount) {
            foods.addFirst(Food(itemStackNormalized, getLocalFoodAura(globalFoodAura, itemStackNormalized)))
            if (foods.size > 100) foods.removeLast()
        }
        auraCache.dirty()
    }

    private fun normalizeItemStack(itemStack: ItemStack) = if (itemStack.hasSubtypes) ItemStack(itemStack.item, 1, itemStack.metadata) else ItemStack(itemStack.item)

    // 過去100エントリー分の回復分について、それの寿命ごとにスロットを持つ
    // スロットが同じ料理で埋まっている割合に応じて100%から30%まで効果量が線形に減少する
    fun getLocalFoodAura(globalFoodAura: IManaSet, itemStackNormalized: ItemStack): IManaSet {
        fun equals(a: ItemStack, b: ItemStack) = a.item == b.item && a.metadata == b.metadata
        fun stair(n: Int) = n * (n + 1) / 2
        val saturationRate = foods.mapIndexed { index, food -> if (equals(food.itemStack, itemStackNormalized)) 100 - index else 0 }.sum() / stair(100).toDouble()
        return globalFoodAura * (1.0 - 0.7 * saturationRate)
    }


    private val gson by lazy { GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()!! }
    private fun getFile(player: EntityPlayer) = File(player.world.saveHandler.worldDirectory, "${ModMirageFairy2019.MODID}/playeraura/${player.cachedUniqueIdString}.json")

    fun toJson() = gson.toJson(toJsonObject())!!

    fun fromJson(json: String) {
        reset()
        fromJsonObject(gson.fromJson(json, PlayerAuraData::class.java))
    }

    fun load(player: EntityPlayerMP) {
        reset()
        val file = getFile(player)
        if (file.exists()) {
            try {
                fromJson(file.readText())
            } catch (e: Exception) {
                LOGGER.error("Can not load player aura:")
                LOGGER.error("  Player: ${player.name}")
                LOGGER.error("  File: $file")
                LOGGER.error(e)
                return
            }
        }
    }

    fun save(player: EntityPlayerMP) {
        val file = getFile(player)
        LOGGER.debug("Saving: " + player.cachedUniqueIdString + " > " + file)
        try {
            file.parentFile.mkdirs()
            file.writeText(toJson())
        } catch (e: Exception) {
            LOGGER.error("Can not save player aura:")
            LOGGER.error("  Player: ${player.name}")
            LOGGER.error("  File: $file")
            LOGGER.error(e)
        }
    }
}

open class PlayerAuraHandler(protected val manager: IPlayerAuraManager,
                             protected val model: PlayerAuraModel) : IPlayerAuraHandler {
    override fun getPlayerAura() = model.aura
    override fun getLocalFoodAura(itemStack: ItemStack) = manager.getGlobalFoodAura(itemStack)?.let { model.getLocalFoodAura(it, itemStack) }
}

class ClientPlayerAuraHandler(manager: IPlayerAuraManager,
                              model: PlayerAuraModel) : PlayerAuraHandler(manager, model), IClientPlayerAuraHandler

class ServerPlayerAuraHandler(manager: IPlayerAuraManager,
                              model: PlayerAuraModel,
                              private val player: EntityPlayerMP) : PlayerAuraHandler(manager, model), IServerPlayerAuraHandler {
    override fun load() = model.load(player)
    override fun save() = model.save(player)
    override fun onEat(itemStack: ItemStack, healAmount: Int) = run { manager.getGlobalFoodAura(itemStack)?.let { model.pushFood(it, itemStack, healAmount) }; Unit }
    override fun send() = ApiMain.simpleNetworkWrapper.sendTo(MessagePlayerAura(model.toJson()), player)
}
