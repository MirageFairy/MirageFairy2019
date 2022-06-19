package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName

fun ModInitializer.makeBlockModel(resourceName: ResourceName, creator: () -> JsonElement) = onMakeResource {
    place("assets/${resourceName.domain}/models/block/${resourceName.path}.json", creator())
}
