package miragefairy2019.libkt

import java.io.File

class ResourceMaker(val dirBase: File) {
    private val String.replaceIndents get() = this.replace("""^( +)""".toRegex(RegexOption.MULTILINE)) { "\t".repeat(it.groups[1]!!.value.length / 2) }
    private val String.replaceSymbols get() = this.replace("""\\u003d""".toRegex(), "=")
    private val String.format get() = this.replaceIndents.replaceSymbols + "\n"
    private val Any.json get() = GsonBuilder().setPrettyPrinting().create().toJson(this).format
    fun File.place(data: Any) = apply { parentFile.mkdirs() }.writeText(data.json)
}
