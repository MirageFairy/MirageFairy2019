package miragefairy2019.resourcemaker

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.File

class ResourceMaker(val dirBase: File) {
    private val Any.json get() = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this) + "\n"
    fun File.place(data: JsonElement) = apply { parentFile.mkdirs() }.writeText(data.json)
}
