package miragefairy2019.lib.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.NamedScope

fun ModScope.makeAdvancement(path: String, creator: () -> JsonElement) = onMakeResource {
    place("assets/$modId/advancements/$path.json", creator())
}

fun NamedScope.makeAdvancement(creator: () -> JsonElement) = modScope.onMakeResource {
    place("assets/${resourceName.domain}/advancements/${resourceName.path}.json", creator())
}
