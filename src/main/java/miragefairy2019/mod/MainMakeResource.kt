package miragefairy2019.mod

import miragefairy2019.lib.modinitializer.ModInitializer
import miragefairy2019.lib.resourcemaker.ResourceMaker
import miragefairy2019.libkt.LangMaker
import java.io.File

object MainMakeResource {
    @JvmStatic
    fun main(args: Array<String>) {
        val modInitializer = ModInitializer(ModMirageFairy2019.MODID, true)
        modules(modInitializer)

        modInitializer.onMakeResource(ResourceMaker(File("src/main/resources")))

        val langMaker = LangMaker(File("src/main/resources/assets/miragefairy2019/lang"))
        modInitializer.onMakeLang(langMaker)
        langMaker.make()

    }
}
