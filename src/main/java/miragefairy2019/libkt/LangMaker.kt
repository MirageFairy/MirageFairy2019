package miragefairy2019.libkt

import miragefairy2019.lib.resourcemaker.LangMaker

fun LangMaker.en(key: String, value: String) = registerTranslation("en_us", key, value)
fun LangMaker.ja(key: String, value: String) = registerTranslation("ja_jp", key, value)
fun LangMaker.enJa(key: String, english: String, japanese: String) {
    en(key, english)
    ja(key, japanese)
}
