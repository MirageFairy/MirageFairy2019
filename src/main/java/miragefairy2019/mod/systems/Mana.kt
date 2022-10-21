package miragefairy2019.mod.systems

import miragefairy2019.api.Mana
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.lang

val manaModule = module {
    fun getKey(name: String) = "mirageFairy2019.mana.$name.name"
    Mana.values().forEach {
        when (it) {
            Mana.SHINE -> lang(getKey("shine"), "Shine", "光")
            Mana.FIRE -> lang(getKey("fire"), "Fire", "火")
            Mana.WIND -> lang(getKey("wind"), "Wind", "風")
            Mana.GAIA -> lang(getKey("gaia"), "Gaia", "土")
            Mana.AQUA -> lang(getKey("aqua"), "Aqua", "水")
            Mana.DARK -> lang(getKey("dark"), "Dark", "闇")
        }
    }
}
