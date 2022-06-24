package miragefairy2019.mod.systems

import miragefairy2019.api.Mana
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.enJa

val manaModule = module {
    onMakeLang {
        Mana.values().forEach {
            fun getKey(name: String) = "mirageFairy2019.mana.$name.name"
            when (it) {
                Mana.SHINE -> enJa(getKey("shine"), "Shine", "光")
                Mana.FIRE -> enJa(getKey("fire"), "Fire", "火")
                Mana.WIND -> enJa(getKey("wind"), "Wind", "風")
                Mana.GAIA -> enJa(getKey("gaia"), "Gaia", "土")
                Mana.AQUA -> enJa(getKey("aqua"), "Aqua", "水")
                Mana.DARK -> enJa(getKey("dark"), "Dark", "闇")
            }
        }
    }
}
