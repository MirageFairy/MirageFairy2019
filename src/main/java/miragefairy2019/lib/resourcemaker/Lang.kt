package miragefairy2019.lib.resourcemaker

import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.libkt.mkdirsParent
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
            file.writeText(translations.sortedBy { it.first }.join("") { (key, value) -> "$key=$value\n" })
        }
    }
}

fun ModScope.lang(key: String, en: String?, ja: String?) = onMakeLang {
    if (en != null) registerTranslation("en_us", key, en)
    if (ja != null) registerTranslation("ja_jp", key, ja)
}
