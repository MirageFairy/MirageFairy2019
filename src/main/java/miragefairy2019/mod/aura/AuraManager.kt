package miragefairy2019.mod.aura

import com.google.gson.JsonElement
import miragefairy2019.api.Erg
import miragefairy2019.api.ErgSet
import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.ClientPlayerProxy
import miragefairy2019.lib.ExtraPlayerStatusManager
import miragefairy2019.lib.ExtraPlayerStatusMessage
import miragefairy2019.lib.ExtraPlayerStatusMessageHandler
import miragefairy2019.lib.PlayerProxy
import miragefairy2019.lib.ServerPlayerProxy
import miragefairy2019.lib.entries
import miragefairy2019.lib.get
import miragefairy2019.lib.plus
import miragefairy2019.mod.Main
import miragefairy2019.mod.PacketId
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.toJsonWrapper
import mirrg.kotlin.hydrogen.or
import kotlin.math.log
import kotlin.math.pow

val auraManager = AuraManager()

val PlayerProxy.auraData
    get() = when (this) {
        is ClientPlayerProxy -> auraManager.getClientData()
        is ServerPlayerProxy -> auraManager.getServerData(this.player)
    }


class AuraManager : ExtraPlayerStatusManager<AuraMessageHandler, AuraMessage, AuraData>() {

    override val name get() = "aura"

    override val simpleNetworkWrapper get() = Main.simpleNetworkWrapper

    override val discriminator get() = PacketId.aura

    override val messageHandlerClass get() = AuraMessageHandler::class.java

    override val messageClass get() = AuraMessage::class.java

    override fun createMessage() = AuraMessage()

    override fun createData() = AuraData(AuraSet(ManaSet.ZERO, ErgSet.ZERO))

    override fun toJson(data: AuraData): JsonElement {
        return jsonObject(
            "aura_set" to jsonObject(
                "mana" to jsonObject(
                    "shine" to data.auraSet[Mana.SHINE].jsonElement,
                    "fire" to data.auraSet[Mana.FIRE].jsonElement,
                    "wind" to data.auraSet[Mana.WIND].jsonElement,
                    "gaia" to data.auraSet[Mana.GAIA].jsonElement,
                    "aqua" to data.auraSet[Mana.AQUA].jsonElement,
                    "dark" to data.auraSet[Mana.DARK].jsonElement
                ),
                "erg" to data.auraSet.erg.entries.map { (erg, value) -> "${erg.ordinal}" to value.jsonElement }.jsonObject
            )
        )
    }

    override fun fromJson(jsonElement: JsonElement?): AuraData {
        val root = jsonElement.toJsonWrapper()
        return AuraData(
            AuraSet(
                ManaSet(
                    root["aura_set"].orNull?.get("mana")?.orNull?.get("shine")?.orNull?.asDouble().or { 0.0 },
                    root["aura_set"].orNull?.get("mana")?.orNull?.get("fire")?.orNull?.asDouble().or { 0.0 },
                    root["aura_set"].orNull?.get("mana")?.orNull?.get("wind")?.orNull?.asDouble().or { 0.0 },
                    root["aura_set"].orNull?.get("mana")?.orNull?.get("gaia")?.orNull?.asDouble().or { 0.0 },
                    root["aura_set"].orNull?.get("mana")?.orNull?.get("aqua")?.orNull?.asDouble().or { 0.0 },
                    root["aura_set"].orNull?.get("mana")?.orNull?.get("dark")?.orNull?.asDouble().or { 0.0 }
                ),
                ErgSet(
                    root["aura_set"].orNull?.get("erg")?.asMap().or { mapOf() }.entries.mapNotNull { (ergIndex, value) ->
                        val erg = Erg.values().getOrNull(ergIndex.toInt()) ?: return@mapNotNull null
                        erg to value.asDouble()
                    }.toMap()
                )
            )
        )
    }

}

class AuraMessageHandler : ExtraPlayerStatusMessageHandler<AuraMessage, AuraData>() {
    override val manager get() = auraManager
}

class AuraMessage : ExtraPlayerStatusMessage()

class AuraData(
    var auraSet: AuraSet
)


class AuraSet(val mana: ManaSet, val erg: ErgSet)

operator fun AuraSet.get(mana: Mana) = this.mana[mana]
operator fun AuraSet.get(erg: Erg) = this.erg[erg]
operator fun AuraSet.plus(other: AuraSet) = AuraSet(mana + other.mana, erg + other.erg)

fun AuraSet.getPower() = (this.mana.entries.map { it.second } + this.erg.entries.map { it.second }) // 値のリスト
    .map { log((20.0 + it) / 20.0, 10.0) * 8.0 } // 全く無いときにゼロ、約10増やすと100、約100増やすと200
    .sortedDescending() // 降順にする
    .mapIndexed { i, value -> value * 0.9.pow(i.toDouble()) } // 2位は0.9倍、3位は0.9*0.9倍…
    .sum()
