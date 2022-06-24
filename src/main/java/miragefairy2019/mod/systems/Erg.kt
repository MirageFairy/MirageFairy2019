package miragefairy2019.mod.systems

import miragefairy2019.api.Erg
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.enJa

val ergModule = module {
    onMakeLang {
        Erg.values().forEach {
            fun getKey(name: String) = "mirageFairy2019.erg.$name.name"
            when (it) {
                Erg.ATTACK -> enJa(getKey("attack"), "ATTACK", "攻撃")
                Erg.CRAFT -> enJa(getKey("craft"), "CRAFT", "加工")
                Erg.HARVEST -> enJa(getKey("harvest"), "HARVEST", "収穫")
                Erg.LIGHT -> enJa(getKey("light"), "LIGHT", "発光")
                Erg.FLAME -> enJa(getKey("flame"), "FLAME", "火炎")
                Erg.WATER -> enJa(getKey("water"), "WATER", "流水")
                Erg.CRYSTAL -> enJa(getKey("crystal"), "CRYSTAL", "結晶")
                Erg.SOUND -> enJa(getKey("sound"), "SOUND", "音波")
                Erg.SPACE -> enJa(getKey("space"), "SPACE", "空間")
                Erg.WARP -> enJa(getKey("warp"), "WARP", "縮地")
                Erg.SHOOT -> enJa(getKey("shoot"), "PROJECTION", "射出")
                Erg.DESTROY -> enJa(getKey("destroy"), "DESTROY", "破壊")
                Erg.CHEMICAL -> enJa(getKey("chemical"), "CHEMICAL", "化学")
                Erg.SLASH -> enJa(getKey("slash"), "SLASH", "切断")
                Erg.LIFE -> enJa(getKey("life"), "LIFE", "生命")
                Erg.KNOWLEDGE -> enJa(getKey("knowledge"), "KNOWLEDGE", "知識")
                Erg.ENERGY -> enJa(getKey("energy"), "ENERGY", "熱量")
                Erg.SUBMISSION -> enJa(getKey("submission"), "SUBMISSION", "服従")
                Erg.CHRISTMAS -> enJa(getKey("christmas"), "CHRISTMAS", "聖夜")
                Erg.FREEZE -> enJa(getKey("freeze"), "FREEZE", "氷結")
                Erg.THUNDER -> enJa(getKey("thunder"), "ELECTRIC", "雷電")
                Erg.LEVITATE -> enJa(getKey("levitate"), "LEVITATE", "浮揚")
                Erg.SENSE -> enJa(getKey("sense"), "SENSE", "感覚")
            }
        }
    }
}
