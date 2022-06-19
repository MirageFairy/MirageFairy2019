package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer

fun ModInitializer.makeAdvancement(path: String, creator: () -> JsonElement) = onMakeResource {
    place("assets/$modId/advancements/$path.json", creator())
}
