package miragefairy2019.mod3.mana

import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod3.mana.api.EnumManaType

val EnumManaType.displayName get() = buildText { translate("mirageFairy2019.mana.$name.name").color(textColor) }
