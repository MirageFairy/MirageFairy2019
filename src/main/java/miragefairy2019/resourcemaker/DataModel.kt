package miragefairy2019.resourcemaker

import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull


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

data class DataItemModel(
    val parent: String,
    val elements: List<DataElement>? = null,
    val textures: Map<String, String>? = null
) {
    val jsonElement = jsonObjectNotNull(
        "parent" to parent.jsonElement,
        "elements" to elements?.map { it.jsonElement }?.jsonArray,
        "textures" to textures?.mapValues { (_, it) -> it.jsonElement }?.jsonObject
    )
}


data class DataElement(
    val from: DataPoint,
    val to: DataPoint,
    val shade: Boolean? = null,
    val faces: DataFaces
) {
    val jsonElement
        get() = jsonObjectNotNull(
            "from" to from.jsonElement,
            "to" to to.jsonElement,
            "shade" to shade?.jsonElement,
            "faces" to faces.jsonElement
        )
}

data class DataPoint(
    val x: Double,
    val y: Double,
    val z: Double
) {
    val jsonElement get() = jsonArray(x.jsonElement, y.jsonElement, z.jsonElement)
}

data class DataFaces(
    val down: DataFace? = null,
    val up: DataFace? = null,
    val north: DataFace? = null,
    val south: DataFace? = null,
    val west: DataFace? = null,
    val east: DataFace? = null
) {
    val jsonElement
        get() = jsonObjectNotNull(
            "down" to down?.jsonElement,
            "up" to up?.jsonElement,
            "north" to north?.jsonElement,
            "south" to south?.jsonElement,
            "west" to west?.jsonElement,
            "east" to east?.jsonElement
        )
}

data class DataFace(
    val uv: DataUv? = null,
    val texture: String,
    val tintindex: Int? = null,
    val cullface: String? = null,
    val rotation: Int? = null
) {
    val jsonElement
        get() = jsonObjectNotNull(
            "uv" to uv?.jsonElement,
            "texture" to texture.jsonElement,
            "tintindex" to tintindex?.jsonElement,
            "cullface" to cullface?.jsonElement,
            "rotation" to rotation?.jsonElement
        )
}

data class DataUv(
    val u1: Double,
    val v1: Double,
    val u2: Double,
    val v2: Double
) {
    val jsonElement get() = jsonArray(u1.jsonElement, v1.jsonElement, u2.jsonElement, v2.jsonElement)
}
