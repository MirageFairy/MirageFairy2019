package miragefairy2019.mod.systems

import miragefairy2019.api.Erg
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.lang

val ergModule = module {
    fun getKey(name: String) = "mirageFairy2019.erg.$name.name"
    Erg.values().forEach {
        when (it) {
            Erg.ATTACK -> lang(getKey("attack"), "ATTACK", "攻撃")
            Erg.CRAFT -> lang(getKey("craft"), "CRAFT", "加工")
            Erg.HARVEST -> lang(getKey("harvest"), "HARVEST", "収穫")
            Erg.LIGHT -> lang(getKey("light"), "LIGHT", "発光")
            Erg.FLAME -> lang(getKey("flame"), "FLAME", "火炎")
            Erg.WATER -> lang(getKey("water"), "WATER", "流水")
            Erg.CRYSTAL -> lang(getKey("crystal"), "CRYSTAL", "結晶")
            Erg.SOUND -> lang(getKey("sound"), "SOUND", "音波")
            Erg.SPACE -> lang(getKey("space"), "SPACE", "空間")
            Erg.WARP -> lang(getKey("warp"), "WARP", "縮地")
            Erg.KINESIS -> lang(getKey("kinesis"), "KINESIS", "運動")
            Erg.DESTROY -> lang(getKey("destroy"), "DESTROY", "破壊")
            Erg.CHEMICAL -> lang(getKey("chemical"), "CHEMICAL", "化学")
            Erg.SLASH -> lang(getKey("slash"), "SLASH", "切断")
            Erg.LIFE -> lang(getKey("life"), "LIFE", "生命")
            Erg.KNOWLEDGE -> lang(getKey("knowledge"), "KNOWLEDGE", "知識")
            Erg.ENERGY -> lang(getKey("energy"), "ENERGY", "熱量")
            Erg.SUBMISSION -> lang(getKey("submission"), "SUBMISSION", "服従")
            Erg.CHRISTMAS -> lang(getKey("christmas"), "CHRISTMAS", "聖夜")
            Erg.FREEZE -> lang(getKey("freeze"), "FREEZE", "氷結")
            Erg.THUNDER -> lang(getKey("thunder"), "ELECTRIC", "雷電")
            Erg.LEVITATE -> lang(getKey("levitate"), "LEVITATE", "浮揚")
            Erg.SENSE -> lang(getKey("sense"), "SENSE", "感覚")
        }
    }
}
