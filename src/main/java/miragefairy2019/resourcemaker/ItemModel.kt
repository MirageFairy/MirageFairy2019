package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.map
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull


fun ModInitializer.makeItemModel(path: String, creator: MakeItemModelScope.() -> JsonElement) = onMakeResource {
    place("assets/$modId/models/item/${path}.json", MakeItemModelScope(ResourceName(modId, path)).creator())
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


val MakeItemModelScope.generated get() = generated(resourceName.map { "items/$it" })
val MakeItemModelScope.handheld get() = handheld(resourceName.map { "items/$it" })
val MakeItemModelScope.block get() = block(resourceName.map { "block/$it" })
val MakeItemModelScope.fluid get() = generated(resourceName.map { "blocks/${it}_still" })

private fun generated(resourceName: ResourceName) = jsonObject(
    "parent" to "item/generated".jsonElement,
    "textures" to jsonObject(
        "layer0" to "$resourceName".jsonElement
    )
)

private fun handheld(resourceName: ResourceName) = jsonObject(
    "parent" to "item/handheld".jsonElement,
    "textures" to jsonObject(
        "layer0" to "$resourceName".jsonElement
    )
)

private fun block(parent: ResourceName) = jsonObject(
    "parent" to "$parent".jsonElement
)
