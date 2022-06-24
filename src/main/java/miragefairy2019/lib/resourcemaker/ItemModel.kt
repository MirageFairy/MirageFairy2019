package miragefairy2019.lib.resourcemaker

import miragefairy2019.common.ResourceName
import miragefairy2019.common.map
import miragefairy2019.lib.modinitializer.ModInitializer
import miragefairy2019.lib.modinitializer.NamedInitializer


fun ModInitializer.makeItemModel(path: String, creator: MakeItemModelScope.() -> DataModel) = onMakeResource {
    place("assets/$modId/models/item/$path.json", MakeItemModelScope(ResourceName(modId, path)).creator().jsonElement)
}

fun NamedInitializer.makeItemModel(creator: MakeItemModelScope.() -> DataModel) = modInitializer.onMakeResource {
    place("assets/${resourceName.domain}/models/item/${resourceName.path}.json", MakeItemModelScope(resourceName).creator().jsonElement)
}

class MakeItemModelScope(val resourceName: ResourceName)


val MakeItemModelScope.generated
    get() = DataModel(
        parent = "item/generated",
        textures = mapOf(
            "layer0" to "${resourceName.map { "items/$it" }}"
        )
    )
val MakeItemModelScope.handheld
    get() = DataModel(
        parent = "item/handheld",
        textures = mapOf(
            "layer0" to "${resourceName.map { "items/$it" }}"
        )
    )
val MakeItemModelScope.block
    get() = DataModel(
        parent = "${resourceName.map { "block/$it" }}"
    )
val MakeItemModelScope.fluid
    get() = DataModel(
        parent = "item/generated",
        textures = mapOf(
            "layer0" to "${resourceName.map { "blocks/${it}_still" }}"
        )
    )
