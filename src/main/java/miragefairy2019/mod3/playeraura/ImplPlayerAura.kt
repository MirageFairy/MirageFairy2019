package miragefairy2019.mod3.playeraura

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import miragefairy2019.api.IFoodAuraItem
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.plus
import miragefairy2019.lib.times
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.fairy.relation.FairySelector
import miragefairy2019.mod3.fairy.relation.primaries
import miragefairy2019.mod3.fairy.relation.withoutPartiallyMatch
import miragefairy2019.mod3.main.Main
import miragefairy2019.mod3.playeraura.api.IClientPlayerAuraHandler
import miragefairy2019.mod3.playeraura.api.IFoodHistoryEntry
import miragefairy2019.mod3.playeraura.api.IPlayerAuraHandler
import miragefairy2019.mod3.playeraura.api.IPlayerAuraManager
import miragefairy2019.mod3.playeraura.api.IServerPlayerAuraHandler
import mirrg.kotlin.gson.JsonWrapper
import mirrg.kotlin.gson.jsonElement
import mirrg.kotlin.gson.jsonWrapper
import mirrg.kotlin.gson.toDouble
import mirrg.kotlin.gson.toInt
import mirrg.kotlin.gson.toString
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.ArrayDeque

class PlayerAuraManager : IPlayerAuraManager {
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

    override fun getGlobalFoodAura(itemStack: ItemStack): ManaSet? {
        val item = itemStack.item

        if (item !is ItemFood) return null // 食べ物以外は無視

        if (item is IFoodAuraItem) {
            return item.getFoodAura(itemStack) // カスタムフードオーラ
        } else {

            // アイテムスタックに紐づけられた妖精のリスト
            val entries = FairySelector().itemStack(itemStack).allMatch().withoutPartiallyMatch.primaries
            if (entries.isEmpty()) return null // 関連付けられた妖精が居ない場合は無視

            // 平均を返す
            fun f(typeChooser: (ManaSet) -> Double) = entries.map { typeChooser(it.fairy.main.type.manaSet) / it.fairy.main.type.cost * 50 * 0.5 }.average()
            return ManaSet(f { it.shine }, f { it.fire }, f { it.wind }, f { it.gaia }, f { it.aqua }, f { it.dark })
        }
    }

    override fun unloadAllServerPlayerAuraHandlers() = playerAuraHandlersServer.clear()
}


// Data Model

fun ItemStack.toJsonElement() = jsonElement("item" to item.registryName.toString().jsonElement, "metadata" to metadata.jsonElement)

fun JsonWrapper.toItemStack(): ItemStack {
    val item = Item.getByNameOrId(this["item"].toString) ?: return EMPTY_ITEM_STACK
    val metadata = this["metadata"].toInt
    return ItemStack(item, 1, metadata)
}

fun ManaSet.toJsonElement() = jsonElement(
    "shine" to shine.jsonElement,
    "fire" to fire.jsonElement,
    "wind" to wind.jsonElement,
    "gaia" to gaia.jsonElement,
    "aqua" to aqua.jsonElement,
    "dark" to dark.jsonElement
)

fun JsonWrapper.toManaSet() = ManaSet(
    this["shine"].toDouble,
    this["fire"].toDouble,
    this["wind"].toDouble,
    this["gaia"].toDouble,
    this["aqua"].toDouble,
    this["dark"].toDouble
)

class Food(val itemStack: ItemStack, val aura: ManaSet) {
    fun copy() = Food(itemStack.copy(), aura)
}

fun Food.toJsonElement() = jsonElement("itemStack" to itemStack.toJsonElement(), "aura" to aura.toJsonElement())

fun JsonWrapper.toFood() = Food(this["itemStack"].toItemStack(), this["aura"].toManaSet())

class PlayerAuraData(val foods: List<Food>)

fun PlayerAuraData.toJsonElement() = jsonElement("foods" to jsonElement(foods.map { it.toJsonElement() }))

fun JsonWrapper.toPlayerAuraData() = PlayerAuraData(this["foods"].asList.map { it.toFood() })


data class ResettableProperty<T : Any>(private val getter: () -> T) {
    private var value: T? = null
    fun getValue() = value ?: getter().also { value = it }
    fun dirty() = run { value = null }
}

class PlayerAuraModel {
    companion object {
        private val LOGGER = LogManager.getLogger(PlayerAuraModel::class.java)
    }


    private val lock = Any()
    fun reset() {
        foods.clear()
        auraCache.dirty()
    }

    fun setData(data: PlayerAuraData) {
        synchronized(lock) {
            reset()
            data.foods.forEach { foods += it.copy() }
            while (foods.size > 100) foods.removeLast()
        }
    }

    fun getData() = synchronized(lock) { PlayerAuraData(foods.map { it.copy() }) }
    fun copy() = synchronized(lock) { PlayerAuraModel().also { model -> foods.forEach { model.foods.add(it.copy()) } } }


    private val foods = ArrayDeque<Food>()
    val foodHistory get() = synchronized(lock) { foods.mapIndexed { i, food -> FoodHistoryEntry(food.itemStack, food.aura, (100 - i) / 100.0) } }

    // 過去100エントリーの回複分について、それ自身のオーラにその寿命割合を乗じたものの合計
    private val auraCache = ResettableProperty { foods.mapIndexed { i, food -> food.aura * ((100 - i) / 100.0) }.fold<ManaSet, ManaSet>(ManaSet.ZERO) { a, b -> a + b } * (1 / 100.0) * 8.0 }
    val aura get() = synchronized(lock) { auraCache.getValue() }


    // 回復量の分だけ、その都度計算したローカルオーラをキューに追加
    fun pushFood(globalFoodAura: ManaSet, itemStack: ItemStack, healAmount: Int) {
        synchronized(lock) {
            val itemStackNormalized = normalizeItemStack(itemStack)
            repeat(healAmount) {
                foods.addFirst(Food(itemStackNormalized, getLocalFoodAura(globalFoodAura, itemStackNormalized)))
                if (foods.size > 100) foods.removeLast()
            }
            auraCache.dirty()
        }
    }

    private fun normalizeItemStack(itemStack: ItemStack) = if (itemStack.hasSubtypes) ItemStack(itemStack.item, 1, itemStack.metadata) else ItemStack(itemStack.item)

    // 過去100エントリー分の回復分について、それの寿命ごとにスロットを持つ
    fun getSaturationRate(itemStack: ItemStack): Double {
        synchronized(lock) {
            fun equals(a: ItemStack, b: ItemStack) = a.item == b.item && a.metadata == b.metadata
            fun stair(n: Int) = n * (n + 1) / 2
            val itemStackNormalized = normalizeItemStack(itemStack)
            return foods.mapIndexed { index, food -> if (equals(food.itemStack, itemStackNormalized)) 100 - index else 0 }.sum() / stair(100).toDouble()
        }
    }

    // スロットが同じ料理で埋まっている割合に応じて100%から30%まで効果量が線形に減少する
    fun getLocalFoodAura(globalFoodAura: ManaSet, itemStack: ItemStack) = synchronized(lock) { globalFoodAura * (1.0 - 0.7 * getSaturationRate(itemStack)) }


    private val gson by lazy { GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()!! }
    private fun getFile(player: EntityPlayer) = File(player.world.minecraftServer!!.getWorld(0).saveHandler.worldDirectory, "${ModMirageFairy2019.MODID}/playeraura/${player.cachedUniqueIdString}.json")

    fun toJson() = synchronized(lock) { gson.toJson(getData().toJsonElement())!! }

    fun fromJson(json: String) {
        synchronized(lock) {
            reset()
            setData(gson.fromJson(json, JsonElement::class.java).jsonWrapper.toPlayerAuraData())
        }
    }

    fun load(player: EntityPlayerMP) {
        synchronized(lock) {
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
    }

    fun save(player: EntityPlayerMP) {
        synchronized(lock) {
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
}

class FoodHistoryEntry(private val food: ItemStack, private val baseLocalFoodAura: ManaSet?, private val health: Double) : IFoodHistoryEntry {
    override fun getFood() = food
    override fun getBaseLocalFoodAura() = baseLocalFoodAura
    override fun getHealth() = health
}

open class PlayerAuraHandler(
    protected val manager: IPlayerAuraManager,
    protected val model: PlayerAuraModel
) : IPlayerAuraHandler {
    override fun getPlayerAura() = model.aura
    override fun getLocalFoodAura(itemStack: ItemStack) = manager.getGlobalFoodAura(itemStack)?.let { model.getLocalFoodAura(it, itemStack) }
    override fun getSaturationRate(itemStack: ItemStack) = model.getSaturationRate(itemStack)
    override fun simulatePlayerAura(itemStack: ItemStack, healAmount: Int): ManaSet? = manager.getGlobalFoodAura(itemStack)?.let {
        val model2 = model.copy()
        model2.pushFood(it, itemStack, healAmount)
        model2.aura
    }

    override fun getFoodHistory() = model.foodHistory
}

class ClientPlayerAuraHandler(
    manager: IPlayerAuraManager,
    model: PlayerAuraModel
) : PlayerAuraHandler(manager, model), IClientPlayerAuraHandler

class ServerPlayerAuraHandler(
    manager: IPlayerAuraManager,
    model: PlayerAuraModel,
    private val player: EntityPlayerMP
) : PlayerAuraHandler(manager, model), IServerPlayerAuraHandler {
    override fun load() = model.load(player)
    override fun save() = model.save(player)
    override fun onReset() = model.reset()
    override fun onEat(itemStack: ItemStack, healAmount: Int) = run { manager.getGlobalFoodAura(itemStack)?.let { model.pushFood(it, itemStack, healAmount) }; Unit }
    override fun send() = Main.simpleNetworkWrapper.sendTo(MessagePlayerAura(model.toJson()), player)
}
