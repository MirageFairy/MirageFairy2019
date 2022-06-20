package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull

fun ModInitializer.makeBlockModel(path: String, creator: () -> DataBlockModel) = onMakeResource {
    place("assets/$modId/models/block/$path.json", creator().jsonElement)
}


data class DataBlockModel(
    val parent: String? = null,
    val ambientocclusion: Boolean? = null,
    val elements: List<JsonElement>? = null,
    val textures: Map<String, String>? = null
) {
    val jsonElement
        get() = jsonObjectNotNull(
            "parent" to parent?.jsonElement,
            "ambientocclusion" to ambientocclusion?.jsonElement,
            "elements" to elements?.jsonArray,
            "textures" to textures?.mapValues { (_, it) -> it.jsonElement }?.jsonObject
        )
}
