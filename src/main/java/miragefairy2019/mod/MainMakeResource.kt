package miragefairy2019.mod

import miragefairy2019.libkt.LangMaker
import miragefairy2019.lib.modinitializer.ModInitializer
import miragefairy2019.lib.resourcemaker.ResourceMaker
import java.io.File

object MainMakeResource {
    @JvmStatic
    fun main(args: Array<String>) {
        val modInitializer = ModInitializer(ModMirageFairy2019.MODID, true)
        modules(modInitializer)

        modInitializer.onMakeResource(ResourceMaker(File("src/main/resources")))

        val langMaker = LangMaker(File("src/main/resources/assets/miragefairy2019/lang"))
        File("src/lang").listFiles()!!.filter { it.isDirectory }.forEach { languageDir ->
            languageDir.listFiles()!!.filter { it.isFile }.forEach { langFile ->
                langFile.readText().lines().forEachIndexed ignoredLine@{ i, line ->
                    if (line.isBlank()) return@ignoredLine
                    if (line.startsWith("#")) return@ignoredLine

                    val array = line.split("=")
                    langMaker.registerTranslation(
                        languageDir.name,
                        array.getOrNull(0) ?: throw Exception("malformed line: $line (${langFile.path}:${i + 1})"),
                        array.getOrNull(1) ?: throw Exception("malformed line: $line (${langFile.path}:${i + 1})")
                    )
                }
            }
        }
        modInitializer.onMakeLang(langMaker)
        langMaker.make()

    }
}
