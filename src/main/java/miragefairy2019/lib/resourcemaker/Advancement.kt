package miragefairy2019.lib.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.lib.modinitializer.ModInitializer
import miragefairy2019.lib.modinitializer.NamedInitializer

fun ModInitializer.makeAdvancement(path: String, creator: () -> JsonElement) = onMakeResource {
    place("assets/$modId/advancements/$path.json", creator())
}

fun NamedInitializer.makeAdvancement(creator: () -> JsonElement) = modInitializer.onMakeResource {
    place("assets/${resourceName.domain}/advancements/${resourceName.path}.json", creator())
}
