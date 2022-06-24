package miragefairy2019.lib.resourcemaker

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import mirrg.kotlin.java.hydrogen.mkdirsOrThrow
import java.io.File

class ResourceMaker(val dirBase: File)

fun ResourceMaker.place(path: String, data: JsonElement) = dirBase.resolve(path).also { it.parentFile.mkdirsOrThrow() }.writeText(GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(data) + "\n")
