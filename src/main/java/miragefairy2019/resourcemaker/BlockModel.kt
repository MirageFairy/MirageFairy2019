package miragefairy2019.resourcemaker

import miragefairy2019.libkt.ModInitializer

fun ModInitializer.makeBlockModel(path: String, creator: () -> DataModel) = onMakeResource {
    place("assets/$modId/models/block/$path.json", creator().jsonElement)
}
