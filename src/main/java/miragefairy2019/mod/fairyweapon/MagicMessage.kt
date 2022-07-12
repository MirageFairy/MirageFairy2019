package miragefairy2019.mod.fairyweapon

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.textComponent

enum class MagicMessage(val unlocalizedName: String) {
    NO_FAIRY("noFairy"),
    INSUFFICIENT_RESOURCE("insufficientResource"),
    INSUFFICIENT_DURABILITY("insufficientDurability"),
    NO_TARGET("noTarget"),
    COOL_TIME("coolTime"),
    INVALID_TARGET("insufficientDurability"),
}

val MagicMessage.displayText get() = textComponent { translate("miragefairy2019.magic.$unlocalizedName.text") }

val magicMessageModule = module {
    onMakeLang {
        enJa("miragefairy2019.magic.${MagicMessage.NO_FAIRY.unlocalizedName}.text", "You don't have a fairy", "妖精を所持していません")
        enJa("miragefairy2019.magic.${MagicMessage.INSUFFICIENT_RESOURCE.unlocalizedName}.text", "Insufficient resources", "資源が不足しています")
        enJa("miragefairy2019.magic.${MagicMessage.INSUFFICIENT_DURABILITY.unlocalizedName}.text", "Insufficient durability", "耐久値が不足しています")
        enJa("miragefairy2019.magic.${MagicMessage.NO_TARGET.unlocalizedName}.text", "There is no target", "発動対象がありません")
        enJa("miragefairy2019.magic.${MagicMessage.COOL_TIME.unlocalizedName}.text", "Cool time remains", "クールタイムが残っています")
        enJa("miragefairy2019.magic.${MagicMessage.INVALID_TARGET.unlocalizedName}.text", "This target is not supported", "非対応の対象です")
    }
}
