package miragefairy2019.mod

import miragefairy2019.lib.modinitializer.ModInitializer
import miragefairy2019.lib.resourcemaker.ResourceMaker
import miragefairy2019.lib.resourcemaker.place
import miragefairy2019.libkt.LangMaker
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import java.io.File

object MainMakeResource {
    @JvmStatic
    fun main(args: Array<String>) {
        val modInitializer = ModInitializer(ModMirageFairy2019.MODID, true)
        modules(modInitializer)

        val ingredientMap = mutableMapOf<String, Class<*>>()
        modInitializer.onMakeIngredientFactory(ingredientMap)
        modInitializer.onMakeResource {
            val data = jsonObject(
                "ingredients" to ingredientMap.map { it.key to it.value.canonicalName.jsonElement }.jsonObject
            )
            place("assets/${ModMirageFairy2019.MODID}/recipes/_factories.json", data)
        }

        modInitializer.onMakeResource(ResourceMaker(File("src/main/resources")))

        val langMaker = LangMaker(File("src/main/resources/assets/miragefairy2019/lang"))
        modInitializer.onMakeLang(langMaker)
        langMaker.make()

    }
}
