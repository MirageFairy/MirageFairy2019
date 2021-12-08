package miragefairy2019.mod

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceMaker
import java.io.File

object MainMakeResource {
    @JvmStatic
    fun main(args: Array<String>) {
        ModInitializer().also { modules.forEach { module -> module(it) } }.onMakeResource(ResourceMaker(File("src/main/resources")))
    }
}
