package miragefairy2019.mod.fairyweapon

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.libkt.textComponent

enum class MagicMessage(val unlocalizedName: String, val englishText: String, val japaneseText: String) {
    NO_FAIRY("noFairy", "You don't have a fairy", "妖精を所持していません"),
    INSUFFICIENT_RESOURCE("insufficientResource", "Insufficient resources", "資源が不足しています"),
    INSUFFICIENT_DURABILITY("insufficientDurability", "Insufficient durability", "耐久値が不足しています"),
    NO_TARGET("noTarget", "There is no target", "発動対象がありません"),
    COOL_TIME("coolTime", "Cool time remains", "クールタイムが残っています"),
    INVALID_TARGET("invalidTarget", "This target is not supported", "非対応の対象です"),
}

val MagicMessage.displayText get() = textComponent { translate("miragefairy2019.magic.$unlocalizedName.text") }

val magicMessageModule = module {
    MagicMessage.values().forEach { magicMessage ->
        lang("miragefairy2019.magic.${magicMessage.unlocalizedName}.text", magicMessage.englishText, magicMessage.japaneseText)
    }
}
