package miragefairy2019.libkt

import miragefairy2019.lib.resourcemaker.LangMaker

fun LangMaker.enJa(key: String, english: String?, japanese: String?) {
    if (english != null) registerTranslation("en_us", key, english)
    if (japanese != null) registerTranslation("ja_jp", key, japanese)
}
