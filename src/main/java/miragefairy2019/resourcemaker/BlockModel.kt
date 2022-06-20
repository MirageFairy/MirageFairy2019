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
    val ambientOcclusion: Boolean? = null,
    val elements: List<DataElement>? = null,
    val textures: Map<String, String>? = null
) {
    val jsonElement
        get() = jsonObjectNotNull(
            "parent" to parent?.jsonElement,
            "ambientocclusion" to ambientOcclusion?.jsonElement,
            "elements" to elements?.map { it.jsonElement }?.jsonArray,
            "textures" to textures?.mapValues { (_, it) -> it.jsonElement }?.jsonObject
        )
}

data class DataElement(
    val from: DataPoint,
    val to: DataPoint,
    val shade: Boolean? = null,
    val faces: JsonElement
) {
    val jsonElement
        get() = jsonObjectNotNull(
            "from" to from.jsonElement,
            "to" to to.jsonElement,
            "shade" to shade?.jsonElement,
            "faces" to faces
        )
}

data class DataPoint(
    val x: Double,
    val y: Double,
    val z: Double
) {
    val jsonElement get() = jsonArray(x.jsonElement, y.jsonElement, z.jsonElement)
}
