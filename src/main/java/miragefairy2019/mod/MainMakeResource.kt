package miragefairy2019.mod

import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.resourcemaker.LangMaker
import miragefairy2019.lib.resourcemaker.ResourceMaker
import miragefairy2019.lib.resourcemaker.place
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import java.io.File

object MainMakeResource {
    @JvmStatic
    fun main(args: Array<String>) {
        val modScope = ModScope(ModMirageFairy2019.MODID, true)
        modules(modScope)

        val ingredientMap = mutableMapOf<String, Class<*>>()
        modScope.onMakeIngredientFactory(ingredientMap)
        modScope.onMakeResource {
            val data = jsonObject(
                "ingredients" to ingredientMap.map { it.key to it.value.canonicalName.jsonElement }.jsonObject
            )
            place("assets/${ModMirageFairy2019.MODID}/recipes/_factories.json", data)
        }

        modScope.onMakeResource(ResourceMaker(File("src/main/resources")))

        val langMaker = LangMaker(File("src/main/resources/assets/miragefairy2019/lang"))
        modScope.onMakeLang(langMaker)
        langMaker.make()

    }
}
