package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName

fun ModInitializer.makeAdvancement(resourceName: ResourceName, creator: () -> JsonElement) = onMakeResource {
    place("assets/${resourceName.domain}/advancements/${resourceName.path}.json", creator())
}
