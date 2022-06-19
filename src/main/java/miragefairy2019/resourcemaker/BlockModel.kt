package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer

fun ModInitializer.makeBlockModel(path: String, creator: () -> JsonElement) = onMakeResource {
    place("assets/$modId/models/block/$path.json", creator())
}
