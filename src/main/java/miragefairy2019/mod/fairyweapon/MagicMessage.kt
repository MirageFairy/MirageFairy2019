package miragefairy2019.mod.fairyweapon

import miragefairy2019.libkt.textComponent

enum class MagicMessage(val unlocalizedName: String) {
    NO_FAIRY("noFairy"),
    INSUFFICIENT_DURABILITY("insufficientDurability"),
    NO_TARGET("noTarget"),
    COOL_TIME("coolTime"),
}

val MagicMessage.displayText get() = textComponent { translate("miragefairy2019.magic.$unlocalizedName.text") }
