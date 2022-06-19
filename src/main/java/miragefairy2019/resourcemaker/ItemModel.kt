package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.map
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull


fun ModInitializer.makeItemModel(path: String, creator: MakeItemModelScope.() -> DataItemModel) = onMakeResource {
    place("assets/$modId/models/item/$path.json", MakeItemModelScope(ResourceName(modId, path)).creator().jsonElement)
}

class MakeItemModelScope(val resourceName: ResourceName)


data class DataItemModel(
    val parent: String,
    val elements: JsonElement? = null,
    val textures: Map<String, String>? = null
) {
    val jsonElement = jsonObjectNotNull(
        "parent" to parent.jsonElement,
        "elements" to elements,
        "textures" to textures?.mapValues { (_, it) -> it.jsonElement }?.jsonObject
    )
}


val MakeItemModelScope.generated
    get() = DataItemModel(
        parent = "item/generated",
        textures = mapOf(
            "layer0" to "${resourceName.map { "items/$it" }}"
        )
    )
val MakeItemModelScope.handheld
    get() = DataItemModel(
        parent = "item/handheld",
        textures = mapOf(
            "layer0" to "${resourceName.map { "items/$it" }}"
        )
    )
val MakeItemModelScope.block
    get() = DataItemModel(
        parent = "${resourceName.map { "block/$it" }}"
    )
val MakeItemModelScope.fluid
    get() = DataItemModel(
        parent = "item/generated",
        textures = mapOf(
            "layer0" to "${resourceName.map { "blocks/${it}_still" }}"
        )
    )
