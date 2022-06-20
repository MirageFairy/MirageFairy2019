package miragefairy2019.resourcemaker

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.map


fun ModInitializer.makeItemModel(path: String, creator: MakeItemModelScope.() -> DataItemModel) = onMakeResource {
    place("assets/$modId/models/item/$path.json", MakeItemModelScope(ResourceName(modId, path)).creator().jsonElement)
}

class MakeItemModelScope(val resourceName: ResourceName)


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
