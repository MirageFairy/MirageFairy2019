package miragefairy2019.libkt

import mirrg.kotlin.hydrogen.join
import java.io.File

class LangMaker(val langDir: File) {
    private val langs = mutableMapOf<String, MutableList<Pair<String, String>>>()
    fun registerTranslation(language: String, key: String, value: String) {
        langs.computeIfAbsent(language) { mutableListOf() } += key to value
    }

    fun make() {
        langs.entries.forEach { (language, translations) ->
            val file = langDir.resolve("$language.lang")
            file.mkdirsParent()
            file.writeText(translations.join("") { (key, value) -> "$key=$value\n" })
        }
    }
}

fun LangMaker.en(key: String, value: String) = registerTranslation("en_us", key, value)
fun LangMaker.ja(key: String, value: String) = registerTranslation("ja_jp", key, value)
fun LangMaker.enJa(key: String, english: String, japanese: String) {
    en(key, english)
    ja(key, japanese)
}
