package miragefairy2019.mod.modules.fairy

import miragefairy2019.mod.lib.multi.ItemVariant
import miragefairy2019.modkt.api.fairy.FairyType
import miragefairy2019.modkt.impl.fairy.ColorSet

class VariantFairy(val id: Int, val colorSet: ColorSet, val type: FairyType, val rare: Int, val rank: Int) : ItemVariant()
